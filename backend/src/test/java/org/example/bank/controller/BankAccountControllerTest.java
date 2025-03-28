package org.example.bank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bank.api.BankAccountController;
import org.example.bank.domain.BankAccount;
import org.example.bank.domain.User;
import org.example.bank.facade.FinancialFacade;
import org.example.bank.service.UserService;
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

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = BankAccountController.class,
        // Исключаем только проблемный фильтр, который требует бин JwtUtils
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = { org.example.bank.security.JwtAuthenticationFilter.class }
        )
)
@Import(BankAccountControllerTest.MockBeans.class)
class BankAccountControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private FinancialFacade financialFacade;
    @Autowired private UserService userService;
    @Autowired private ObjectMapper objectMapper;

    @TestConfiguration
    static class MockBeans {
        @Bean
        public FinancialFacade financialFacade() {
            return Mockito.mock(FinancialFacade.class);
        }

        @Bean
        public UserService userService() {
            return Mockito.mock(UserService.class);
        }
    }

    @Test
    @WithMockUser(username = "testuser")
    void getAccountById_shouldReturnAccount() throws Exception {
        BankAccount account = new BankAccount();
        account.setId(1L);
        Mockito.when(financialFacade.getAccountById(1L)).thenReturn(account);

        mockMvc.perform(get("/api/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(username = "testuser")
    void createAccount_shouldAssignOwnerAndReturnCreated() throws Exception {
        User user = new User();
        user.setId(42L);
        user.setUsername("testuser");

        BankAccount toCreate = new BankAccount();
        toCreate.setName("New Account");

        BankAccount created = new BankAccount();
        created.setId(1L);
        created.setName("New Account");
        created.setOwner(user);

        Mockito.when(userService.findByUsername("testuser")).thenReturn(user);
        Mockito.when(financialFacade.createAccount(any(BankAccount.class))).thenReturn(created);

        mockMvc.perform(post("/api/accounts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New Account"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void getUserAccounts_shouldReturnList() throws Exception {
        User user = new User();
        user.setId(7L);
        user.setUsername("testuser");

        BankAccount acc1 = new BankAccount();
        acc1.setId(1L);
        BankAccount acc2 = new BankAccount();
        acc2.setId(2L);

        Mockito.when(userService.findByUsername("testuser")).thenReturn(user);
        Mockito.when(financialFacade.getAccountsByUserId(7L)).thenReturn(List.of(acc1, acc2));

        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser(username = "testuser")
    void getUserAccounts_shouldReturnNoContentIfEmpty() throws Exception {
        User user = new User();
        user.setId(99L);
        user.setUsername("testuser");

        Mockito.when(userService.findByUsername("testuser")).thenReturn(user);
        Mockito.when(financialFacade.getAccountsByUserId(99L)).thenReturn(List.of());

        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "testuser")
    void updateAccount_shouldUpdateAndReturnAccount() throws Exception {
        BankAccount acc = new BankAccount();
        acc.setId(5L);
        acc.setName("Updated");

        Mockito.when(financialFacade.updateAccount(any(BankAccount.class))).thenReturn(acc);

        mockMvc.perform(put("/api/accounts/5")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(acc)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void deleteAccount_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/accounts/3").with(csrf()))
                .andExpect(status().isNoContent());

        Mockito.verify(financialFacade).deleteAccount(3L);
    }
}
