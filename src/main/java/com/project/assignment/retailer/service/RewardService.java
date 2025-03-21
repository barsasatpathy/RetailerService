package com.project.assignment.retailer.service;

import com.project.assignment.retailer.dto.CustomerDto;
import com.project.assignment.retailer.entity.Transaction;
import com.project.assignment.retailer.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Service class to handle reward points processing and management.
 * Provides methods to process transactions, calculate reward points,
 * and retrieve rewards for customers.
 */
@Service
public class RewardService {

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Retrieves the rewards for a specific customer by their customer ID.
     *
     * @param customerId The unique identifier of the customer whose rewards are being fetched.
     * @return A list of Reward objects associated with the given customer ID.
     */
    public CustomerDto getCustomerRewards(String customerId) {
        Date currentDate = new Date();
        // Use Calendar to subtract 3 months
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate); // Set the current date in the calendar
        calendar.add(Calendar.MONTH, -3); // Subtract 3 months

        // Get the new start date after subtracting 3 months
        Date startDate = calendar.getTime();
        List<Transaction> transactionList = transactionRepository.findByCustomerIdAndTransactionDateBetween(customerId, startDate, currentDate);

        // Create a map to store the points per month
        Map<String, Integer> pointsPerMonth = new HashMap<>();
        int totalPoints = 0;
        // Iterate over each transaction and calculate reward points
        for (Transaction transaction : transactionList) {
            // Calculate reward points for the transaction
            int points = calculateRewardPoints(transaction.getTransactionAmount());

            // Add the points to the corresponding month
            String monthYear = new SimpleDateFormat("yyyy-MM").format(transaction.getTransactionDate());
            pointsPerMonth.put(monthYear, pointsPerMonth.getOrDefault(monthYear, 0) + points);

            // Add to the total points
            totalPoints += points;
        }

        // Create CustomerDto with customerId, points per month, and total points
        CustomerDto customerDto = new CustomerDto();
        customerDto.setCustomerId(customerId);
        customerDto.setPointsPerMonth(pointsPerMonth);
        customerDto.setTotalPoints(totalPoints);

        return customerDto;
    }

    /**
     * Calculates the reward points for a transaction based on the transaction amount.
     *
     * Reward points are calculated as follows:
     * - 2 points for every dollar spent over $100.
     * - 1 point for every dollar spent between $50 and $100.
     *
     * @param transactionAmount The amount spent in the transaction.
     * @return The total reward points calculated based on the transaction amount.
     */
    int calculateRewardPoints(double transactionAmount) {
        int rewardPoints = 0;
        // If the transaction amount is greater than $100, calculate the reward for the amount over $100
        if (transactionAmount > 100) {
            rewardPoints += (transactionAmount - 100) * 2;
            transactionAmount = 100;
        }
        // If the transaction amount is greater than $50, calculate the reward for the amount between $50 and $100
        if (transactionAmount > 50) {
            rewardPoints += (transactionAmount - 50);
        }
        return rewardPoints;
    }
}
