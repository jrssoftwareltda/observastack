package br.com.jrssoftware.observastack.controller;

import br.com.jrssoftware.observastack.dto.AlertSimulationResponse;
import br.com.jrssoftware.observastack.dto.ObservabilityReport;
import br.com.jrssoftware.observastack.dto.OptimizationComparisonResponse;
import br.com.jrssoftware.observastack.dto.StructuredLogSample;
import br.com.jrssoftware.observastack.dto.TrafficSimulationResponse;
import br.com.jrssoftware.observastack.service.InventorySearchService;
import br.com.jrssoftware.observastack.service.ObservabilityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/observability")
public class ObservabilityController {

    private final ObservabilityService observabilityService;
    private final InventorySearchService inventorySearchService;

    public ObservabilityController(
            ObservabilityService observabilityService,
            InventorySearchService inventorySearchService
    ) {
        this.observabilityService = observabilityService;
        this.inventorySearchService = inventorySearchService;
    }

    @GetMapping("/report")
    public ObservabilityReport report() {
        return observabilityService.report();
    }

    @PostMapping("/traffic")
    public TrafficSimulationResponse simulateTraffic(
            @RequestParam(defaultValue = "25") int requests,
            @RequestParam(defaultValue = "true") boolean optimized
    ) {
        return observabilityService.simulateTraffic(requests, optimized);
    }

    @PostMapping("/alerts/simulate")
    public AlertSimulationResponse simulateAlert(@RequestParam(defaultValue = "latency") String type) {
        return observabilityService.simulateAlert(type);
    }

    @GetMapping("/optimization/compare")
    public OptimizationComparisonResponse compareOptimization() {
        return inventorySearchService.compareBeforeAfter();
    }

    @GetMapping("/logs/sample")
    public StructuredLogSample structuredLogSample() {
        return observabilityService.structuredLogSample();
    }
}
