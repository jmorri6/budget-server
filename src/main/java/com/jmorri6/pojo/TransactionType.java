package com.jmorri6.pojo;

public enum TransactionType {
	Credit(1),
	Debit(2);
	
	private int code;
	private TransactionType(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return this.code;
	}
}
