package org.example.bank.facade;


import org.example.bank.domain.BankAccount;
import org.example.bank.domain.Category;
import org.example.bank.domain.Operation;
import org.example.bank.repository.BankAccountRepository;
import org.example.bank.service.FinancialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class FinancialFacade {

    private final FinancialService financialService;
    private final BankAccountRepository bankAccountRepository;


    public FinancialFacade(FinancialService financialService, BankAccountRepository bankAccountRepository) {
        this.financialService = financialService;
        this.bankAccountRepository = bankAccountRepository;

    }

    // Методы для работы с BankAccount
    public BankAccount createAccount(BankAccount bankAccount) {
        return financialService.createBankAccount(bankAccount);
    }

    public BankAccount updateAccount(BankAccount bankAccount) {
        return financialService.updateBankAccount(bankAccount);
    }

    public void deleteAccount(Long id) {
        financialService.deleteBankAccount(id);
    }

    public List<BankAccount> getAllAccounts() {
        return financialService.getAllAccounts();
    }

    public BankAccount getAccountById(Long id) {
        return financialService.getAccountById(id);
    }

    // Методы для работы с Category
    public Category createCategory(Category category) {
        return financialService.createCategory(category);
    }

    public Category updateCategory(Category category) {
        return financialService.updateCategory(category);
    }

    public void deleteCategory(Long id) {
        financialService.deleteCategory(id);
    }

    public List<Category> getAllCategories() {
        return financialService.getAllCategories();
    }

    public Category getCategoryById(Long id) {
        return financialService.getCategoryById(id);
    }
    // Методы для работы с Operation
    public Operation createOperation(Operation operation) {
        return financialService.createOperation(operation);
    }

    public Operation updateOperation(Operation operation) {
        return financialService.updateOperation(operation);
    }

    public void deleteOperation(Long id) {
        financialService.deleteOperation(id);
    }

    public List<Operation> getAllOperations() {
        return financialService.getAllOperations();
    }

    public Operation getOperationById(Long id) {
        return financialService.getOperationById(id);
    }

    // Аналитика
    public BigDecimal getIncomeExpenseDifference(LocalDate start, LocalDate end) {
        return financialService.calculateIncomeExpenseDifference(start, end);
    }

    public Map<Category, List<Operation>> getOperationsGroupedByCategory() {
        return financialService.groupOperationsByCategory();
    }

    public boolean transferFunds(Long fromAccountId, Long toAccountId, double amount) {
        Optional<BankAccount> fromAccountOpt = bankAccountRepository.findById(fromAccountId);
        Optional<BankAccount> toAccountOpt = bankAccountRepository.findById(toAccountId);

        if (fromAccountOpt.isEmpty() || toAccountOpt.isEmpty()) {
            return false;  // ❌ Один из счетов не найден
        }

        return financialService.transferFunds(fromAccountOpt.get(), toAccountOpt.get(), amount);
    }

    // В FinancialFacade.java
    public List<BankAccount> getAccountsByUserId(Long userId) {
        return bankAccountRepository.findByOwnerId(userId);
    }

}
