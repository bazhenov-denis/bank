package org.example.bank.facade;

import org.example.bank.domain.BankAccount;
import org.example.bank.domain.Category;
import org.example.bank.domain.Operation;
import org.example.bank.service.BankAccountService;
import org.example.bank.service.CategoryService;
import org.example.bank.service.OperationService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class FinancialFacade {

    private final BankAccountService bankAccountService;
    private final CategoryService categoryService;
    private final OperationService operationService;

    public FinancialFacade(BankAccountService bankAccountService,
                           CategoryService categoryService,
                           OperationService operationService) {
        this.bankAccountService = bankAccountService;
        this.categoryService = categoryService;
        this.operationService = operationService;

    }

    // Методы для BankAccount
    public BankAccount createAccount(BankAccount account) {
        return bankAccountService.create(account);
    }

    public BankAccount updateAccount(BankAccount account) {
        return bankAccountService.update(account);
    }

    public void deleteAccount(Long id) {
        bankAccountService.delete(id);
    }

    public List<BankAccount> getAllAccounts() {
        return bankAccountService.getAll();
    }

    public BankAccount getAccountById(Long id) {
        return bankAccountService.getById(id);
    }

    public List<BankAccount> getAccountsByUserId(Long userId) {
        return bankAccountService.getByOwnerId(userId);
    }


    // Методы для Category
    public Category createCategory(Category category) {
        return categoryService.create(category);
    }

    public Category updateCategory(Category category) {
        return categoryService.update(category);
    }

    public void deleteCategory(Long id) {
        categoryService.delete(id);
    }

    public List<Category> getAllCategories() {
        return categoryService.getAll();
    }

    public Category getCategoryById(Long id) {
        return categoryService.getById(id);
    }

    // Методы для Operation
    public Operation createOperation(Operation operation) {
        return operationService.create(operation);
    }

    public Operation updateOperation(Operation operation) {
        return operationService.update(operation);
    }

    public void deleteOperation(Long id) {
        operationService.delete(id);
    }

    public List<Operation> getAllOperations() {
        return operationService.getAll();
    }

    public Operation getOperationById(Long id) {
        return operationService.getById(id);
    }

    public List<Operation> getOperationsByUserId(Long userId) {
        return operationService.getByUserId(userId);
    }

    public Operation getLastOperation() {
        return operationService.getLastOperation();
    }

    // Аналитика
    public BigDecimal getIncomeExpenseDifference(LocalDate start, LocalDate end) {
        return operationService.calculateIncomeExpenseDifference(start, end);
    }

    public Map<Category, List<Operation>> getOperationsGroupedByCategory() {
        return operationService.groupByCategory();
    }

    public boolean transferFunds(Long fromAccountId, Long toAccountId, double amount) {
        return operationService.transferFunds(fromAccountId, toAccountId, amount);
    }

}
