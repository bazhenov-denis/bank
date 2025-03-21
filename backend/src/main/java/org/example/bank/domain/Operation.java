package org.example.bank.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

// Аннотация @Entity указывает, что класс Operation является сущностью и будет отображаться в таблице базы данных.
@Entity
public class Operation {

    // Поле id — это уникальный идентификатор операции, который является первичным ключом.
    // Аннотация @Id обозначает это, а @GeneratedValue с стратегией GenerationType.IDENTITY обеспечивает автоматическую генерацию значения.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Поле type хранит тип операции, представленный в виде перечисления.
    // Аннотация @Enumerated(EnumType.STRING) указывает, что значение будет сохранено в базе данных в виде строки,
    // что улучшает читаемость и упрощает отладку.
    @Enumerated(EnumType.STRING)
    private OperationType type;

    // Поле bankAccount связывает операцию с банковским счетом.
    // Аннотация @ManyToOne обозначает, что одна операция может относиться к одному банковскому счету,
    // а @JoinColumn(name = "bank_account_id") указывает имя столбца внешнего ключа в таблице операции.
    @ManyToOne
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;

    // Поле amount хранит сумму операции с использованием BigDecimal для точности при денежных расчетах.
    private BigDecimal amount;

    // Поле date хранит дату проведения операции.
    private LocalDate date;

    // Поле description предназначено для хранения описания операции.
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Поле category связывает операцию с категорией, к которой она относится.
    // Используется связь @ManyToOne, так как одна операция может относиться к одной категории.
    // Аннотация @JoinColumn(name = "category_id") указывает имя столбца внешнего ключа.
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // Конструктор по умолчанию обязателен для работы JPA.
    public Operation() {
    }

    // Конструктор с параметрами позволяет создать экземпляр Operation с заданными значениями всех полей.
    public Operation(OperationType type, BankAccount bankAccount, BigDecimal amount, LocalDate date, String description, Category category) {
        this.type = type;
        this.bankAccount = bankAccount;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.category = category;
    }

    // Геттеры и сеттеры для доступа к приватным полям класса.

    // Геттер для id.
    public Long getId() {
        return id;
    }

    // Геттер для типа операции.
    public OperationType getType() {
        return type;
    }

    // Геттер для банковского счета.
    public BankAccount getBankAccount() {
        return bankAccount;
    }

    // Геттер для суммы операции.
    public BigDecimal getAmount() {
        return amount;
    }

    // Геттер для даты операции.
    public LocalDate getDate() {
        return date;
    }

    // Геттер для описания операции.
    public String getDescription() {
        return description;
    }

    // Геттер для категории.
    public Category getCategory() {
        return category;
    }

    // Сеттер для id.
    public void setId(Long id) {
        this.id = id;
    }

    // Сеттер для типа операции.
    public void setType(OperationType type) {
        this.type = type;
    }

    // Сеттер для банковского счета.
    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    // Сеттер для суммы операции.
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    // Сеттер для даты операции.
    public void setDate(LocalDate date) {
        this.date = date;
    }

    // Сеттер для описания операции.
    public void setDescription(String description) {
        this.description = description;
    }

    // Сеттер для категории.
    public void setCategory(Category category) {
        this.category = category;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
