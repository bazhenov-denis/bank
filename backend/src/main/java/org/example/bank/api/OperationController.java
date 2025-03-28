package org.example.bank.api;


import org.example.bank.domain.Operation;
import org.example.bank.domain.TransferRequest;
import org.example.bank.domain.User;
import org.example.bank.facade.FinancialFacade;
import org.example.bank.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/operations")
public class OperationController {

    private final FinancialFacade financialFacade;
    private final UserRepository userRepository; // Инжектируйте репозиторий для работы с пользователями


    public OperationController(FinancialFacade financialFacade, UserRepository userRepository) {
        this.financialFacade = financialFacade;
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Operation> getOperationById(@PathVariable Long id) {
        Operation operation = financialFacade.getOperationById(id);
        return ResponseEntity.ok(operation);
    }

    @PostMapping
    public ResponseEntity<Operation> createOperation(@RequestBody Operation operation, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Пользователь не найден"));;
        operation.setUser(user);
        Operation created = financialFacade.createOperation(operation);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Operation> updateOperation(@PathVariable Long id, @RequestBody Operation operation) {
        operation.setId(id);
        Operation updated = financialFacade.updateOperation(operation);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOperation(@PathVariable Long id) {
        financialFacade.deleteOperation(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferFunds(@RequestBody TransferRequest transferRequest) {
        boolean success = financialFacade.transferFunds(
                transferRequest.getFromAccountId(),
                transferRequest.getToAccountId(),
                transferRequest.getAmount()
        );
        if (success) {
            return ResponseEntity.ok("Перевод выполнен успешно.");
        } else {
            return ResponseEntity.badRequest().body("Ошибка при переводе средств.");
        }
    }


    @GetMapping("/last-operation-date")
    public ResponseEntity<Map<String, String>> getLastOperationDate() {
        Operation lastOperation = financialFacade.getLastOperation();

        String lastDate;
        if (lastOperation == null || lastOperation.getDate() == null) {
            lastDate = LocalDate.now().toString();
        } else {
            lastDate = lastOperation.getDate().toString();
        }
        return ResponseEntity.ok(Collections.singletonMap("date", lastDate));
    }

    @GetMapping("/user")
    public ResponseEntity<List<Operation>> getUserOperations(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        List<Operation> operations = financialFacade.getOperationsByUserId(user.getId());
        if (operations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(operations);
    }
}
