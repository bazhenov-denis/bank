package org.example.bank.command;

public class TimingCommandDecorator implements Command {

    private final Command wrappedCommand;

    public TimingCommandDecorator(Command command) {
        this.wrappedCommand = command;
    }

    @Override
    public void execute() {
        long startTime = System.currentTimeMillis();
        wrappedCommand.execute();
        long endTime = System.currentTimeMillis();
        System.out.println("Execution time: " + (endTime - startTime) + " ms");
    }
}
