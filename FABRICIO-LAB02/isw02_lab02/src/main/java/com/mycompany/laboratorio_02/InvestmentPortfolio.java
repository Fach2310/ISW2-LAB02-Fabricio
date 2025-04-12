package com.mycompany.laboratorio_02;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InvestmentPortfolio {

    private String userId;
    private List<Transaction> transactions;
    private Map<String, Double> fundBalances;

    public InvestmentPortfolio(String userId) {
        this.userId = userId;
        this.transactions = new ArrayList<>();
        this.fundBalances = new HashMap<>();
    }

    public void processBuyTransaction(String fundCode, double amount) {
        validateTransaction(fundCode, amount);
        processTransaction("BUY", fundCode, amount);
    }

    public void processSellTransaction(String fundCode, double amount) {
        validateTransaction(fundCode, amount);
        // Verificar fondos suficientes para SELL
        Double currentBalance = fundBalances.getOrDefault(fundCode, 0.0);
        if (currentBalance < amount) {
            throw new IllegalArgumentException("Saldo insuficiente. Balance actual: " + currentBalance);
        }
        processTransaction("SELL", fundCode, amount);
    }

    private void validateTransaction(String fundCode, double amount) {
        if (fundCode == null || fundCode.isEmpty()) {
            throw new IllegalArgumentException("El código del fondo no puede estar vacío");
        }

        if (amount <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }

        // Verificar si el fondo existe
        if (!isFundValid(fundCode)) {
            throw new IllegalArgumentException("El fondo no existe: " + fundCode);
        }
    }

    private void processTransaction(String transactionType, String fundCode, double amount) {
        String transactionId = UUID.randomUUID().toString();
        Date transactionDate = new Date();
        Transaction transaction = new Transaction(transactionId, userId, fundCode, transactionType, amount, transactionDate);

        // Registrar la transacción
        transactions.add(transaction);

        // Actualizar el balance del fondo
        updateFundBalance(fundCode, transactionType, amount);

        // Registrar la transacción en la base de datos
        saveTransactionToDatabase(transaction);

        // Notificar al usuario
        sendNotificationToUser(transactionType, amount, fundCode);

        System.out.println("Transacción de " + transactionType.toLowerCase() + " procesada exitosamente. ID: " + transactionId);
    }

    private void updateFundBalance(String fundCode, String transactionType, double amount) {
        double currentBalance = fundBalances.getOrDefault(fundCode, 0.0);
        if (transactionType.equals("BUY")) {
            fundBalances.put(fundCode, currentBalance + amount);
        } else if (transactionType.equals("SELL")) {
            fundBalances.put(fundCode, currentBalance - amount);
        }
    }

    private boolean isFundValid(String fundCode) {
        // Simulación de validación de fondo
        return fundCode.startsWith("FUND");
    }

    private void saveTransactionToDatabase(Transaction transaction) {
        // Simulación de guardado en base de datos
        System.out.println("Guardando transacción en la base de datos: " + transaction.getId());
    }

    private void sendNotificationToUser(String transactionType, double amount, String fundCode) {
        String message = String.format("Se ha realizado un %s por $%.2f en el fondo %s", transactionType, amount, fundCode);
        // Simulación de envío de notificación
        System.out.println("Notificación para usuario " + userId + ": " + message);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public Map<String, Double> getFundBalances() {
        return fundBalances;
    }
}
