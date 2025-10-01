package com.galafis.bigdataprocessingengine;

import org.junit.Test;
import static org.junit.Assert.*;

public class BigDataProcessingSystemTest {

    @Test
    public void testSystemInitializationAndProcessing() throws Exception {
        BigDataProcessingSystem system = new BigDataProcessingSystem();
        assertNotNull("System should not be null", system);

        system.initialize().get();
        assertTrue("Data records should be initialized", system.getDataRecords().size() > 0);

        BigDataProcessingSystem.AnalysisResult result = system.processData().get();
        assertNotNull("Analysis result should not be null", result);
        assertTrue("Summary should not be empty", result.getSummary().size() > 0);
        assertTrue("Insights should not be empty", result.getInsights().size() > 0);
        assertTrue("Recommendations should not be empty", result.getRecommendations().size() > 0);

        system.shutdown();
    }
}
