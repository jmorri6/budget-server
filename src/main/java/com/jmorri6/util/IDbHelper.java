package com.jmorri6.util;

import java.sql.Date;
import java.util.List;

import com.jmorri6.pojo.Budget;
import com.jmorri6.pojo.Category;
import com.jmorri6.pojo.Income;
import com.jmorri6.pojo.Note;
import com.jmorri6.pojo.ScheduledJob;
import com.jmorri6.pojo.Transaction;

public interface IDbHelper {

	List<Category> getAllCategories();

	List<Budget> getAllBudgets();

	void addBudget(Budget b) throws Exception;

	void updateBalance(int budgetId, double balance) throws Exception;

	void updateBudget(Budget b) throws Exception;

	void addCategory(String name) throws Exception;

	void deleteCategory(int id) throws Exception;

	void addNote(String note) throws Exception;

	void deleteNote(int id) throws Exception;

	List<Note> getNotes();

	List<Income> getAllIncome();

	void insertIncome(Income i) throws Exception;

	void deleteIncome(int id) throws Exception;

	void insertTransaction(Transaction t) throws Exception;

	double getBudgetBalance();
	
	double getAllocatedIncome();
	
	void deleteBudget(int id) throws Exception;

	void purge(Date olderThan) throws Exception;

	void insertMonthlyIncome(int scheduleId, Date nextRunTime) throws Exception;

	List<ScheduledJob> getSchedueldJobs();

	void insertScheduledTransaction(ScheduledJob job) throws Exception;

	void deleteScheduledJob(int id) throws Exception;

	void createScheduledJob(ScheduledJob job) throws Exception;

	void updateNextRunTime(ScheduledJob job) throws Exception;
}