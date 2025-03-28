package org.example.bank;

import org.example.bank.domain.BankAccount;
import org.example.bank.domain.Category;
import org.example.bank.domain.Operation;
import org.example.bank.facade.FinancialFacade;
import org.example.bank.service.BankAccountService;
import org.example.bank.service.CategoryService;
import org.example.bank.service.OperationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FinancialFacadeTest {

    private BankAccountService bankAccountService;
    private CategoryService categoryService;
    private OperationService operationService;
    private FinancialFacade financialFacade;

    @BeforeEach
    void setUp() {
        bankAccountService = mock(BankAccountService.class);
        categoryService = mock(CategoryService.class);
        operationService = mock(OperationService.class);
        financialFacade = new FinancialFacade(bankAccountService, categoryService, operationService);
    }

    @Test
    void createAccount_shouldCallBankAccountService() {
        BankAccount acc = new BankAccount();
        when(bankAccountService.create(acc)).thenReturn(acc);
        assertEquals(acc, financialFacade.createAccount(acc));
        verify(bankAccountService).create(acc);
    }

    @Test
    void getAccountById_shouldReturnAccount() {
        BankAccount acc = new BankAccount();
        when(bankAccountService.getById(1L)).thenReturn(acc);
        assertEquals(acc, financialFacade.getAccountById(1L));
    }

    @Test
    void getAccountsByUserId_shouldReturnList() {
        List<BankAccount> accounts = List.of(new BankAccount());
        when(bankAccountService.getByOwnerId(1L)).thenReturn(accounts);
        assertEquals(accounts, financialFacade.getAccountsByUserId(1L));
    }

    @Test
    void createCategory_shouldDelegateToService() {
        Category cat = new Category();
        when(categoryService.create(cat)).thenReturn(cat);
        assertEquals(cat, financialFacade.createCategory(cat));
    }

    @Test
    void getCategoryById_shouldReturnCategory() {
        Category cat = new Category();
        when(categoryService.getById(1L)).thenReturn(cat);
        assertEquals(cat, financialFacade.getCategoryById(1L));
    }

    @Test
    void createOperation_shouldCallOperationService() {
        Operation op = new Operation();
        when(operationService.create(op)).thenReturn(op);
        assertEquals(op, financialFacade.createOperation(op));
    }

    @Test
    void getOperationById_shouldReturnOperation() {
        Operation op = new Operation();
        when(operationService.getById(1L)).thenReturn(op);
        assertEquals(op, financialFacade.getOperationById(1L));
    }

    @Test
    void getOperationsByUserId_shouldReturnList() {
        List<Operation> operations = List.of(new Operation());
        when(operationService.getByUserId(1L)).thenReturn(operations);
        assertEquals(operations, financialFacade.getOperationsByUserId(1L));
    }

    @Test
    void getIncomeExpenseDifference_shouldReturnValue() {
        LocalDate start = LocalDate.now().minusDays(5);
        LocalDate end = LocalDate.now();
        BigDecimal expected = BigDecimal.valueOf(500);
        when(operationService.calculateIncomeExpenseDifference(start, end)).thenReturn(expected);
        assertEquals(expected, financialFacade.getIncomeExpenseDifference(start, end));
    }

    @Test
    void transferFunds_shouldReturnTrueIfSuccess() {
        when(operationService.transferFunds(1L, 2L, 100.0)).thenReturn(true);
        assertTrue(financialFacade.transferFunds(1L, 2L, 100.0));
    }
}
