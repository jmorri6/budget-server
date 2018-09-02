package com.jmorri6.pojo;

import java.util.List;

public class BudgetTrend {
	private String name;
	private List<Double> amounts;
	
	public BudgetTrend(String name, List<Double> amounts) {
		this.name = name;
		this.amounts = amounts;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Double> getAmounts() {
		return amounts;
	}
	public void setAmount(List<Double> amounts) {
		this.amounts = amounts;
	}
}
