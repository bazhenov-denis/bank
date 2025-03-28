package org.example.bank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bank.api.CategoryController;
import org.example.bank.domain.Category;
import org.example.bank.facade.FinancialFacade;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CategoryController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                org.example.bank.security.JwtAuthenticationFilter.class
        }),
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class
        })
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private FinancialFacade financialFacade;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public FinancialFacade financialFacade() {
            return Mockito.mock(FinancialFacade.class);
        }
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllCategories_shouldReturnList() throws Exception {
        Category c1 = new Category();
        c1.setId(1L);
        c1.setName("Food");

        Category c2 = new Category();
        c2.setId(2L);
        c2.setName("Transport");

        Mockito.when(financialFacade.getAllCategories()).thenReturn(List.of(c1, c2));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Food"))
                .andExpect(jsonPath("$[1].name").value("Transport"));
    }

    @Test
    void getCategoryById_shouldReturnCategory() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("Utilities");

        Mockito.when(financialFacade.getCategoryById(1L)).thenReturn(category);

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Utilities"));
    }

    @Test
    void createCategory_shouldReturnCreated() throws Exception {
        Category category = new Category();
        category.setName("New Category");

        Category saved = new Category();
        saved.setId(10L);
        saved.setName("New Category");

        Mockito.when(financialFacade.createCategory(any(Category.class))).thenReturn(saved);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("New Category"));
    }

    @Test
    void updateCategory_shouldReturnUpdated() throws Exception {
        Category category = new Category();
        category.setId(2L);
        category.setName("Updated");

        Mockito.when(financialFacade.updateCategory(any(Category.class))).thenReturn(category);

        mockMvc.perform(put("/api/categories/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void deleteCategory_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/categories/3"))
                .andExpect(status().isNoContent());

        Mockito.verify(financialFacade).deleteCategory(3L);
    }
}
