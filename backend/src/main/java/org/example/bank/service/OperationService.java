package org.example.bank.service;

import org.example.bank.domain.BankAccount;
import org.example.bank.domain.Category;
import org.example.bank.domain.Operation;
import org.example.bank.domain.OperationType;
import org.example.bank.repository.BankAccountRepository;
import org.example.bank.repository.OperationRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OperationService {
    private final OperationRepository operationRepository;
    private final BankAccountRepository bankAccountRepository;

    public OperationService(OperationRepository operationRepository, BankAccountRepository bankAccountRepository) {
        this.operationRepository = operationRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    public Operation create(Operation operation) {
        Long accountId = operation.getBankAccount().getId();
        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        BigDecimal amount = operation.getAmount();

        if (operation.getType() == OperationType.EXPENSE) {
            if (account.getBalance().compareTo(amount) < 0) {
                throw new RuntimeException("Not enough balance for expense operation");
            }
            account.setBalance(account.getBalance().subtract(amount));
        } else if (operation.getType() == OperationType.INCOME) {
            account.setBalance(account.getBalance().add(amount));
        }

        operation.setBankAccount(account);

        bankAccountRepository.save(account);
        return operationRepository.save(operation);
    }

    public Operation update(Operation operation) {
        return operationRepository.save(operation);
    }

    public void delete(Long id) {
        operationRepository.deleteById(id);
    }

    public List<Operation> getAll() {
        return operationRepository.findAll();
    }

    public Operation getById(Long id) {
        return operationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Operation not found with id: " + id));
    }

    public List<Operation> getByUserId(Long userId) {
        return operationRepository.findByUserId(userId);
    }

    public BigDecimal calculateIncomeExpenseDifference(LocalDate start, LocalDate end) {
        List<Operation> operations = operationRepository.findAll()
                .stream()
                .filter(op -> !op.getDate().isBefore(start) && !op.getDate().isAfter(end))
                .collect(Collectors.toList());

        BigDecimal income = operations.stream()
                .filter(op -> op.getType() == OperationType.INCOME)
                .map(Operation::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expense = operations.stream()
                .filter(op -> op.getType() == OperationType.EXPENSE)
                .map(Operation::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return income.subtract(expense);
    }

    public Map<Category, List<Operation>> groupByCategory() {
        return operationRepository.findAll().stream()
                .collect(Collectors.groupingBy(Operation::getCategory));
    }

    public boolean transfer(BankAccount from, BankAccount to, double amount) {
        if (amount <= 0) return false;

        BigDecimal transferAmount = BigDecimal.valueOf(amount);

        if (from.getBalance().compareTo(transferAmount) < 0) {
            return false;
        }

        from.setBalance(from.getBalance().subtract(transferAmount));
        to.setBalance(to.getBalance().add(transferAmount));

        bankAccountRepository.save(from);
        bankAccountRepository.save(to);

        return true;
    }

    public boolean transferFunds(Long fromAccountId, Long toAccountId, double amount) {
        Optional<BankAccount> from = bankAccountRepository.findById(fromAccountId);
        Optional<BankAccount> to = bankAccountRepository.findById(toAccountId);

        if (from.isEmpty() || to.isEmpty()) {
            return false;
        }

        return transfer(from.get(), to.get(), amount);
    }

    public Operation getLastOperation() {
        return operationRepository.findFirstByOrderByDateDesc();
    }

}
