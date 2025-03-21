package com.project.assignment.retailer;

import com.project.assignment.retailer.controller.RewardController;
import com.project.assignment.retailer.dto.CustomerDto;
import com.project.assignment.retailer.service.RewardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.when;

public class RewardControllerIntegrationTest {

    private MockMvc mockMvc;

    @Mock
    private RewardService rewardService;

    @InjectMocks
    private RewardController rewardController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        mockMvc = MockMvcBuilders.standaloneSetup(rewardController).build(); // Setup MockMvc
    }

    // Test for GET /api/rewards/{customerId}
    @Test
    void testGetRewards() throws Exception {
        // Create mock data
        Map<String, Integer> pointsPerMonth = new HashMap<>();
        pointsPerMonth.put("2025-01", 90);
        pointsPerMonth.put("2025-02", 30);

        CustomerDto customerDto = new CustomerDto("cust001", pointsPerMonth, 120);

        // Mock the RewardService method
        when(rewardService.getCustomerRewards("cust001")).thenReturn(customerDto);

        mockMvc.perform(get("/retail/customer/cust001"))
                .andExpect(status().isOk()) // Expect HTTP 200 OK
                .andExpect(jsonPath("$.customerId").value("cust001"))
                .andExpect(jsonPath("$.pointsPerMonth['2025-02']").value(30))
                .andExpect(jsonPath("$.totalPoints").value(120)); // Validate response content
    }

    // Test for GET /api/rewards/{customerId} when no rewards exist
    @Test
    void testGetRewardsNotFound() throws Exception {
        // Mock the RewardService method to return null or an empty CustomerDto
        when(rewardService.getCustomerRewards("cust001")).thenReturn(null); // or return new CustomerDto() if you want to simulate empty

        // Perform the GET request and check the status and error message
        mockMvc.perform(get("/retail/customer/cust001"))
                .andExpect(status().isNotFound()) // Expect HTTP 404 Not Found
                .andExpect(jsonPath("$.error").value("No rewards found for this customer")); // Validate error message
    }

}