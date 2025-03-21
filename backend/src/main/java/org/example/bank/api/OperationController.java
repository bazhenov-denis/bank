package org.example.bank.api;


import org.example.bank.domain.Operation;
import org.example.bank.domain.TransferRequest;
import org.example.bank.domain.User;
import org.example.bank.facade.FinancialFacade;
import org.example.bank.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/operations")
public class OperationController {

    private final FinancialFacade financialFacade;
    private final UserRepository userRepository; // Инжектируйте репозиторий для работы с пользователями


    public OperationController(FinancialFacade financialFacade, UserRepository userRepository) {
        this.financialFacade = financialFacade;
        this.userRepository = userRepository;
    }


    @GetMapping
    public ResponseEntity<List<Operation>> getAllOperations() {
        List<Operation> operations = financialFacade.getAllOperations();
        return ResponseEntity.ok(operations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Operation> getOperationById(@PathVariable Long id) {
        Operation operation = financialFacade.getOperationById(id);
        return ResponseEntity.ok(operation);
    }

    @PostMapping
    public ResponseEntity<Operation> createOperation(@RequestBody Operation operation, Authentication authentication) {
        // Получаем имя текущего пользователя из объекта аутентификации
        String username = authentication.getName();
        // Ищем пользователя в БД
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Пользователь не найден"));;
        // Устанавливаем пользователя в операцию
        operation.setUser(user);

        // Создаем операцию через фасад
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
}
