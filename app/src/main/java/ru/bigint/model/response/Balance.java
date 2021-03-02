package ru.bigint.model.response;

import java.util.Arrays;

public class Balance {
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

    @Override
    public String toString() {
        return "Balance{" +
                "balance=" + balance +
                ", wallet=" + Arrays.toString(wallet) +
                '}';
    }
}
