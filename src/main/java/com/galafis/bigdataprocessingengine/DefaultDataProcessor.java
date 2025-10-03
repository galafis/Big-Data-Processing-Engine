package com.galafis.bigdataprocessingengine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class DefaultDataProcessor implements IDataProcessor {

    private static final Logger logger = LoggerFactory.getLogger(DefaultDataProcessor.class);
    private final ExecutorService executorService;

    public DefaultDataProcessor(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public CompletableFuture<BigDataProcessingSystem.AnalysisResult> process(List<BigDataProcessingSystem.DataRecord> records) {
        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            logger.info("Starting data processing with DefaultDataProcessor...");

            try {
                Map<String, Double> summary = calculateSummary(records);
                List<String> insights = generateInsights(records);
                List<String> recommendations = generateRecommendations(records);

                long processingTime = System.currentTimeMillis() - startTime;
                logger.info("Data processing completed in {}ms", processingTime);

                return new BigDataProcessingSystem.AnalysisResult(summary, insights, recommendations, processingTime);

            } catch (Exception e) {
                logger.error("Data processing failed in DefaultDataProcessor", e);
                throw new RuntimeException("Data processing failed", e);
            }
        }, executorService);
    }

    private Map<String, Double> calculateSummary(List<BigDataProcessingSystem.DataRecord> records) {
        Map<String, Double> summary = new HashMap<>();

        summary.put("totalRecords", (double) records.size());

        double averageValue = records.stream()
                .mapToDouble(BigDataProcessingSystem.DataRecord::getValue)
                .average()
                .orElse(0.0);
        summary.put("averageValue", Math.round(averageValue * 100.0) / 100.0);

        double maxValue = records.stream()
                .mapToDouble(BigDataProcessingSystem.DataRecord::getValue)
                .max()
                .orElse(0.0);
        summary.put("maxValue", maxValue);

        double minValue = records.stream()
                .mapToDouble(BigDataProcessingSystem.DataRecord::getValue)
                .min()
                .orElse(0.0);
        summary.put("minValue", minValue);

        logger.debug("Calculated summary statistics: {}", summary);
        return summary;
    }

    private List<String> generateInsights(List<BigDataProcessingSystem.DataRecord> records) {
        List<String> insights = new ArrayList<>();

        Map<String, Long> categoryCount = records.stream()
                .collect(Collectors.groupingBy(
                        record -> {
                            String category = (String) record.getMetadata().get("category");
                            return category != null ? category : "Unknown";
                        },
                        Collectors.counting()
                ));

        Optional<Map.Entry<String, Long>> dominantCategoryEntry = categoryCount.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        if (dominantCategoryEntry.isPresent()) {
            String dominantCategory = dominantCategoryEntry.get().getKey();
            double percentage = (dominantCategoryEntry.get().getValue() * 100.0) / records.size();
            insights.add(String.format("Category \'%s\' represents %.1f%% of all data", dominantCategory, percentage));
        }

        double avgValue = records.stream().mapToDouble(BigDataProcessingSystem.DataRecord::getValue).average().orElse(0.0);
        long highValueCount = records.stream()
                .mapToDouble(BigDataProcessingSystem.DataRecord::getValue)
                .filter(value -> value > avgValue * 1.5)
                .count();

        if (highValueCount > 0) {
            insights.add(String.format("%d records show significantly high values (>150%% of average)", highValueCount));
        }

        logger.debug("Generated insights: {}", insights);
        return insights;
    }

    private List<String> generateRecommendations(List<BigDataProcessingSystem.DataRecord> records) {
        List<String> recommendations = new ArrayList<>();

        if (records.size() < 100) {
            recommendations.add("Consider increasing data collection for more robust analysis");
        }

        long recentDataCount = records.stream()
                .filter(record -> record.getTimestamp().isAfter(LocalDateTime.now().minusDays(1)))
                .count();

        if ((double) recentDataCount / records.size() < 0.1) {
            recommendations.add("Data appears outdated - consider refreshing data sources");
        }

        if (recommendations.isEmpty()) {
            recommendations.add("No specific recommendations based on current data, but continuous monitoring is advised.");
        }
        logger.debug("Generated recommendations: {}", recommendations);
        return recommendations;
    }
}

