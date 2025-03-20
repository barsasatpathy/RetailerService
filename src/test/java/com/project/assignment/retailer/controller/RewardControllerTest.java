package com.project.assignment.retailer.controller;

import com.project.assignment.retailer.entity.Reward;
import com.project.assignment.retailer.entity.Transaction;
import com.project.assignment.retailer.service.RewardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.when;

public class RewardControllerTest {

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

    // Test for POST /api/transactions
    @Test
    void testAddTransaction() throws Exception {
        // Mock the RewardService method
        Transaction transaction = new Transaction("cust001", 120.0, new java.util.Date());
        // Use doNothing for void methods
        when(rewardService.processTransaction(transaction)).thenReturn(new Reward("cust001","2025-03", 90));
        // Simulate no return value

        mockMvc.perform(post("/retail/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\": \"cust001\", \"transactionAmount\": 120, \"transactionDate\": \"2025-03-15\"}"))
                .andExpect(status().isCreated()); // Expect HTTP 201 Created
    }

    // Test for POST /retail/transactions with exception
    @Test
    void testAddTransactionWithException() throws Exception {
        // Create a sample transaction
        Transaction transaction = new Transaction("cust001", 120.0, new java.util.Date());

        // Mock the RewardService method to throw an exception
        doThrow(new RuntimeException("Unable to process transaction"))
                .when(rewardService).processTransaction(any(Transaction.class));

        // Perform the request with the date in ISO 8601 format
        mockMvc.perform(post("/retail/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\": \"cust001\", \"transactionAmount\": 120, \"transactionDate\": \"2025-03-15\"}"))
                .andExpect(status().isInternalServerError()) // Expect HTTP 500 Internal Server Error
                .andExpect(jsonPath("$.error").value("Failed to process the transaction: Unable to process transaction")); // Validate error message
    }

    // Test for GET /api/rewards/{customerId}
    @Test
    void testGetRewards() throws Exception {
        // Create mock data
        Reward reward = new Reward("cust001", "2025-03", 90);
        List<Reward> rewards = Arrays.asList(reward);

        // Mock the RewardService method
        when(rewardService.getRewards("cust001")).thenReturn(rewards);

        mockMvc.perform(get("/retail/rewards/cust001"))
                .andExpect(status().isOk()) // Expect HTTP 200 OK
                .andExpect(jsonPath("$[0].customerId").value("cust001"))
                .andExpect(jsonPath("$[0].monthYear").value("2025-03"))
                .andExpect(jsonPath("$[0].rewardPoints").value(90)); // Validate response content
    }

    // Test for GET /api/rewards/{customerId} when no rewards exist
    @Test
    void testGetRewardsNotFound() throws Exception {
        // Mock the RewardService method to return an empty list
        when(rewardService.getRewards("cust001")).thenReturn(Arrays.asList());

        mockMvc.perform(get("/retail/rewards/cust001"))
                .andExpect(status().isNotFound()) // Expect HTTP 404 Not Found
                .andExpect(jsonPath("$.error").value("No rewards found for this customer")); // Validate error message
    }

}