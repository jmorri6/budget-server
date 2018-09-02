package com.jmorri6.util;

public class Queries {
	public static final String DELETE_BUDGET = "DELETE FROM Budget WHERE budget_id = ?";
	public static final String DELETE_BUDGET_BALANCE = "DELETE FROM BudgetBalance where budget_id = ?";
	public static final String DELETE_CATEGORY = "DELETE FROM Category WHERE category_id = ?";
	public static final String DELETE_BUDGET_CATEGORY = "DELETE FROM BudgetCategory WHERE budget_id = ?";
	public static final String DELETE_BUDGET_TXNS = "DELETE FROM BudgetTransaction WHERE budget_id = ?";
	public static final String DELETE_OLD_TXNS = "DELETE FROM BudgetTransaction WHERE created < ?";
	public static final String DELETE_NOTE = "DELETE FROM Notes WHERE id = ?";
	public static final String DELETE_INCOME = "DELETE FROM Income WHERE income_id = ?";
	
	
	public static final String GET_ALL_CATEGORIES = "SELECT * FROM Category";
	public static final String INSERT_CATEGORY = "INSERT INTO Category (name) VALUES (?)";
	
	public static final String INSERT_BUDGET = "INSERT INTO Budget(name, allocation, txn_requires_desc) VALUES (?,?,?)";
	public static final String INSERT_BUDGET_CATEGORY = "INSERT INTO BudgetCategory(budget_id, category_id) VALUES (?,?)";
	public static final String INSERT_BUDGET_BALANCE = "INSERT INTO BudgetBalance(budget_id, balance) VALUES (?,?)";
	public static final String GET_BUDGET_BALANCE = "SELECT SUM(balance) from BudgetBalance";
	public static final String GET_BALANCE_FOR_BUDGET = "SELECT balance from BudgetBalance where budget_id = ?";
	
	public static final String UPDATE_BUDGET_BALANCE = "UPDATE BudgetBalance set balance = ? WHERE budget_id = ?";
	public static final String UPDATE_BUDGET_CATEGORY = "UPDATE BudgetCategory set category_id = ? WHERE budget_id = ?";
	
	public static final String INSERT_NOTE = "INSERT INTO Notes(desc) VALUES (?)";
	public static final String GET_NOTES = "SELECT * FROM Notes";
	
	public static final String INSERT_INCOME = "INSERT INTO Income(desc, amount, frequencyPerYr, monthlyDeduction) VALUES (?, ?, ?, ?)";
	public static final String GET_ALL_INCOME = "SELECT * FROM Income";
	
	public static final String GET_BUDGETS = "SELECT b.*, bb.balance, c.* " +
			"FROM Budget b, BudgetBalance bb, BudgetCategory bc, Category c " +
			" WHERE bb.budget_id = b.budget_id " +
			"  AND bc.budget_id = b.budget_id " +
			"  AND c.category_id = bc.category_id ";
	
	public static final String UPDATE_BUDGET = "UPDATE Budget set name = ?, allocation = ?, txn_requires_desc = ? WHERE budget_id = ?";
	
	public static final String INSERT_TRANSACTION = "INSERT INTO BudgetTransaction(budget_id, txn_type, amount, desc, created) " +
			"VALUES (?, ?, ?, ?, ?)";
	
	public static final String GET_SPENT_FOR_BUDGET = "SELECT SUM(amount) FROM BudgetTransaction WHERE budget_id = ? AND txn_type = 2 AND created > ?";
	public static final String GET_ALLOCATED_INCOME = "SELECT SUM(allocation) FROM Budget";
	
	public static final String INSERT_SCHEDULED_TXN = "INSERT INTO ScheduledTransactions (name, budget_id, dayToRun, amount, nextRunTime) values (?, ?, ?, ?, ?)";
	public static final String DELETE_SCHEDULED_TXN = "DELETE from ScheduledTransactions where id = ?";
	public static final String GET_SCHEDULED_TXNS = "SELECT * FROM ScheduledTransactions";
	public static final String UPDATE_SCHEDULED_RUN_TIME = "UPDATE ScheduledTransactions set lastRunTime = ?, nextRunTime = ? where id = ?";
	
	public static final String GET_SCHED_TXNS_FOR_DISPLAY = "select t.id, t.name, b.name, t.amount, t.dayToRun, t.lastRunTime, t.nextRunTime " + 
			"from ScheduledTransactions t, Budget b " + 
			"where b.budget_id = t.budget_id " + 
			"order by b.name ";
}
