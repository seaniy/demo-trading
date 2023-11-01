package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cryptoPair;
    private double quantity;
    private double price;
    private TransactionType transactionType;
    private LocalDateTime transactionTime;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
