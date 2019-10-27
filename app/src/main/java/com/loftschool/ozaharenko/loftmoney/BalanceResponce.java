package com.loftschool.ozaharenko.loftmoney;

public class BalanceResponce {

    private String status;

    private float total_expenses;

    private float total_income;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getTotal_expenses() {
        return total_expenses;
    }

    public void setTotal_expenses(float total_expenses) {
        this.total_expenses = total_expenses;
    }

    public float getTotal_income() {
        return total_income;
    }

    public void setTotal_income(float total_income) {
        this.total_income = total_income;
    }
}
