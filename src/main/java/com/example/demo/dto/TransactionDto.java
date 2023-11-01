package com.example.demo.dto;

import com.example.demo.entity.TransactionType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    @NotNull
    private Long userId;
    @NotEmpty
    private String cryptoPair;
    @NotNull
    private double quantity;
    @NotNull
    private TransactionType transactionType;
}
