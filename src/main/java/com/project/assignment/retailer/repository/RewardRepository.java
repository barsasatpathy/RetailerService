package com.project.assignment.retailer.repository;

import com.project.assignment.retailer.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for interacting with the Reward data in the database.
 * Provides methods for accessing, storing, and querying reward records.
 *
 * Extends JpaRepository to leverage Spring Data JPA's automatic implementation
 * of common database operations for the Reward entity.
 */
public interface RewardRepository extends JpaRepository<Reward, Long> {

    /**
     * Retrieves a list of rewards associated with a specific customer.
     *
     * @param customerId The unique identifier for the customer whose rewards are being retrieved.
     * @return A list of Reward objects associated with the given customer ID.
     */
    List<Reward> findByCustomerId(String customerId);
}
