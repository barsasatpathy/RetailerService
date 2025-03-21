package com.project.assignment.retailer.controller;

import com.project.assignment.retailer.dto.CustomerDto;
import com.project.assignment.retailer.service.RewardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RewardControllerTest {

    @Mock
    private RewardService rewardService;

    @InjectMocks
    private RewardController rewardController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCustomer_ValidBehaviour() throws Exception {
        // Create sample CustomerDto (representing the customer and their rewards data)
        Map<String, Integer> pointsPerMonth = new HashMap<>();
        pointsPerMonth.put("2025-01", 90);
        pointsPerMonth.put("2025-02", 30);

        CustomerDto customerDto = new CustomerDto("cust001", pointsPerMonth, 120);

        // Assume that RewardService is able to return the CustomerDto when fetching rewards for a given customerId
        when(rewardService.getCustomerRewards("cust001")).thenReturn(customerDto);

        // Make a request to the endpoint
        ResponseEntity<CustomerDto> response = (ResponseEntity<CustomerDto>) rewardController.getCustomerRewards("cust001");

        // Validate the response body (CustomerDto)
        assertNotNull(response.getBody());
        assertEquals("cust001", response.getBody().getCustomerId());
        assertEquals(120, response.getBody().getTotalPoints());
        assertTrue(response.getBody().getPointsPerMonth().containsKey("2025-01"));
        assertTrue(response.getBody().getPointsPerMonth().containsKey("2025-02"));

        // Verify that the service was called once with the correct customerId
        verify(rewardService, times(1)).getCustomerRewards("cust001");
    }

    @Test
    public void testGetCustomer_ErrorBehaviour() throws Exception {
        // Simulate an error scenario, where the RewardService throws an exception
        when(rewardService.getCustomerRewards("cust001")).thenThrow(new RuntimeException("No rewards found for this customer"));

        // Perform the request and assert that the exception is thrown
        Exception exception = assertThrows(RuntimeException.class, () -> {
            rewardController.getCustomerRewards("cust001");
        });

        // Verify the exception message
        assertEquals("No rewards found for this customer", exception.getMessage());

        // Verify that the service method was called exactly once
        verify(rewardService, times(1)).getCustomerRewards("cust001");
    }
}