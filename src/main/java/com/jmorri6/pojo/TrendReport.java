package com.jmorri6.pojo;

import java.util.List;

public class TrendReport {
	private List<String> months;
	private List<BudgetTrend> budgetAmounts;
	
	public List<String> getMonths() {
		return months;
	}
	public void setMonths(List<String> months) {
		this.months = months;
	}
	public List<BudgetTrend> getBudgetAmounts() {
		return budgetAmounts;
	}
	public void setBudgetAmounts(List<BudgetTrend> budgetAmounts) {
		this.budgetAmounts = budgetAmounts;
	}
}
