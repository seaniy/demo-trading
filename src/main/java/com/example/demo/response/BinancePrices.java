package com.example.demo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BinancePrices {
    private String symbol;
    private double bidPrice;
    private double bidQty;
    private double askPrice;
    private double askQty;
}