package com.jmorri6.pojo;

public class TransactionReport {
	private String budgetName;
	private String month;
	private Double amount;
	
	public TransactionReport(String budgetName, Double amount) {
		this.budgetName = budgetName;
		this.amount = amount;
	}
	
	public TransactionReport(String month, String budgetName, Double amount) {
		this(budgetName, amount);
		this.month = month;
	}
	
	public String getBudgetName() {
		return budgetName;
	}
	public void setBudgetName(String budgetName) {
		this.budgetName = budgetName;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
}
