package com.example.personal_item_lending.repository;

import com.example.personal_item_lending.entity.TransactionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository
        extends JpaRepository<TransactionRecord, Integer> {
}