
package com.example.personal_item_lending.controller;

import com.example.personal_item_lending.entity.Item;
import com.example.personal_item_lending.entity.TransactionRecord;
import com.example.personal_item_lending.entity.TransactionStatus;
import com.example.personal_item_lending.repository.ItemRepository;
import com.example.personal_item_lending.repository.TransactionRepository;
import com.example.personal_item_lending.repository.ReturnRepository;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:5173")
public class DashboardController {

    private final ItemRepository itemRepository;
    private final TransactionRepository transactionRepository;
    private final ReturnRepository returnRepository;

    public DashboardController(
            ItemRepository itemRepository,
            TransactionRepository transactionRepository,
            ReturnRepository returnRepository) {
        this.itemRepository = itemRepository;
        this.transactionRepository = transactionRepository;
        this.returnRepository = returnRepository;
    }

    @GetMapping
    public Map<String, Object> getDashboard() {

        List<TransactionRecord> transactions =
                transactionRepository.findAll();

        LocalDate today = LocalDate.now();

        long activeTransactions = transactions.stream()
                .filter(t -> t.getStatus() == TransactionStatus.ACTIVE)
                .count();

        long returnedTransactions = transactions.stream()
                .filter(t -> t.getStatus() == TransactionStatus.RETURNED)
                .count();

        long overdueTransactions = transactions.stream()
                .filter(t -> t.getStatus() != TransactionStatus.RETURNED)
                .filter(t -> t.getDueDate() != null)
                .filter(t -> t.getDueDate().isBefore(today))
                .count();

        Map<String, Object> dashboard = new LinkedHashMap<>();
        dashboard.put("totalItems", itemRepository.count());
        dashboard.put("totalTransactions", transactions.size());
        dashboard.put("activeTransactions", activeTransactions);
        dashboard.put("returnedTransactions", returnedTransactions);
        dashboard.put("overdueTransactions", overdueTransactions);
        dashboard.put("totalReturns", returnRepository.count());

        return dashboard;
    }
}
