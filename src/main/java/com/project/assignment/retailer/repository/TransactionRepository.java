package com.project.assignment.retailer.repository;

import com.project.assignment.retailer.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCustomerIdAndTransactionDateBetween(String customerId, Date transactionDate, Date transactionDate2);
}
