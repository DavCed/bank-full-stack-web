package com.bankapp.accountsmicro.entity;

import lombok.*;
import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer accountId;

    @Column(nullable = false, name = "user_id")
    private Integer userId;

    @Column(nullable = false, name = "balance")
    private Double balance;

    @Column(nullable = false, name = "account_number")
    private Integer accountNumber;

    @Column(nullable = false, name = "routing_number")
    private Long routingNumber;

    @Column(nullable = false, name = "account_type")
    private Character accountType;
}