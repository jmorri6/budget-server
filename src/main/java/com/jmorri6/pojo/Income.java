package com.jmorri6.pojo;

public class Income {
	private int id;
	private String desc;
	private double amount;
	private int frequencyPerYr;
	private double monthlyDeduction;
	
	public Income(int id, String desc, double amount, int frequencyPerYr, double monthlyDeduction) {
		this.id = id;
		this.desc = desc;
		this.amount = amount;
		this.frequencyPerYr = frequencyPerYr;
		this.monthlyDeduction = monthlyDeduction;
	}

	public double getMonthlyDeduction() {
		return monthlyDeduction;
	}

	public int getId() {
		return id;
	}

	public String getDesc() {
		return desc;
	}

	public double getAmount() {
		return amount;
	}

	public int getFrequencyPerYr() {
		return frequencyPerYr;
	}
}
