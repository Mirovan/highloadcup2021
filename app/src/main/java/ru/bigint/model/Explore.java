package ru.bigint.model;

public class Explore {
    private ExploreRequest area;
    private int amount;

    public ExploreRequest getArea() {
        return area;
    }

    public void setArea(ExploreRequest area) {
        this.area = area;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Explore{" +
                "area=" + area +
                ", amount=" + amount +
                '}';
    }
}
