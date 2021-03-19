package ru.bigint.model.response;

import java.util.Arrays;

public class Balance {
    private int balance;
    private Integer[] wallet;

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public Integer[] getWallet() {
        return wallet;
    }

    public void setWallet(Integer[] wallet) {
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
