package com.galafis.bigdataprocessingengine;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Big-Data-Processing-Engine - Professional Java Implementation
 * Enterprise-grade BigDataProcessing system
 * 
 * @author Gabriel Demetrios Lafis
 * @version 1.0.0
 */
public class BigDataProcessingSystem {
    
    private static final Logger logger = LoggerFactory.getLogger(BigDataProcessingSystem.class);
    
    private final List<DataRecord> dataRecords;
    private final ExecutorService executorService;
    private final Map<String, Object> configuration;

    // Getter for testing purposes
    public List<DataRecord> getDataRecords() {
        return dataRecords;
    }

    // Method to check if the executor service is shut down
    public boolean isShutdown() {
        return executorService.isShutdown();
    }
    
    public BigDataProcessingSystem() {
        this.dataRecords = new CopyOnWriteArrayList<>();
        this.executorService = Executors.newFixedThreadPool(10);
        this.configuration = new ConcurrentHashMap<>();
        initializeConfiguration();
    }
    
    /**
     * Initialize system configuration
     */
    private void initializeConfiguration() {
        configuration.put("batchSize", 1000);
        configuration.put("timeout", 30000);
        configuration.put("retryAttempts", 3);
        configuration.put("enableLogging", true);
        logger.info("System configuration initialized.");
    }
    
    /**
     * Data record model
     */
    public static class DataRecord {
        private final String id;
        private final LocalDateTime timestamp;
        private final double value;
        private final Map<String, Object> metadata;
        
        public DataRecord(String id, LocalDateTime timestamp, double value, Map<String, Object> metadata) {
            this.id = id;
            this.timestamp = timestamp;
            this.value = value;
            this.metadata = new HashMap<>(metadata);
        }
        
        // Getters
        public String getId() { return id; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public double getValue() { return value; }
        public Map<String, Object> getMetadata() { return new HashMap<>(metadata); }
        
        @Override
        public String toString() {
            return String.format("DataRecord{id=\'%s\', timestamp=%s, value=%.2f}", 
                               id, timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), value);
        }
    }
    
    /**
     * Analysis result model
     */
    public static class AnalysisResult {
        private final Map<String, Double> summary;
        private final List<String> insights;
        private final List<String> recommendations;
        private final long processingTimeMs;
        
        public AnalysisResult(Map<String, Double> summary, List<String> insights, 
                            List<String> recommendations, long processingTimeMs) {
            this.summary = new HashMap<>(summary);
            this.insights = new ArrayList<>(insights);
            this.recommendations = new ArrayList<>(recommendations);
            this.processingTimeMs = processingTimeMs;
        }
        
        // Getters
        public Map<String, Double> getSummary() { return new HashMap<>(summary); }
        public List<String> getInsights() { return new ArrayList<>(insights); }
        public List<String> getRecommendations() { return new ArrayList<>(recommendations); }
        public long getProcessingTimeMs() { return processingTimeMs; }
    }
    
    /**
     * Initialize the system with sample data
     */
    public CompletableFuture<Void> initialize() {
        return CompletableFuture.runAsync(() -> {
            logger.info("Initializing Big-Data-Processing-Engine System...");
            generateSampleData(1000);
            logger.info("System initialized with {} records", dataRecords.size());
        }, executorService);
    }
    
    /**
     * Generate sample data for demonstration
     */
    private void generateSampleData(int count) {
        Random random = new Random();
        String[] categories = {"A", "B", "C"};
        
        for (int i = 0; i < count; i++) {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("category", categories[random.nextInt(categories.length)]);
            metadata.put("priority", random.nextInt(5) + 1);
            metadata.put("source", "generated");
            
            DataRecord record = new DataRecord(
                "record-" + (i + 1),
                LocalDateTime.now().minusHours(random.nextInt(24)),
                random.nextDouble() * 1000,
                metadata
            );
            
            dataRecords.add(record);
        }
        logger.debug("Generated {} sample data records.", count);
    }
    
    /**
     * Process data and generate comprehensive analysis
     */
    public CompletableFuture<AnalysisResult> processData() {
        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            logger.info("Starting data processing...");
            
            try {
                Map<String, Double> summary = calculateSummary();
                List<String> insights = generateInsights();
                List<String> recommendations = generateRecommendations();
                
                long processingTime = System.currentTimeMillis() - startTime;
                logger.info("Data processing completed in {}ms", processingTime);
                
                return new AnalysisResult(summary, insights, recommendations, processingTime);
                
            } catch (Exception e) {
                logger.error("Data processing failed", e);
                throw new RuntimeException("Data processing failed", e);
            }
        }, executorService);
    }
    
    /**
     * Calculate summary statistics
     */
    private Map<String, Double> calculateSummary() {
        Map<String, Double> summary = new HashMap<>();
        
        summary.put("totalRecords", (double) dataRecords.size());
        
        double averageValue = dataRecords.stream()
            .mapToDouble(DataRecord::getValue)
            .average()
            .orElse(0.0);
        summary.put("averageValue", Math.round(averageValue * 100.0) / 100.0);
        
        double maxValue = dataRecords.stream()
            .mapToDouble(DataRecord::getValue)
            .max()
            .orElse(0.0);
        summary.put("maxValue", maxValue);
        
        double minValue = dataRecords.stream()
            .mapToDouble(DataRecord::getValue)
            .min()
            .orElse(0.0);
        summary.put("minValue", minValue);
        
        logger.debug("Calculated summary statistics: {}", summary);
        return summary;
    }
    
    /**
     * Generate insights from data analysis
     */
    private List<String> generateInsights() {
        List<String> insights = new ArrayList<>();
        
        // Category distribution analysis
        Map<String, Long> categoryCount = dataRecords.stream()
            .collect(Collectors.groupingBy(
                record -> {
                    String category = (String) record.getMetadata().get("category");
                    return category != null ? category : "Unknown";
                },
                Collectors.counting()
            ));
        
        String dominantCategory = categoryCount.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("Unknown");
        
        double percentage = (categoryCount.get(dominantCategory) * 100.0) / dataRecords.size();
        insights.add(String.format("Category \'%s\' represents %.1f%% of all data", dominantCategory, percentage));
        
        // Value analysis
        double avgValue = dataRecords.stream().mapToDouble(DataRecord::getValue).average().orElse(0.0);
        long highValueCount = dataRecords.stream()
            .mapToDouble(DataRecord::getValue)
            .filter(value -> value > avgValue * 1.5)
            .count();
        
        if (highValueCount > 0) {
            insights.add(String.format("%d records show significantly high values (>150%% of average)", highValueCount));
        }
        
        logger.debug("Generated insights: {}", insights);
        return insights;
    }
    
    /**
     * Generate recommendations based on analysis
     */
    private List<String> generateRecommendations() {
        List<String> recommendations = new ArrayList<>();
        
        if (dataRecords.size() < 100) {
            recommendations.add("Consider increasing data collection for more robust analysis");
        }
        
        long recentDataCount = dataRecords.stream()
            .filter(record -> record.getTimestamp().isAfter(LocalDateTime.now().minusDays(1)))
            .count();
        
        if ((double) recentDataCount / dataRecords.size() < 0.1) {
            recommendations.add("Data appears outdated - consider refreshing data sources");
        }
        
        // Ensure at least one recommendation for testing purposes if no other conditions are met
        if (recommendations.isEmpty()) {
            recommendations.add("No specific recommendations based on current data, but continuous monitoring is advised.");
        }
        logger.debug("Generated recommendations: {}", recommendations);
        return recommendations;
    }
    
    /**
     * Export system data and metadata
     */
    public Map<String, Object> exportData() {
        Map<String, Object> export = new HashMap<>();
        export.put("data", new ArrayList<>(dataRecords));
        export.put("exportTime", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        export.put("recordCount", dataRecords.size());
        export.put("systemVersion", "1.0.0");
        
        logger.info("Data exported successfully. Record count: {}", dataRecords.size());
        return export;
    }
    
    /**
     * Shutdown the system gracefully
     */
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                logger.warn("Executor service did not terminate in time, forcing shutdown.");
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
            logger.error("Shutdown interrupted.", e);
        }
        logger.info("Big-Data-Processing-Engine System shutdown complete.");
    }
    
    /**
     * Main method for standalone execution
     */
    public static void main(String[] args) {
        logger.info("Starting Big-Data-Processing-Engine...");
        
        BigDataProcessingSystem system = new BigDataProcessingSystem();
        
        try {
            // Initialize system
            system.initialize().get();
            
            // Process data
            AnalysisResult result = system.processData().get();
            
            // Display results
            logger.info("Analysis completed in {}ms", result.getProcessingTimeMs());
            logger.info("Summary: {}", result.getSummary());
            logger.info("Insights: {}", result.getInsights());
            logger.info("Recommendations: {}", result.getRecommendations());
            
            logger.info("System running successfully!");
            
        } catch (Exception e) {
            logger.error("Error during system execution", e);
        } finally {
            system.shutdown();
        }
    }
}

