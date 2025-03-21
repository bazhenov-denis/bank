package org.example.bank.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.math.BigDecimal;

/**
 * Сущность BankAccount представляет банковский счет в системе.
 */
@Entity
@EntityScan
public class BankAccount {

    // Поле id является первичным ключом, автоматически генерируемым при сохранении в БД
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Поле name хранит название или имя владельца счета
    private String name;

    // Поле balance хранит баланс счета с использованием BigDecimal для точности денежных расчетов
    private BigDecimal balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User owner;

    /**
     * Конструктор с параметрами для создания банковского счета с указанным именем и балансом.
     */
    public BankAccount(String name, BigDecimal balance) {
        this.name = name;
        this.balance = balance;
    }

    /**
     * Конструктор по умолчанию, необходимый для работы JPA.
     */
    public BankAccount() {
    }

    // Геттер для поля id
    public Long getId() {
        return id;
    }

    // Сеттер для поля id
    public void setId(Long id) {
        this.id = id;
    }

    // Геттер для поля name
    public String getName() {
        return name;
    }

    // Сеттер для поля name
    public void setName(String name) {
        this.name = name;
    }

    // Геттер для поля balance
    public BigDecimal getBalance() {
        return balance;
    }

    // Сеттер для поля balance
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
