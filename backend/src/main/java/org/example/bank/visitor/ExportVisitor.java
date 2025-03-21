package org.example.bank.visitor;


import org.example.bank.domain.BankAccount;
import org.example.bank.domain.Category;
import org.example.bank.domain.Operation;

public interface ExportVisitor {
    String export(BankAccount bankAccount);
    String export(Category category);
    String export(Operation operation);
}
