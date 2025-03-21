package org.example.bank.service;


import com.sun.tools.javac.Main;
import lombok.extern.slf4j.Slf4j;
import org.example.bank.domain.BankAccount;
import org.example.bank.domain.Category;
import org.example.bank.domain.Operation;
import org.example.bank.domain.OperationType;
import org.example.bank.repository.BankAccountRepository;
import org.example.bank.repository.CategoryRepository;
import org.example.bank.repository.OperationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FinancialService {

    private final BankAccountRepository bankAccountRepository;
    private final CategoryRepository categoryRepository;
    private final OperationRepository operationRepository;

    public FinancialService(BankAccountRepository bankAccountRepository, CategoryRepository categoryRepository, OperationRepository operationRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.categoryRepository = categoryRepository;
        this.operationRepository = operationRepository;
    }

    // CRUD для BankAccount
    @Transactional
    public BankAccount createBankAccount(BankAccount bankAccount) {
        return bankAccountRepository.save(bankAccount);
    }

    @Transactional
    public BankAccount updateBankAccount(BankAccount bankAccount) {
        return bankAccountRepository.save(bankAccount);
    }

    @Transactional
    public void deleteBankAccount(Long id) {
        bankAccountRepository.deleteById(id);
    }

    public List<BankAccount> getAllAccounts() {
        List<BankAccount> accounts = bankAccountRepository.findAll();
        if (accounts.isEmpty()) {
            // Можно залогировать отсутствие данных
            System.out.println("Нет доступных счетов");}
        return accounts;
    }



    public BankAccount getAccountById(Long id) {
        return bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
    }

    // CRUD для Category
    @Transactional
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }
    private static final Logger logger = LoggerFactory.getLogger(FinancialService.class);


    // CRUD для Operation с обновлением баланса
    @Transactional
    public Operation createOperation(Operation operation) {
        // Получаем управляемый объект BankAccount
        Long accountId = operation.getBankAccount().getId();

        // Загружаем управляемый объект BankAccount из БД
        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Счет не найден"));

        // Вычисляем новый баланс
        BigDecimal updatedBalance = operation.getType().equals(OperationType.INCOME)
                ? account.getBalance().add(operation.getAmount())
                : account.getBalance().subtract(operation.getAmount());

        logger.info(account.getBalance().toString());
        // Обновляем баланс счета
        account.setBalance(updatedBalance);
        operation.setBankAccount(account);


        // Если account уже находится в контексте персистенции, его изменение будет автоматически сохранено при завершении транзакции.
        // Поэтому вызов bankAccountRepository.save(account) можно опустить.

        // Сохраняем операцию
        return operationRepository.save(operation);
    }

    @Transactional
    public Operation updateOperation(Operation operation) {
        return operationRepository.save(operation);
    }

    @Transactional
    public void deleteOperation(Long id) {
        operationRepository.deleteById(id);
    }

    public List<Operation> getAllOperations() {
        return operationRepository.findAll();
    }

    public Operation getOperationById(Long id) {
        return operationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Operation not found with id: " + id));
    }

    // Пример аналитики: разница доходов и расходов за выбранный период
    public BigDecimal calculateIncomeExpenseDifference(LocalDate start, LocalDate end) {
        List<Operation> operations = operationRepository.findAll()
                .stream()
                .filter(op -> !op.getDate().isBefore(start) && !op.getDate().isAfter(end))
                .collect(Collectors.toList());
        BigDecimal totalIncome = operations.stream()
                .filter(op -> op.getType().name().equals("INCOME"))
                .map(Operation::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalExpense = operations.stream()
                .filter(op -> op.getType().name().equals("EXPENSE"))
                .map(Operation::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalIncome.subtract(totalExpense);
    }

    // Пример аналитики: группировка операций по категориям
    public Map<Category, List<Operation>> groupOperationsByCategory() {
        List<Operation> operations = operationRepository.findAll();
        return operations.stream().collect(Collectors.groupingBy(Operation::getCategory));
    }

    @Transactional
    public boolean transferFunds(BankAccount fromAccount, BankAccount toAccount, double amount) {
        BigDecimal transferAmount = BigDecimal.valueOf(amount);

        // Проверяем, хватает ли средств
        if (fromAccount.getBalance().compareTo(transferAmount) < 0) {
            return false;  // ❌ Недостаточно средств
        }

        // Перевод
        fromAccount.setBalance(fromAccount.getBalance().subtract(transferAmount));
        toAccount.setBalance(toAccount.getBalance().add(transferAmount));

        // Сохранение обновленных данных
        bankAccountRepository.save(fromAccount);
        bankAccountRepository.save(toAccount);

        return true;  // ✅ Успех
    }

}
