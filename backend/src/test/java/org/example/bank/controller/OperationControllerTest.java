package org.example.bank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bank.api.OperationController;
import org.example.bank.domain.BankAccount;
import org.example.bank.domain.Operation;
import org.example.bank.domain.OperationType;
import org.example.bank.domain.TransferRequest;
import org.example.bank.domain.User;
import org.example.bank.facade.FinancialFacade;
import org.example.bank.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@WebMvcTest(
        controllers = OperationController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = { org.example.bank.security.JwtAuthenticationFilter.class }
        )
)
@Import(OperationControllerTest.MockBeans.class)
class OperationControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private FinancialFacade financialFacade;
    @Autowired private UserRepository userRepository;
    @Autowired private ObjectMapper objectMapper;

    @TestConfiguration
    static class MockBeans {
        @Bean public FinancialFacade financialFacade() {
            return Mockito.mock(FinancialFacade.class);
        }
        @Bean public UserRepository userRepository() {
            return Mockito.mock(UserRepository.class);
        }
    }

    @Test
    @WithMockUser
    void getOperationById_shouldReturnOperation() throws Exception {
        Operation operation = new Operation();
        operation.setId(1L);
        operation.setAmount(BigDecimal.valueOf(100));
        Mockito.when(financialFacade.getOperationById(1L)).thenReturn(operation);

        mockMvc.perform(get("/api/operations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(username = "testuser")
    void createOperation_shouldReturnCreatedOperation() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        Operation operation = new Operation();
        operation.setId(1L);
        operation.setType(OperationType.INCOME);
        operation.setAmount(BigDecimal.valueOf(200));
        operation.setBankAccount(new BankAccount());

        Mockito.when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        Mockito.when(financialFacade.createOperation(any(Operation.class))).thenReturn(operation);

        mockMvc.perform(post("/api/operations")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(operation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(username = "testuser")
    void transferFunds_shouldReturnOkIfSuccess() throws Exception {
        // Передаем JSON-строку напрямую, чтобы гарантировать корректные значения
        String requestJson = "{\"fromAccountId\":1,\"toAccountId\":2,\"amount\":100.0}";
        Mockito.when(financialFacade.transferFunds(1L, 2L, 100.0)).thenReturn(true);

        mockMvc.perform(post("/api/operations/transfer")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Перевод выполнен успешно."));
    }


    @Test
    @WithMockUser(username = "testuser")
    void transferFunds_shouldReturnBadRequestIfFailed() throws Exception {
        TransferRequest req = new TransferRequest(1L, 2L, 9999.0);
        Mockito.when(financialFacade.transferFunds(1L, 2L, 9999.0)).thenReturn(false);

        mockMvc.perform(post("/api/operations/transfer")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Ошибка при переводе средств."));

    }

    @Test
    @WithMockUser
    void getLastOperationDate_shouldReturnDate() throws Exception {
        Operation op = new Operation();
        op.setDate(LocalDate.of(2024, 1, 10));
        Mockito.when(financialFacade.getLastOperation()).thenReturn(op);

        mockMvc.perform(get("/api/operations/last-operation-date"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value("2024-01-10"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void getUserOperations_shouldReturnOperations() throws Exception {
        User user = new User();
        user.setId(5L);
        user.setUsername("testuser");

        Operation op1 = new Operation(); op1.setId(1L);
        Operation op2 = new Operation(); op2.setId(2L);

        Mockito.when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        Mockito.when(financialFacade.getOperationsByUserId(5L)).thenReturn(List.of(op1, op2));

        mockMvc.perform(get("/api/operations/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @WithMockUser(username = "testuser")
    void getUserOperations_shouldReturnNoContentIfEmpty() throws Exception {
        User user = new User();
        user.setId(99L);
        user.setUsername("testuser");

        Mockito.when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        Mockito.when(financialFacade.getOperationsByUserId(99L)).thenReturn(List.of());

        mockMvc.perform(get("/api/operations/user"))
                .andExpect(status().isNoContent());
    }
}
