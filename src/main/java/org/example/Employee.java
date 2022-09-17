package org.example;

public class Employee {
    private int productNum;
    private int amount;

    public Employee(int productNum, int amount) {
        this.productNum = productNum;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return productNum + "," + amount;
    }
}
