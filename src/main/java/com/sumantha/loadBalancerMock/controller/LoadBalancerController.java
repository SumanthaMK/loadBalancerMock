package com.sumantha.loadBalancerMock.controller;


import com.sumantha.loadBalancerMock.dto.response.ResponseDTO;
import com.sumantha.loadBalancerMock.service.HealthCheckService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.sumantha.loadBalancerMock.constants.AppConstants.IP_STATUS_SUCCESS_MESSAGE;
import static com.sumantha.loadBalancerMock.constants.AppConstants.ROUTING_REQUEST_SUCCESS_MESSAGE;

@RestController
@RequestMapping("/loadBalancer")
public class LoadBalancerController {

    private final HealthCheckService healthCheckService;

    public LoadBalancerController(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }

    @GetMapping("/route")
    public ResponseEntity<ResponseDTO<String>> routeRequest() {
        healthCheckService.checkHealthStatus(); // Update health status
        String availableIp = healthCheckService.getAvailableIp();

        ResponseDTO<String> response = new ResponseDTO<>(
                ROUTING_REQUEST_SUCCESS_MESSAGE,
                "Routing request to: " + availableIp,
                true
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    public ResponseEntity<ResponseDTO<Map<String, Boolean>>> getIpStatus() {
        healthCheckService.checkHealthStatus();
        Map<String, Boolean> ipStatus = healthCheckService.getIpStatus();

        ResponseDTO<Map<String, Boolean>> response = new ResponseDTO<>(
                IP_STATUS_SUCCESS_MESSAGE,
                ipStatus,
                Boolean.TRUE
        );

        return ResponseEntity.ok(response);
    }
}

