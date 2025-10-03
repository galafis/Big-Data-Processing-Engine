package com.galafis.bigdataprocessingengine;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class BigDataProcessingSystemTest {

    private BigDataProcessingSystem system;
    private IDataProcessor dataProcessor;

    @Before
    public void setUp() {
        system = new BigDataProcessingSystem();
        dataProcessor = new DefaultDataProcessor(Executors.newFixedThreadPool(1)); // Usar um pool pequeno para testes
    }

    @Test
    public void testSystemInitializationAndProcessing() throws ExecutionException, InterruptedException {
        assertNotNull("System should not be null", system);

        system.initialize().get();
        assertTrue("Data records should be initialized", system.getDataRecords().size() > 0);

        BigDataProcessingSystem.AnalysisResult result = system.processData().get();
        assertNotNull("Analysis result should not be null", result);
        assertTrue("Summary should not be empty", result.getSummary().size() > 0);
        assertTrue("Insights should not be empty", result.getInsights().size() > 0);
        assertTrue("Recommendations should not be empty", result.getRecommendations().size() > 0);
    }

    @Test
    public void testDataRecordCreation() {
        LocalDateTime now = LocalDateTime.now();
        Map<String, Object> metadata = Map.of("key", "value");
        BigDataProcessingSystem.DataRecord record = new BigDataProcessingSystem.DataRecord("id1", now, 100.0, metadata);

        assertEquals("id1", record.getId());
        assertEquals(now, record.getTimestamp());
        assertEquals(100.0, record.getValue(), 0.001);
        assertEquals("value", record.getMetadata().get("key"));
    }

    @Test
    public void testAnalysisResultCreation() {
        Map<String, Double> summary = Map.of("totalRecords", 10.0);
        List<String> insights = List.of("insight1");
        List<String> recommendations = List.of("recommendation1");
        BigDataProcessingSystem.AnalysisResult result = new BigDataProcessingSystem.AnalysisResult(summary, insights, recommendations, 100L);

        assertEquals(10.0, result.getSummary().get("totalRecords"), 0.001);
        assertEquals("insight1", result.getInsights().get(0));
        assertEquals("recommendation1", result.getRecommendations().get(0));
        assertEquals(100L, result.getProcessingTimeMs());
    }

    @Test
    public void testGenerateSampleData() throws ExecutionException, InterruptedException {
        system.initialize().get(); // This calls generateSampleData
        List<BigDataProcessingSystem.DataRecord> records = system.getDataRecords();
        assertFalse("Sample data should not be empty", records.isEmpty());
        assertEquals("Should generate 1000 records by default", 1000, records.size());
        // Check some properties of generated data
        BigDataProcessingSystem.DataRecord firstRecord = records.get(0);
        assertNotNull(firstRecord.getId());
        assertNotNull(firstRecord.getTimestamp());
        assertTrue(firstRecord.getValue() >= 0 && firstRecord.getValue() <= 1000);
        assertTrue(firstRecord.getMetadata().containsKey("category"));
    }

    @Test
    public void testDefaultDataProcessorSummaryCalculation() throws ExecutionException, InterruptedException {
        // Manually add some data for predictable summary calculation
        List<BigDataProcessingSystem.DataRecord> testRecords = List.of(
                new BigDataProcessingSystem.DataRecord("rec1", LocalDateTime.now(), 10.0, Map.of()),
                new BigDataProcessingSystem.DataRecord("rec2", LocalDateTime.now(), 20.0, Map.of()),
                new BigDataProcessingSystem.DataRecord("rec3", LocalDateTime.now(), 30.0, Map.of())
        );

        BigDataProcessingSystem.AnalysisResult result = dataProcessor.process(testRecords).get();
        Map<String, Double> summary = result.getSummary();

        assertEquals(3.0, summary.get("totalRecords"), 0.001);
        assertEquals(20.0, summary.get("averageValue"), 0.001);
        assertEquals(30.0, summary.get("maxValue"), 0.001);
        assertEquals(10.0, summary.get("minValue"), 0.001);
    }

    @Test
    public void testDefaultDataProcessorInsightsGeneration() throws ExecutionException, InterruptedException {
        // Add data with specific categories to test insights
        List<BigDataProcessingSystem.DataRecord> testRecords = List.of(
                new BigDataProcessingSystem.DataRecord("recA1", LocalDateTime.now(), 100.0, Map.of("category", "A")),
                new BigDataProcessingSystem.DataRecord("recA2", LocalDateTime.now(), 150.0, Map.of("category", "A")),
                new BigDataProcessingSystem.DataRecord("recB1", LocalDateTime.now(), 50.0, Map.of("category", "B"))
        );

        BigDataProcessingSystem.AnalysisResult result = dataProcessor.process(testRecords).get();
        List<String> insights = result.getInsights();

        assertFalse("Insights should not be empty", insights.isEmpty());
        assertTrue(insights.stream().anyMatch(s -> s.contains("Category 'A' represents")));
    }

    @Test
    public void testDefaultDataProcessorRecommendationsGeneration() throws ExecutionException, InterruptedException {
        // Test case for low data count recommendation
        List<BigDataProcessingSystem.DataRecord> lowDataRecords = List.of(
                new BigDataProcessingSystem.DataRecord("rec1", LocalDateTime.now(), 10.0, Map.of())
        );

        BigDataProcessingSystem.AnalysisResult resultLowData = dataProcessor.process(lowDataRecords).get();
        List<String> recommendationsLowData = resultLowData.getRecommendations();
        assertTrue(recommendationsLowData.stream().anyMatch(s -> s.contains("Consider increasing data collection")));

        // Test case for no specific recommendations
        List<BigDataProcessingSystem.DataRecord> sufficientDataRecords = new java.util.ArrayList<>();
        for (int i = 0; i < 200; i++) {
            sufficientDataRecords.add(new BigDataProcessingSystem.DataRecord("rec" + i, LocalDateTime.now().minusHours(i), 100.0, Map.of()));
        }
        BigDataProcessingSystem.AnalysisResult resultSufficientData = dataProcessor.process(sufficientDataRecords).get();
        List<String> recommendationsSufficientData = resultSufficientData.getRecommendations();
        assertTrue(recommendationsSufficientData.stream().anyMatch(s -> s.contains("No specific recommendations based on current data")));
    }

    @Test
    public void testExportData() throws ExecutionException, InterruptedException {
        system.initialize().get();
        Map<String, Object> exportedData = system.exportData();

        assertNotNull("Exported data should not be null", exportedData);
        assertTrue(exportedData.containsKey("data"));
        assertTrue(exportedData.containsKey("exportTime"));
        assertTrue(exportedData.containsKey("recordCount"));
        assertTrue(exportedData.containsKey("systemVersion"));
        assertEquals(system.getDataRecords().size(), exportedData.get("recordCount"));
    }

    @Test
    public void testShutdown() {
        system.shutdown();
        assertTrue("Executor service should be shut down", system.isShutdown());
    }
}

