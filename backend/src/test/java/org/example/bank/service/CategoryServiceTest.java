package org.example.bank.service;

import org.example.bank.domain.Category;
import org.example.bank.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    private CategoryRepository categoryRepository;
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryRepository = mock(CategoryRepository.class);
        categoryService = new CategoryService(categoryRepository);
    }

    @Test
    void create_shouldSaveCategory() {
        Category category = new Category();
        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.create(category);

        assertEquals(category, result);
        verify(categoryRepository).save(category);
    }

    @Test
    void update_shouldSaveCategory() {
        Category category = new Category();
        category.setId(1L);
        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.update(category);

        assertEquals(category, result);
        verify(categoryRepository).save(category);
    }

    @Test
    void delete_shouldCallRepository() {
        categoryService.delete(1L);
        verify(categoryRepository).deleteById(1L);
    }

    @Test
    void getAll_shouldReturnAllCategories() {
        List<Category> categories = List.of(new Category(), new Category());
        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> result = categoryService.getAll();

        assertEquals(2, result.size());
        verify(categoryRepository).findAll();
    }

    @Test
    void getById_shouldReturnCategory() {
        Category category = new Category();
        category.setId(1L);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Category result = categoryService.getById(1L);

        assertEquals(category, result);
        verify(categoryRepository).findById(1L);
    }

    @Test
    void getById_shouldThrowIfNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> categoryService.getById(1L));

        assertEquals("Category not found with id: 1", ex.getMessage());
    }
}
