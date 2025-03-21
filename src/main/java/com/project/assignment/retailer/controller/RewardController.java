package com.project.assignment.retailer.controller;

import com.project.assignment.retailer.dto.CustomerDto;
import com.project.assignment.retailer.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * Endpoint to retrieve the reward points of a specific customer.
     *
     * @param customerId The unique identifier for the customer whose rewards are being retrieved.
     * @return A ResponseEntity containing the list of rewards associated with the customer.
     *         If no rewards are found, it returns a 404 NOT_FOUND status with an error message.
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getCustomerRewards(@PathVariable String customerId) {
        CustomerDto customerData = rewardService.getCustomerRewards(customerId);

        if (customerData == null || customerData.getPointsPerMonth().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"No rewards found for this customer\"}");
        }
        return ResponseEntity.ok(customerData);
    }
}
