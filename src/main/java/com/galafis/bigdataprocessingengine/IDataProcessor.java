package com.galafis.bigdataprocessingengine;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IDataProcessor {
    CompletableFuture<BigDataProcessingSystem.AnalysisResult> process(List<BigDataProcessingSystem.DataRecord> records);
}

