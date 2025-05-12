package dev.layseiras.domoney.config;

import dev.layseiras.domoney.dto.DashboardApiDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "dashboardApiClient", url = "http://localhost:8080")
public interface DashboardApiClient {

        @GetMapping("/activity")
        List<DashboardApiDTO> getAllActivities();

}
