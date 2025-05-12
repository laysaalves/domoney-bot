package dev.layseiras.domoney.service;

import dev.layseiras.domoney.config.DashboardApiClient;
import dev.layseiras.domoney.dto.DashboardApiDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardApiService {

    private final DashboardApiClient dashboardApiClient;

    public DashboardApiService(DashboardApiClient dashboardApiClient) {
        this.dashboardApiClient = dashboardApiClient;
    }

    public List<DashboardApiDTO> getAllActivities() {
        return dashboardApiClient.getAllActivities();
    }
}
