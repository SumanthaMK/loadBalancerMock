package com.sumantha.loadBalancerMock.service;

import org.springframework.stereotype.Service;

import java.util.Map;


public interface HealthCheckService {

    void checkHealthStatus();

    Map<String, Boolean> getIpStatus();

    String getAvailableIp();
}
