package com.assignment.tamyuz.discount.service.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/actuator")
@Tag(name = "Actuator (Admin)", description = "Admin-only actuator proxy endpoints")
@SecurityRequirement(name = "bearerAuth")
public class ActuatorProxyController {

    private final HealthEndpoint healthEndpoint;
    private final InfoEndpoint infoEndpoint;
    private final MetricsEndpoint metricsEndpoint;

    @Operation(
            summary = "Health status",
            description = "Returns application health information",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Health info returned")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/health")
    public HealthComponent getHealth() {
        return healthEndpoint.health();
    }

    @Operation(summary = "Application info")
    @ApiResponse(responseCode = "200", description = "Info returned")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/info")
    public Map<String, Object> getInfo() {
        return infoEndpoint.info();
    }

    @Operation(summary = "List metrics names")
    @ApiResponse(responseCode = "200", description = "Metrics names list")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/metrics")
    public Object getMetrics() {
        return metricsEndpoint.listNames();
    }

    @Operation(summary = "Complete actuator info (health + info + metrics)")
    @ApiResponse(responseCode = "200", description = "Complete actuator data")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/complete")
    public Map<String, Object> getAdminActuatorInfo() {
        return Map.of(
                "health", healthEndpoint.health(),
                "info", infoEndpoint.info(),
                "metrics", metricsEndpoint.listNames()
        );
    }
}