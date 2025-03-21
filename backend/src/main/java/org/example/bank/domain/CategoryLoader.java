package org.example.bank.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bank.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.util.List;

@Component
public class CategoryLoader implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;
    private final ObjectMapper objectMapper;

    public CategoryLoader(CategoryRepository categoryRepository, ObjectMapper objectMapper) {
        this.categoryRepository = categoryRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        // Загружаем JSON из ресурсов
        InputStream inputStream = getClass().getResourceAsStream("/categories.json");
        List<CategoryDto> categories = objectMapper.readValue(inputStream, new TypeReference<>() {});

        // Добавляем категории, если их нет
        for (CategoryDto category : categories) {
            if (!categoryRepository.existsByName(category.getName())) {
                categoryRepository.save(new Category(CategoryType.valueOf(category.getType()), category.getName()));
                System.out.println("Добавлена категория: " + category.getName());
            }
        }
    }

    private static class CategoryDto {
        private String name;
        private String type;

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }
    }
}
