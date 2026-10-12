
package com.example.personal_item_lending.controller;

import com.example.personal_item_lending.entity.ReturnRecord;
import com.example.personal_item_lending.entity.TransactionRecord;
import com.example.personal_item_lending.entity.TransactionStatus;
import com.example.personal_item_lending.repository.ReturnRepository;
import com.example.personal_item_lending.repository.TransactionRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/returns")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "https://personal-item-lending-frontend.onrender.com"
})
public class ReturnController {

    private final ReturnRepository returnRepository;
    private final TransactionRepository transactionRepository;

    public ReturnController(
            ReturnRepository returnRepository,
            TransactionRepository transactionRepository) {
        this.returnRepository = returnRepository;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping
    public List<ReturnRecord> getAllReturns() {
        return returnRepository.findAll();
    }

    @PostMapping
    @Transactional
    public ReturnRecord createReturn(@RequestBody ReturnRecord record) {

        TransactionRecord transaction = transactionRepository
                .findById(record.getTransactionId())
                .orElseThrow(() -> new RuntimeException(
                        "Transaction not found: " + record.getTransactionId()));

        ReturnRecord savedReturn = returnRepository.save(record);

        transaction.setStatus(TransactionStatus.RETURNED);
        transactionRepository.save(transaction);

        return savedReturn;
    }

    @GetMapping("/{id}")
    public ReturnRecord getReturnById(@PathVariable Long id) {
        return returnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Return record not found: " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteReturn(@PathVariable Long id) {
        returnRepository.deleteById(id);
    }
    @PostMapping("/sync-statuses")
@Transactional
public String syncReturnStatuses() {
    List<ReturnRecord> returns = returnRepository.findAll();

    int updated = 0;

    for (ReturnRecord record : returns) {
        TransactionRecord transaction = transactionRepository
                .findById(record.getTransactionId())
                .orElse(null);

        if (transaction != null
                && transaction.getStatus() != TransactionStatus.RETURNED) {
            transaction.setStatus(TransactionStatus.RETURNED);
            transactionRepository.save(transaction);
            updated++;
        }
    }

    return "Successfully synchronized " + updated
            + " transaction statuses.";
}
}
