package com.project.assignment.retailer.service;

import com.project.assignment.retailer.entity.Reward;
import com.project.assignment.retailer.entity.Transaction;
import com.project.assignment.retailer.repository.RewardRepository;
import com.project.assignment.retailer.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Service class to handle reward points processing and management.
 * Provides methods to process transactions, calculate reward points,
 * and retrieve rewards for customers.
 */
@Service
public class RewardService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RewardRepository rewardRepository;

    /**
     * Processes a transaction, calculates the reward points, and saves the reward to the database.
     * If a reward for the given customer and month already exists, it updates the reward points,
     * otherwise it creates a new reward entry.
     *
     * @param transaction The transaction object containing the transaction details.
     * @return The Reward object with the updated or newly created reward points for the customer.
     */
    public Reward processTransaction(Transaction transaction) {
        // Calculate reward points for the transaction amount
        int rewardPoints = calculateRewardPoints(transaction.getTransactionAmount());
        Reward savedReward = new Reward();

        // Format the month and year from the transaction date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String monthYear = sdf.format(transaction.getTransactionDate());

        // Check if a reward already exists for the given customer and month
        Reward existingReward = rewardRepository.findByCustomerId(transaction.getCustomerId())
                .stream()
                .filter(r -> r.getMonthYear().equals(monthYear))
                .findFirst()
                .orElse(null);

        // If an existing reward is found, update the reward points
        if (existingReward != null) {
            existingReward.setRewardPoints(existingReward.getRewardPoints() + rewardPoints);
            savedReward = rewardRepository.save(existingReward);
        } else {
            // If no reward is found, create a new reward
            Reward newReward = new Reward(transaction.getCustomerId(), monthYear, rewardPoints);
            savedReward = rewardRepository.save(newReward);
        }
        return savedReward;
    }

    /**
     * Retrieves the rewards for a specific customer by their customer ID.
     *
     * @param customerId The unique identifier of the customer whose rewards are being fetched.
     * @return A list of Reward objects associated with the given customer ID.
     */
    public List<Reward> getRewards(String customerId) {
        return rewardRepository.findByCustomerId(customerId);
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
    public int calculateRewardPoints(double transactionAmount) {
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

    /**
     * Retrieves all rewards from the repository.
     *
     * @return A list of all rewards stored in the repository.
     */
    public List<Reward> getAllRewards() {
        return rewardRepository.findAll();
    }

    /**
     * Retrieves the rewards for a specific customer by their customer ID.
     *
     * @param customerId The unique identifier of the customer whose rewards are being fetched.
     * @return A list of Reward objects associated with the given customer ID.
     */
    public List<Reward> getTotalRewards(String customerId) {
        return rewardRepository.findByCustomerId(customerId);
    }
}
