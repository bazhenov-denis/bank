package org.example.bank.visitor;

import org.example.bank.domain.BankAccount;
import org.example.bank.domain.Category;
import org.example.bank.domain.Operation;

public class CsvExportVisitor implements ExportVisitor {

    @Override
    public String export(BankAccount bankAccount) {
        return bankAccount.getId() + "," + bankAccount.getName() + "," + bankAccount.getBalance();
    }

    @Override
    public String export(Category category) {
        return category.getId() + "," + category.getType() + "," + category.getName();
    }

    @Override
    public String export(Operation operation) {
        return operation.getId() + "," + operation.getType() + "," + operation.getBankAccount().getId() + ","
                + operation.getAmount() + "," + operation.getDate() + "," + operation.getDescription() + ","
                + operation.getCategory().getId();
    }
}
