package com.example.demo.service;

import com.example.demo.entity.Wallet;
import com.example.demo.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    public Wallet getWallet(Long userId) {
        return walletRepository.findWalletByUserId(userId);
    }
}
