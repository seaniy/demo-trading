package com.example.demo.controller;

import com.example.demo.entity.CryptoPrice;
import com.example.demo.exception.CryptoPairNotFoundException;
import com.example.demo.service.CryptoPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/price")
public class CryptoPriceController {

    @Autowired
    private CryptoPriceService cryptoPriceService;

    @GetMapping
    public ResponseEntity<?> getLatestBestAggregatedPriceByCryptoPair(@RequestParam(required = true) String cryptoPair) {
        try {
            CryptoPrice price = cryptoPriceService.getLatestPrice(cryptoPair);
            return ResponseEntity.ok(price);
        } catch (CryptoPairNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
