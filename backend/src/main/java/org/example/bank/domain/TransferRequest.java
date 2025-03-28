package org.example.bank.domain;

public class TransferRequest {
    private Long fromAccountId;
    private Long toAccountId;
    private double amount;

    public TransferRequest(long l, long l1, double v) {
    }

    public TransferRequest() {

    }

    // Геттеры и сеттеры
    public Long getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(Long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public Long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(Long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
