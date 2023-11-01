package com.example.demo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HuobiPrices {
    private String symbol;
    private double bid;
    private double bidSize;
    private double ask;
    private double askSize;
}

