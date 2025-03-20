package com.project.assignment.retailer.controller;

import com.project.assignment.retailer.entity.Reward;
import com.project.assignment.retailer.entity.Transaction;
import com.project.assignment.retailer.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class to handle the requests related to customer rewards and transactions.
 * Provides endpoints for recording transactions, calculating rewards, and retrieving rewards information.
 */
@RestController
@RequestMapping("/retail")
public class RewardController {

    @Autowired
    private RewardService rewardService;

    /**
     * Endpoint to record a transaction and calculate the associated reward points.
     *
     * @param transaction The transaction object containing the details of the transaction made by the customer.
     * @return A ResponseEntity indicating the outcome of the transaction processing.
     *         If successful, it returns a 201 CREATED status, otherwise, returns a 500 INTERNAL_SERVER_ERROR status.
     */
    @PostMapping("/transactions")
    public ResponseEntity<?> addTransaction(@RequestBody Transaction transaction) {
        try {
            // Process the transaction and calculate reward points
            Reward reward = rewardService.processTransaction(transaction);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RuntimeException e) {
            // If an error occurs during processing, return a 500 status with the error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Failed to process the transaction: " + e.getMessage() + "\"}");
        }
    }

    /**
     * Endpoint to retrieve the reward points of a specific customer.
     *
     * @param customerId The unique identifier for the customer whose rewards are being retrieved.
     * @return A ResponseEntity containing the list of rewards associated with the customer.
     *         If no rewards are found, it returns a 404 NOT_FOUND status with an error message.
     */
    @GetMapping("/rewards/{customerId}")
    public ResponseEntity<?> getRewards(@PathVariable String customerId) {
        // Retrieve the list of rewards for the specified customer
        List<Reward> rewards = rewardService.getRewards(customerId);

        if (rewards.isEmpty()) {
            // Return a 404 status if no rewards are found
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"No rewards found for this customer\"}");
        }

        // Return the rewards as a 200 OK response
        return ResponseEntity.ok(rewards);
    }

    /**
     * Endpoint to retrieve all rewards across all customers.
     *
     * @return A ResponseEntity containing a list of all rewards.
     *         If no rewards are found, it returns a 404 NOT_FOUND status with an error message.
     */
    @GetMapping("/rewards/all")
    public ResponseEntity<?> getAllRewards() {
        // Retrieve all rewards from the service
        List<Reward> rewards = rewardService.getAllRewards();

        if (rewards.isEmpty()) {
            // Return a 404 status if no rewards are found
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"No rewards found\"}");
        }

        // Return all rewards as a 200 OK response
        return ResponseEntity.ok(rewards);
    }

    /**
     * Endpoint to retrieve the total reward points of a specific customer.
     *
     * @param customerId The unique identifier for the customer whose total rewards are being retrieved.
     * @return A ResponseEntity containing the list of rewards associated with the customer.
     *         If no rewards are found, it returns a 404 NOT_FOUND status with an error message.
     */
    @GetMapping("/rewards/{customerId}")
    public ResponseEntity<?> getTotalRewards(@PathVariable String customerId) {
        // Retrieve the list of rewards for the specified customer
        List<Reward> rewards = rewardService.getRewards(customerId);

        if (rewards.isEmpty()) {
            // Return a 404 status if no rewards are found
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"No rewards found for this customer\"}");
        }

        // Return the rewards as a 200 OK response
        return ResponseEntity.ok(rewards);
    }
}
