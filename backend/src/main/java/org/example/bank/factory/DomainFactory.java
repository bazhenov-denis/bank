package org.example.bank.factory;

import org.example.bank.domain.BankAccount;
import org.example.bank.domain.Category;
import org.example.bank.domain.CategoryType;
import org.example.bank.domain.Operation;
import org.example.bank.domain.OperationType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DomainFactory {
    public static BankAccount createBankAccount(String name, BigDecimal initialBalance) {
        // Здесь можно добавить валидацию и дополнительные бизнес-правила
        return new BankAccount(name, initialBalance);
    }

    public static Category createCategory(CategoryType type, String name) {
        // Валидация: например, проверка на null или пустое имя
        return new Category(type, name);
    }

    public static Operation createOperation(OperationType type, BankAccount bankAccount, BigDecimal amount, LocalDate date, String description, Category category) {
        // Пример валидации: сумма должна быть положительной
        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        return new Operation(type, bankAccount, amount, date, description, category);
    }
}
