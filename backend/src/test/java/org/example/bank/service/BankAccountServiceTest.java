package org.example.bank.service;

import org.example.bank.domain.BankAccount;
import org.example.bank.repository.BankAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BankAccountServiceTest {

    private BankAccountRepository bankAccountRepository;
    private BankAccountService bankAccountService;

    @BeforeEach
    void setUp() {
        bankAccountRepository = mock(BankAccountRepository.class);
        bankAccountService = new BankAccountService(bankAccountRepository);
    }

    @Test
    void create_shouldSaveAccount() {
        BankAccount account = new BankAccount();
        when(bankAccountRepository.save(account)).thenReturn(account);

        BankAccount result = bankAccountService.create(account);

        assertEquals(account, result);
        verify(bankAccountRepository).save(account);
    }

    @Test
    void update_shouldSaveAccount() {
        BankAccount account = new BankAccount();
        account.setId(1L);
        when(bankAccountRepository.save(account)).thenReturn(account);

        BankAccount result = bankAccountService.update(account);

        assertEquals(account, result);
        verify(bankAccountRepository).save(account);
    }

    @Test
    void delete_shouldCallRepository() {
        bankAccountService.delete(1L);
        verify(bankAccountRepository).deleteById(1L);
    }

    @Test
    void getAll_shouldReturnAllAccounts() {
        List<BankAccount> accounts = List.of(new BankAccount(), new BankAccount());
        when(bankAccountRepository.findAll()).thenReturn(accounts);

        List<BankAccount> result = bankAccountService.getAll();

        assertEquals(2, result.size());
        verify(bankAccountRepository).findAll();
    }

    @Test
    void getById_shouldReturnAccount() {
        BankAccount account = new BankAccount();
        account.setId(1L);
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));

        BankAccount result = bankAccountService.getById(1L);

        assertEquals(account, result);
        verify(bankAccountRepository).findById(1L);
    }

    @Test
    void getById_shouldThrowIfNotFound() {
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> bankAccountService.getById(1L));

        assertEquals("Account not found with id: 1", ex.getMessage());
    }

    @Test
    void getByOwnerId_shouldReturnAccounts() {
        List<BankAccount> accounts = List.of(new BankAccount(), new BankAccount());
        when(bankAccountRepository.findByOwnerId(42L)).thenReturn(accounts);

        List<BankAccount> result = bankAccountService.getByOwnerId(42L);

        assertEquals(2, result.size());
        verify(bankAccountRepository).findByOwnerId(42L);
    }
}
