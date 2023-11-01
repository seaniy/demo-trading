package com.example.demo.controller;

import com.example.demo.entity.Wallet;
import com.example.demo.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @GetMapping
    public ResponseEntity<?> getWallet(@RequestParam(required = true) Long userId) {
        Wallet wallet = walletService.getWallet(userId);
        if (wallet == null) return ResponseEntity.badRequest().body("Wallet not found for user " + userId);
        return ResponseEntity.ok(wallet);
    }
}
