
package com.example.personal_item_lending.controller;

import com.example.personal_item_lending.entity.TransactionRecord;
import com.example.personal_item_lending.repository.TransactionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "https://personal-item-lending-frontend.onrender.com"
})
public class TransactionController {

    private final TransactionRepository transactionRepository;

    public TransactionController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @GetMapping
    public List<TransactionRecord> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @PostMapping
    public TransactionRecord createTransaction(
            @RequestBody TransactionRecord transaction) {
        return transactionRepository.save(transaction);
    }

    @GetMapping("/{id}")
    public TransactionRecord getTransactionById(@PathVariable Integer id) {
        return transactionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Transaction not found: " + id));
    }

    @PutMapping("/{id}")
    public TransactionRecord updateTransaction(
            @PathVariable Integer id,
            @RequestBody TransactionRecord updated) {

        TransactionRecord existing = transactionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Transaction not found: " + id));

        existing.setItemId(updated.getItemId());
        existing.setPersonName(updated.getPersonName());
        existing.setTransactionType(updated.getTransactionType());
        existing.setTransactionDate(updated.getTransactionDate());
        existing.setDueDate(updated.getDueDate());
        existing.setStatus(updated.getStatus());
        existing.setNotes(updated.getNotes());

        return transactionRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable Integer id) {
        transactionRepository.deleteById(id);
    }
}
