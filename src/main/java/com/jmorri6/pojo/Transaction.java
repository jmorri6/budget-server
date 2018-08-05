package com.jmorri6.pojo;

import java.util.Date;

public class Transaction {
	private int id;
	private Budget budget;
	private double amount;
	private String desc;
	private TransactionType type;
	private Date created;
	
	public Transaction(int id, Budget budget, double amount, String desc, TransactionType type, Date created) {
		this.id = id;
		this.budget = budget;
		this.amount = amount;
		this.desc = desc;
		this.type = type;
		this.created = created;
	}
	public int getId() {
		return id;
	}
	public Budget getBudget() {
		return budget;
	}
	public double getAmount() {
		return amount;
	}
	public String getDesc() {
		return desc;
	}
	public TransactionType getType() {
		return type;
	}
	public Date getCreated() {
		return created;
	}
}
