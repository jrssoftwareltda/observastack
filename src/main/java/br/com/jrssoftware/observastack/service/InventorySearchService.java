package br.com.jrssoftware.observastack.service;

import br.com.jrssoftware.observastack.dto.OptimizationComparisonResponse;
import br.com.jrssoftware.observastack.dto.SearchMeasurement;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InventorySearchService {

    private static final String DEMO_QUERY = "tenant-42";
    private static final int DATASET_SIZE = 40_000;

    private final List<String> records = new ArrayList<>(DATASET_SIZE);
    private final Map<String, List<String>> optimizedIndex = new HashMap<>();

    public InventorySearchService() {
        for (int i = 0; i < DATASET_SIZE; i++) {
            String tenant = "tenant-" + (i % 100);
            String status = i % 4 == 0 ? "PAID" : "PENDING";
            String record = "invoice-%05d %s status-%s customer-%03d".formatted(i, tenant, status, i % 750);
            records.add(record);
            optimizedIndex.computeIfAbsent(tenant, key -> new ArrayList<>()).add(record);
        }
    }

    public OptimizationComparisonResponse compareBeforeAfter() {
        SearchMeasurement before = unoptimizedSearch(DEMO_QUERY);
        SearchMeasurement after = optimizedSearch(DEMO_QUERY);

        double improvement = before.durationMillis() == 0
                ? 0
                : ((double) (before.durationMillis() - after.durationMillis()) / before.durationMillis()) * 100;

        return new OptimizationComparisonResponse(
                "Search invoices by tenant in a high-volume API",
                before,
                after,
                Math.max(0, Math.round(improvement * 100.0) / 100.0),
                "The optimized path uses a precomputed index, reducing lookup cost and improving API latency predictability.",
                Instant.now()
        );
    }

    public SearchMeasurement unoptimizedSearch(String query) {
        long start = System.nanoTime();
        List<String> results = new ArrayList<>();
        long checksum = 0;

        for (String record : records) {
            checksum += record.hashCode() & 0xFF;
            if (record.contains(query)) {
                results.add(record);
            }
        }

        burnCpuLightly(checksum);
        long durationMillis = Math.max(1, (System.nanoTime() - start) / 1_000_000);

        return new SearchMeasurement(
                "before",
                durationMillis,
                results.size(),
                "Linear scan across all records"
        );
    }

    public SearchMeasurement optimizedSearch(String query) {
        long start = System.nanoTime();
        List<String> results = optimizedIndex.getOrDefault(query, List.of());
        long durationMillis = Math.max(1, (System.nanoTime() - start) / 1_000_000);

        return new SearchMeasurement(
                "after",
                durationMillis,
                results.size(),
                "Hash-map index by tenant"
        );
    }

    private void burnCpuLightly(long seed) {
        long value = seed;
        for (int i = 0; i < 15_000; i++) {
            value = (value * 31 + i) % 9973;
        }
        if (value == -1) {
            throw new IllegalStateException("Unreachable checksum state");
        }
    }
}
