package com.bankapp.accountsmicro.entity;

import lombok.*;
import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer accountId;

    @Column(nullable = false)
    private Integer userId;

    @Column(nullable = false)
    private Integer accountNumber;

    @Column(nullable = false)
    private Long routingNumber;

    @Column(nullable = false)
    private Character accountType;

    @Column(nullable = false)
    private Double balance;

    @Column(nullable = false)
    private Character accountStatus;
}