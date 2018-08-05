package com.jmorri6.pojo;

import java.util.ArrayList;
import java.util.List;

public class Category {
	private int id;
	private String name;
	private List<Budget> budgets;
	
	public Category(int id, String name, List<Budget> budgets) {
		this.id = id;
		this.name = name;
		this.budgets = budgets;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public List<Budget> getBudgets() {
		return budgets;
	}
	
	public void addBudget(Budget b) {
		if (budgets == null) {
			budgets = new ArrayList<Budget>();
		}
		budgets.add(b);
	}
}
