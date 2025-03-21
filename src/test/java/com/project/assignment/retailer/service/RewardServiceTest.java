package com.project.assignment.retailer.service;

import com.project.assignment.retailer.dto.CustomerDto;
import com.project.assignment.retailer.entity.Transaction;
import com.project.assignment.retailer.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RewardServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private RewardService rewardService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testGetCustomerRewards_Valid() {
        // Prepare mock data for transactions
        String customerId = "cust001";

        // Create mock transactions
        Transaction transaction1 = new Transaction();
        transaction1.setCustomerId(customerId);
        transaction1.setTransactionAmount(120d);
        transaction1.setTransactionDate(new GregorianCalendar(2025, Calendar.JANUARY, 15).getTime()); // 2025-01-15

        Transaction transaction2 = new Transaction();
        transaction2.setCustomerId(customerId);
        transaction2.setTransactionAmount(80d);
        transaction2.setTransactionDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 15).getTime()); // 2025-02-15

        // Mock the repository call to return the transactions
        List<Transaction> transactionList = Arrays.asList(transaction1, transaction2);
        when(transactionRepository.findByCustomerIdAndTransactionDateBetween(
                eq(customerId),  // Match the customerId exactly
                any(Date.class), // Match the start date
                any(Date.class)  // Match the end date
        )).thenReturn(transactionList);

        // Call the method to test
        CustomerDto result = rewardService.getCustomerRewards(customerId);

        // Assertions
        assertNotNull(result);
        assertEquals(customerId, result.getCustomerId());
        assertEquals(2, result.getPointsPerMonth().size());  // Two different months
        assertEquals(90, result.getPointsPerMonth().get("2025-01"));
        assertEquals(30, result.getPointsPerMonth().get("2025-02"));
        assertEquals(120, result.getTotalPoints());
    }

    @Test
    public void testGetCustomerRewards_NoTransactions() {
        String customerId = "cust002";

        // Mock the repository call to return an empty list
        when(transactionRepository.findByCustomerIdAndTransactionDateBetween(eq(customerId), any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());

        // Call the method to test
        CustomerDto result = rewardService.getCustomerRewards(customerId);

        // Assertions
        assertNotNull(result);
        assertEquals(customerId, result.getCustomerId());
        assertTrue(result.getPointsPerMonth().isEmpty());
        assertEquals(0, result.getTotalPoints());
    }

    @Test
    void testCalculateRewardPoints_Above100() {
        // Given
        double amount = 120.0;

        // When
        int rewardPoints = rewardService.calculateRewardPoints(amount);

        // Then
        assertEquals(90, rewardPoints);
    }

    @Test
    void testCalculateRewardPoints_Between50and100() {
        // Given
        double amount = 80.0;

        // When
        int rewardPoints = rewardService.calculateRewardPoints(amount);

        // Then
        assertEquals(30, rewardPoints);
    }

    @Test
    void testCalculateRewardPoints_LessThan50() {
        // Given
        double amount = 40.0;

        // When
        int rewardPoints = rewardService.calculateRewardPoints(amount);

        // Then
        assertEquals(0, rewardPoints);
    }
}