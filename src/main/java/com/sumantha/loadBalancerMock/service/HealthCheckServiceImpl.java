package com.sumantha.loadBalancerMock.service;

import com.sumantha.loadBalancerMock.exception.CustomException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

import static com.sumantha.loadBalancerMock.constants.AppConstants.IP_NOT_FOUND;

/**
 * @author Sumanth
 */
@Service
public class HealthCheckServiceImpl implements HealthCheckService {

    private final Map<String, Boolean> ipStatus = new HashMap<>();

    @Value("${loadbalancer.ipAddresses}")
    private String[] ipAddresses;

    /**
     * Set All IP Health Value To True Initially
     */
    @PostConstruct
    public void initializeIpStatus() {
        for (String ip : ipAddresses) {
            ipStatus.put(ip, true);
        }
    }

    /**
     * Simulate a health check for each IP
     */
    @Override
    public void checkHealthStatus() {
        for (String ip : ipAddresses) {
            boolean isHealthy = simulateHealthCheck();
            System.out.println("IP Address "+ ip +" Ping "+isHealthy);
            ipStatus.put(ip, isHealthy);
        }
    }

    /**
     * To Mock Health Check
     * @return
     */
    public boolean simulateHealthCheck() {
        return Math.random() > 0.3; // 70% chance the IP is "healthy"
    }

    /**
     *
     * @return IP Status
     */
    @Override
    public Map<String, Boolean> getIpStatus() {
        return ipStatus;
    }

    /**
     *
     * @return Available Healthy IP
     */
    @Override
    public String getAvailableIp() {
        return ipStatus.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new CustomException(IP_NOT_FOUND));
    }
}
