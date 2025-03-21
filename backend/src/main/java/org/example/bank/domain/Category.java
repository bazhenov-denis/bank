package org.example.bank.domain;

import jakarta.persistence.*;

// Обозначение класса как сущности для JPA
@Entity
public class Category {

    // Поле id является первичным ключом
    @Id
    // Автоматическая генерация идентификатора при сохранении в БД (стратегия IDENTITY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Поле type хранит значение перечисления, которое сохраняется в виде строки
    @Enumerated(EnumType.STRING)
    private CategoryType type;

    // Поле для наименования категории
    private String name;

    // Конструктор по умолчанию (обязателен для работы JPA)
    public Category() {
    }

    // Конструктор для создания объекта с заданными параметрами
    public Category(CategoryType type, String name) {
        this.type = type;
        this.name = name;
    }

    // Геттер для id
    public Long getId() {
        return id;
    }

    // Геттер для type
    public CategoryType getType() {
        return type;
    }

    // Геттер для name
    public String getName() {
        return name;
    }

    // Сеттер для id
    public void setId(Long id) {
        this.id = id;
    }

    // Сеттер для type
    public void setType(CategoryType type) {
        this.type = type;
    }

    // Сеттер для name
    public void setName(String name) {
        this.name = name;
    }
}
