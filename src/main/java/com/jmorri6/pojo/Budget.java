package com.jmorri6.pojo;

public class Budget {
	private int id;
	private String name;
	private double allocation;
	private double balance;
	private double currentMonthSpent;
	public double getCurrentMonthSpent() {
		return currentMonthSpent;
	}

	public void setCurrentMonthSpent(double currentMonthSpent) {
		this.currentMonthSpent = currentMonthSpent;
	}

	private Category category;
	private boolean txnRequireDesc;
	
	public Budget(int id, String name, double allocation, boolean txnRequireDesc,
			double balance, Category category) {
		this.id = id;
		this.name = name;
		this.allocation = allocation;
		this.txnRequireDesc = txnRequireDesc;
		this.balance = balance;
		this.category = category;
	}

	public boolean isTxnRequireDesc() {
		return txnRequireDesc;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public double getAllocation() {
		return allocation;
	}
	
	public double getBalance() {
		return balance;
	}

	public Category getCategory() {
		return category;
	}
}
