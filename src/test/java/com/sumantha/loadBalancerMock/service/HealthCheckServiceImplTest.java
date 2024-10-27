package com.sumantha.loadBalancerMock.service;

import com.sumantha.loadBalancerMock.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static com.sumantha.loadBalancerMock.constants.AppConstants.IP_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HealthCheckServiceImplTest {

    @InjectMocks
    private HealthCheckServiceImpl healthCheckService;

    private final String[] testIpAddresses = {"192.168.1.1", "192.168.1.2", "192.168.1.3", "192.168.1.4", "192.168.1.5"};

    @BeforeEach
    public void setUp() {

        ReflectionTestUtils.setField(healthCheckService, "ipAddresses", testIpAddresses);
        healthCheckService.initializeIpStatus();
    }

    @Test
    public void testInitializeIpStatus() {
        Map<String, Boolean> ipStatus = healthCheckService.getIpStatus();
        assertEquals(5, ipStatus.size());
        assertTrue(ipStatus.values().stream().allMatch(status -> status));
    }

    @Test
    public void testCheckHealthStatus() {

        HealthCheckServiceImpl spyService = spy(healthCheckService);

        // Mock simulateHealthCheck
        when(spyService.simulateHealthCheck()).thenReturn(true, false, true, false, true);
        spyService.checkHealthStatus();

        Map<String, Boolean> ipStatus = spyService.getIpStatus();

        assertTrue(ipStatus.get("192.168.1.1"));
        assertFalse(ipStatus.get("192.168.1.2"));
        assertTrue(ipStatus.get("192.168.1.3"));
        assertFalse(ipStatus.get("192.168.1.4"));
        assertTrue(ipStatus.get("192.168.1.5"));
    }

    @Test
    public void testGetIpStatus() {
        Map<String, Boolean> ipStatus = healthCheckService.getIpStatus();
        assertEquals(5, ipStatus.size());
    }

    @Test
    public void testGetAvailableIp_Success() {
        // Make one IP address healthy
        healthCheckService.getIpStatus().put("192.168.1.1", true);
        String availableIp = healthCheckService.getAvailableIp();
        assertEquals("192.168.1.1", availableIp);
    }

    @Test
    public void testGetAvailableIp_NoAvailableIp() {
        // Set all IPs to unhealthy
        healthCheckService.getIpStatus().replaceAll((ip, status) -> false);

        // Expect CustomException with IP_NOT_FOUND message
        CustomException exception = assertThrows(CustomException.class, () -> healthCheckService.getAvailableIp());
        assertEquals(IP_NOT_FOUND, exception.getMessage());
    }
}

