package org.example.bank.service;

import org.example.bank.domain.BankAccount;
import org.example.bank.repository.BankAccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountService {
    private final BankAccountRepository bankAccountRepository;

    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public BankAccount create(BankAccount account) {
        return bankAccountRepository.save(account);
    }

    public BankAccount update(BankAccount account) {
        return bankAccountRepository.save(account);
    }

    public void delete(Long id) {
        bankAccountRepository.deleteById(id);
    }

    public List<BankAccount> getAll() {
        return bankAccountRepository.findAll();
    }

    public BankAccount getById(Long id) {
        return bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
    }

    public List<BankAccount> getByOwnerId(Long userId) {
        return bankAccountRepository.findByOwnerId(userId);
    }

}
