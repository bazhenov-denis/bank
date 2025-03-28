package org.example.bank.service;

import org.example.bank.domain.BankAccount;
import org.example.bank.domain.Category;
import org.example.bank.domain.Operation;
import org.example.bank.domain.OperationType;
import org.example.bank.repository.BankAccountRepository;
import org.example.bank.repository.OperationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OperationServiceTest {

    private OperationRepository operationRepository;
    private BankAccountRepository bankAccountRepository;
    private OperationService operationService;

    @BeforeEach
    void setUp() {
        operationRepository = mock(OperationRepository.class);
        bankAccountRepository = mock(BankAccountRepository.class);
        operationService = new OperationService(operationRepository, bankAccountRepository);
    }

    @Test
    void createIncomeOperation_shouldIncreaseBalance() {
        BankAccount account = new BankAccount();
        account.setId(1L);
        account.setBalance(BigDecimal.valueOf(100));

        Operation operation = new Operation();
        operation.setType(OperationType.INCOME);
        operation.setAmount(BigDecimal.valueOf(50));
        operation.setBankAccount(account);

        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(operationRepository.save(any(Operation.class))).thenAnswer(i -> i.getArgument(0));

        Operation result = operationService.create(operation);

        assertEquals(BigDecimal.valueOf(150), result.getBankAccount().getBalance());
        verify(operationRepository).save(operation);
    }

    @Test
    void createExpenseOperation_shouldDecreaseBalance() {
        BankAccount account = new BankAccount();
        account.setId(2L);
        account.setBalance(BigDecimal.valueOf(200));

        Operation operation = new Operation();
        operation.setType(OperationType.EXPENSE);
        operation.setAmount(BigDecimal.valueOf(30));
        operation.setBankAccount(account);

        when(bankAccountRepository.findById(2L)).thenReturn(Optional.of(account));
        when(operationRepository.save(any(Operation.class))).thenAnswer(i -> i.getArgument(0));

        Operation result = operationService.create(operation);

        assertEquals(BigDecimal.valueOf(170), result.getBankAccount().getBalance());
    }

    @Test
    void transfer_shouldSucceedIfEnoughFunds() {
        BankAccount from = new BankAccount();
        from.setBalance(BigDecimal.valueOf(100));

        BankAccount to = new BankAccount();
        to.setBalance(BigDecimal.valueOf(50));

        boolean success = operationService.transfer(from, to, 40);

        assertTrue(success);
        assertEquals(BigDecimal.valueOf(60.0), from.getBalance());
        assertEquals(BigDecimal.valueOf(90.0), to.getBalance());

        verify(bankAccountRepository).save(from);
        verify(bankAccountRepository).save(to);
    }

    @Test
    void transfer_shouldFailIfInsufficientFunds() {
        BankAccount from = new BankAccount();
        from.setBalance(BigDecimal.valueOf(20));

        BankAccount to = new BankAccount();
        to.setBalance(BigDecimal.valueOf(50));

        boolean success = operationService.transfer(from, to, 30);

        assertFalse(success);
        assertEquals(BigDecimal.valueOf(20), from.getBalance());
        assertEquals(BigDecimal.valueOf(50), to.getBalance());

        verify(bankAccountRepository, never()).save(any());
    }

    @Test
    void calculateIncomeExpenseDifference_shouldReturnCorrectValue() {
        Operation income1 = new Operation();
        income1.setType(OperationType.INCOME);
        income1.setAmount(BigDecimal.valueOf(200));
        income1.setDate(LocalDate.of(2024, 1, 10));

        Operation expense1 = new Operation();
        expense1.setType(OperationType.EXPENSE);
        expense1.setAmount(BigDecimal.valueOf(50));
        expense1.setDate(LocalDate.of(2024, 1, 12));

        when(operationRepository.findAll()).thenReturn(List.of(income1, expense1));

        BigDecimal result = operationService.calculateIncomeExpenseDifference(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 31)
        );

        assertEquals(BigDecimal.valueOf(150), result);
    }

    @Test
    void groupByCategory_shouldGroupOperationsCorrectly() {
        Category food = new Category();
        food.setName("Food");

        Category transport = new Category();
        transport.setName("Transport");

        Operation op1 = new Operation();
        op1.setCategory(food);

        Operation op2 = new Operation();
        op2.setCategory(food);

        Operation op3 = new Operation();
        op3.setCategory(transport);

        when(operationRepository.findAll()).thenReturn(List.of(op1, op2, op3));

        Map<Category, List<Operation>> grouped = operationService.groupByCategory();

        assertEquals(2, grouped.size());
        assertEquals(2, grouped.get(food).size());
        assertEquals(1, grouped.get(transport).size());
    }
}
