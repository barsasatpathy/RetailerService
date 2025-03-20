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

        mockMvc.perform(post("/retail/transaction")
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
        mockMvc.perform(post("/retail/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\": \"cust001\", \"transactionAmount\": 120, \"transactionDate\": \"2025-03-15\"}"))
                .andExpect(status().isInternalServerError()) // Expect HTTP 500 Internal Server Error
                .andExpect(jsonPath("$.error").value("Failed to process the transaction: Unable to process transaction")); // Validate error message
    }

    /**
     * Test for POST /retail/transactions with multiple customers and multiple transactions.
     */
    @Test
    void testAddMultipleTransactions() throws Exception {
        // Prepare mock data for multiple transactions
        Transaction transaction1 = new Transaction("cust001", 120.0, new java.util.Date());
        Transaction transaction2 = new Transaction("cust001", 80.0, new java.util.Date());
        Transaction transaction3 = new Transaction("cust002", 150.0, new java.util.Date());
        Transaction transaction4 = new Transaction("cust002", 60.0, new java.util.Date());

        List<Transaction> transactions = Arrays.asList(transaction1, transaction2, transaction3, transaction4);

        // Mock the RewardService method to return a list of rewards
        when(rewardService.processTransaction(any(Transaction.class))).thenReturn(new Reward());

        // Perform the POST request with a list of transactions
        mockMvc.perform(post("/retail/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("["
                                + "{\"customerId\": \"cust001\", \"transactionAmount\": 120, \"transactionDate\": \"2025-03-15\"},"
                                + "{\"customerId\": \"cust001\", \"transactionAmount\": 80, \"transactionDate\": \"2025-03-16\"},"
                                + "{\"customerId\": \"cust002\", \"transactionAmount\": 150, \"transactionDate\": \"2025-03-17\"},"
                                + "{\"customerId\": \"cust002\", \"transactionAmount\": 60, \"transactionDate\": \"2025-03-18\"}"
                                + "]"))
                .andExpect(status().isCreated());
    }

    /**
     * Test for POST /retail/transactions with multiple transactions where one transaction causes an error.
     */
    @Test
    void testAddMultipleTransactionsWithError() throws Exception {
        // Prepare mock data for multiple transactions
        Transaction transaction1 = new Transaction("cust001", 120.0, new java.util.Date());
        Transaction transaction2 = new Transaction("cust002", 150.0, new java.util.Date());

        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

        // Mock the RewardService method to throw an exception on processing the second transaction
        doThrow(new RuntimeException("Failed to process transaction"))
                .when(rewardService).processTransaction(any(Transaction.class));

        // Perform the POST request with the list of transactions
        mockMvc.perform(post("/retail/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("["
                                + "{\"customerId\": \"cust001\", \"transactionAmount\": 120, \"transactionDate\": \"2025-03-15\"},"
                                + "{\"customerId\": \"cust002\", \"transactionAmount\": 150, \"transactionDate\": \"2025-03-17\"}"
                                + "]"))
                .andExpect(status().isInternalServerError()) // Expect HTTP 500 Internal Server Error
                .andExpect(jsonPath("$.error").value("Failed to process the transactions: Failed to process transaction")); // Validate error message
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