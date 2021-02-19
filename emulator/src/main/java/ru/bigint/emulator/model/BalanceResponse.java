package ru.bigint.emulator.model;

public class BalanceResponse {
    private int balance;
    private int[] wallet;

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int[] getWallet() {
        return wallet;
    }

    public void setWallet(int[] wallet) {
        this.wallet = wallet;
    }
}
