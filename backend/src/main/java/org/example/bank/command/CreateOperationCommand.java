package org.example.bank.command;


import org.example.bank.domain.Operation;
import org.example.bank.facade.FinancialFacade;

public class CreateOperationCommand implements Command {

    private final FinancialFacade facade;
    private final Operation operation;

    public CreateOperationCommand(FinancialFacade facade, Operation operation) {
        this.facade = facade;
        this.operation = operation;
    }

    @Override
    public void execute() {
        facade.createOperation(operation);
    }
}
