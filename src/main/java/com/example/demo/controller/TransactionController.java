package com.example.demo.controller;

import com.example.demo.dto.TransactionDto;
import com.example.demo.entity.Transaction;
import com.example.demo.exception.InsufficientBalanceException;
import com.example.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trade")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<?> trade(@RequestBody TransactionDto transactionDto) {
        try {
            transactionService.executeTransaction(transactionDto.getUserId(),
                    transactionDto.getCryptoPair(),
                    transactionDto.getQuantity(),
                    transactionDto.getTransactionType());
            return ResponseEntity.ok("Trade successful");
        } catch (InsufficientBalanceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error occurred");
        }
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getTransactions(@RequestParam(required = true) Long userId) {
        List<Transaction> transactionList = transactionService.getUserTransactionHistory(userId);
        return ResponseEntity.ok(transactionList);
    }
}
