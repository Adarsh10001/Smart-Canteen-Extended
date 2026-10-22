package model;

public interface PaymentMethod {
    boolean processPayment(double amount);
}