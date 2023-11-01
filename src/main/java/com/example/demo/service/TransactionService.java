package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.exception.InsufficientBalanceException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CryptoPriceService cryptoPriceService;

    public void executeTransaction(Long userId, String cryptoPair, double quantity, TransactionType transactionType) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        Wallet wallet = user.getWallet();
        CryptoPrice cryptoPrice = cryptoPriceService.getLatestPrice(cryptoPair);
        double price = transactionType.isBuy() ? cryptoPrice.getAskPrice() : cryptoPrice.getBidPrice();
        double totalCost = price * quantity;
        if (transactionType.isBuy()) {
            handleBuy(cryptoPair, quantity, wallet, totalCost);
        } else {
            handleSell(cryptoPair, quantity, wallet, totalCost);
        }
        walletRepository.save(wallet);

        // Save transaction
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setCryptoPair(cryptoPair);
        transaction.setQuantity(quantity);
        transaction.setPrice(price);
        transaction.setTransactionType(transactionType);
        transaction.setTransactionTime(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    private static void handleSell(String cryptoPair, double quantity, Wallet wallet, double totalCost) {
        if ("ETHUSDT".equals(cryptoPair)) {
            if (wallet.getEthBalance() < quantity) {
                throw new InsufficientBalanceException("ETH");
            }
            wallet.setEthBalance(wallet.getEthBalance() - quantity);
            wallet.setUsdtBalance(wallet.getUsdtBalance() + totalCost);
        } else if ("BTCUSDT".equals(cryptoPair)) {
            if (wallet.getBtcBalance() < quantity) {
                throw new InsufficientBalanceException("BTC");
            }
            wallet.setBtcBalance(wallet.getBtcBalance() - quantity);
            wallet.setUsdtBalance(wallet.getUsdtBalance() + totalCost);
        }
    }

    private static void handleBuy(String crypto, double quantity, Wallet wallet, double totalCost) {
        if ("ETHUSDT".equals(crypto)) {
            if (wallet.getUsdtBalance() < totalCost) {
                throw new InsufficientBalanceException("USDT");
            }
            wallet.setUsdtBalance(wallet.getUsdtBalance() - totalCost);
            wallet.setEthBalance(wallet.getEthBalance() + quantity);
        } else if ("BTCUSDT".equals(crypto)) {
            if (wallet.getUsdtBalance() < totalCost) {
                throw new InsufficientBalanceException("USDT");
            }
            wallet.setUsdtBalance(wallet.getUsdtBalance() - totalCost);
            wallet.setBtcBalance(wallet.getBtcBalance() + quantity);
        }
    }

    public List<Transaction> getUserTransactionHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        return transactionRepository.findTransactionsByUser(user);
    }
}

