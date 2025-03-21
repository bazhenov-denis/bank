package org.example.bank.api;


import org.example.bank.domain.BankAccount;
import org.example.bank.facade.FinancialFacade;
import org.example.bank.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class BankAccountController {

    private final FinancialFacade financialFacade;
    private final UserService userService;


    public BankAccountController(FinancialFacade financialFacade, UserService userService) {
        this.financialFacade = financialFacade;
        this.userService = userService;
    }

/*
    @GetMapping
    public ResponseEntity<List<BankAccount>> getAllAccounts() {
        List<BankAccount> accounts = financialFacade.getAllAccounts();
        if (accounts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(accounts);
    }
*/


    @GetMapping("/{id}")
    public ResponseEntity<BankAccount> getAccountById(@PathVariable Long id) {
        BankAccount account = financialFacade.getAccountById(id);
        return ResponseEntity.ok(account);
    }
/*

    @PostMapping
    public ResponseEntity<BankAccount> createAccount(@RequestBody BankAccount bankAccount) {
        BankAccount created = financialFacade.createAccount(bankAccount);
        return ResponseEntity.ok(created);
    }
*/

    @PutMapping("/{id}")
    public ResponseEntity<BankAccount> updateAccount(@PathVariable Long id, @RequestBody BankAccount bankAccount) {
        bankAccount.setId(id);
        BankAccount updated = financialFacade.updateAccount(bankAccount);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        financialFacade.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    // Эндпоинт возвращает только счета текущего пользователя
    @GetMapping
    public ResponseEntity<List<BankAccount>> getUserAccounts(Authentication authentication) {
        String username = authentication.getName();
        var user = userService.findByUsername(username);
        // Метод в FinancialFacade, который возвращает счета по userId
        List<BankAccount> accounts = financialFacade.getAccountsByUserId(user.getId());
        if (accounts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(accounts);
    }

    // Остальные методы (getById, create, update, delete) также должны учитывать владельца
    // например, при создании счета назначать владельца:
    @PostMapping
    public ResponseEntity<BankAccount> createAccount(@RequestBody BankAccount bankAccount, Authentication authentication) {
        String username = authentication.getName();
        var user = userService.findByUsername(username);
        bankAccount.setOwner(user);
        BankAccount created = financialFacade.createAccount(bankAccount);
        return ResponseEntity.ok(created);
    }
}
