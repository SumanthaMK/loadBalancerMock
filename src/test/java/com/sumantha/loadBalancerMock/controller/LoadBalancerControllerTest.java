package com.sumantha.loadBalancerMock.controller;

import com.sumantha.loadBalancerMock.dto.response.ResponseDTO;
import com.sumantha.loadBalancerMock.service.HealthCheckService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static com.sumantha.loadBalancerMock.constants.AppConstants.IP_STATUS_SUCCESS_MESSAGE;
import static com.sumantha.loadBalancerMock.constants.AppConstants.ROUTING_REQUEST_SUCCESS_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class LoadBalancerControllerTest {

    @InjectMocks
    private LoadBalancerController loadBalancerController;

    @Mock
    private HealthCheckService healthCheckService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRouteRequest() {
        String availableIp = "192.168.1.1";
        when(healthCheckService.getAvailableIp()).thenReturn(availableIp);

        ResponseEntity<ResponseDTO<String>> response = loadBalancerController.routeRequest();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ROUTING_REQUEST_SUCCESS_MESSAGE, response.getBody().getMessage());
        assertEquals("Routing request to: " + availableIp, response.getBody().getData());
        assertTrue(response.getBody().isStatus());

        verify(healthCheckService, times(1)).checkHealthStatus();
    }

    @Test
    public void testGetIpStatus() {

        Map<String, Boolean> ipStatus = new HashMap<>();
        ipStatus.put("192.168.1.1", true);
        ipStatus.put("192.168.1.2", false);
        when(healthCheckService.getIpStatus()).thenReturn(ipStatus);

        ResponseEntity<ResponseDTO<Map<String, Boolean>>> response = loadBalancerController.getIpStatus();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(IP_STATUS_SUCCESS_MESSAGE, response.getBody().getMessage());
        assertEquals(ipStatus, response.getBody().getData());
        assertTrue(response.getBody().isStatus());

        verify(healthCheckService, times(1)).checkHealthStatus();
    }
}

