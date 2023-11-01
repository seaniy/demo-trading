package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.entity.Wallet;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void createUser(String username) {
        User user = new User();
        user.setUsername(username);

        Wallet wallet = new Wallet();
        wallet.setUsdtBalance(50000.0);
        user.setWallet(wallet);
        wallet.setUser(user);

        userRepository.save(user);
    }
}
