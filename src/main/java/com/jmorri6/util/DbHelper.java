package com.jmorri6.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Singleton;

import com.jmorri6.pojo.Budget;
import com.jmorri6.pojo.Category;
import com.jmorri6.pojo.Income;
import com.jmorri6.pojo.Note;
import com.jmorri6.pojo.ScheduledJob;
import com.jmorri6.pojo.Transaction;
import com.jmorri6.pojo.TransactionType;

@Singleton
public class DbHelper implements IDbHelper {
	private static final Logger LOGGER = Logger.getLogger(DbHelper.class.getName());
			
	@Override
	public List<Category> getAllCategories() {
		List<Category> results = new ArrayList<Category>();
		ResultSet rs = executeGet(Queries.GET_ALL_CATEGORIES);
		if (rs != null) {
	      try {
			while ( rs.next() ) {
			     results.add(new Category(rs.getInt(1), rs.getString(2), null));
			  }
	      } catch (SQLException e) {
	    	  	LOGGER.severe(LogConfig.format("error reading results", e));
	      }
		}
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			LOGGER.severe(LogConfig.format("error closing connection", e));
		}
		return results;
	}
	
	@Override
	public List<Budget> getAllBudgets() {
		List<Budget> results = new ArrayList<Budget>();
		ResultSet rs = executeGet(Queries.GET_BUDGETS);
		if (rs != null) {
			try {
				while (rs.next()) {
					Category c = new Category(rs.getInt(6), rs.getString(7), null);
					results.add(new Budget(rs.getInt(1),
							rs.getString(2),
							rs.getDouble(3),
							rs.getBoolean(4),
							rs.getDouble(5),
							c));
				}
			} catch (SQLException e) {
	    	  	LOGGER.severe(LogConfig.format("severe reading results", e));
	      }
		}
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			LOGGER.severe(LogConfig.format("error closing connection", e));
		}
		populateSpentForTheMoneth(results);
		return results;
	}
	
	private void populateSpentForTheMoneth(List<Budget> budgets) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DATE, 1);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 1);
		Date d = new Date(c.getTimeInMillis());
		
		for (Budget b : budgets) {
			ResultSet rs = executeGet(Queries.GET_SPENT_FOR_BUDGET, b.getId(), d);
			if (rs != null) {
				try {
					rs.next();
					b.setCurrentMonthSpent(rs.getDouble(1));
				} catch (SQLException e) {
		    	  	LOGGER.severe(LogConfig.format("severe reading results", e));
		      }
			}
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				LOGGER.severe(LogConfig.format("error closing connection", e));
			}
		}
	}
	
	@Override
	public double getBudgetBalance() {
		double balance = 0;
		ResultSet rs = executeGet(Queries.GET_BUDGET_BALANCE);
		if (rs != null) {
			try {
				rs.next();
				balance = rs.getDouble(1);
			} catch (SQLException e) {
				LOGGER.severe(LogConfig.format("severe getting budget balance", e));
			}
		}

		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			LOGGER.severe(LogConfig.format("error closing connection", e));
		}
		
		return balance;
	}
	
	@Override
	public void addBudget(Budget b) throws Exception {
		int budgetId = executeUpdate(Queries.INSERT_BUDGET, b.getName(), b.getAllocation(), b.isTxnRequireDesc());
		double balance = 0;
		executeUpdate(Queries.INSERT_BUDGET_BALANCE, budgetId, balance);
		executeUpdate(Queries.INSERT_BUDGET_CATEGORY, budgetId, b.getCategory().getId());
	}
	
	@Override
	public void updateBalance(int budgetId, double balance) throws Exception {
		executeUpdate(Queries.UPDATE_BUDGET_BALANCE, balance, budgetId);
	}
	
	@Override
	public void updateBudget(Budget b) throws Exception {
		executeUpdate(Queries.UPDATE_BUDGET, b.getName(), b.getAllocation(), b.isTxnRequireDesc(), b.getId());
		executeUpdate(Queries.UPDATE_BUDGET_CATEGORY, b.getCategory().getId(), b.getId());
	}
	
	@Override
	public void addCategory(String name) throws Exception {
		executeUpdate(Queries.INSERT_CATEGORY, name);
	}
	
	@Override
	public void deleteCategory(int id) throws Exception {
		executeUpdate(Queries.DELETE_CATEGORY, id);
	}
	
	@Override
	public void deleteBudget(int id) throws Exception {
		executeUpdate(Queries.DELETE_BUDGET_TXNS, id);
		executeUpdate(Queries.DELETE_BUDGET_CATEGORY, id);
		executeUpdate(Queries.DELETE_BUDGET, id);
	}
	
	@Override
	public void addNote(String note) throws Exception {
		executeUpdate(Queries.INSERT_NOTE, note);
	}
	
	@Override
	public void deleteNote(int id) throws Exception {
		executeUpdate(Queries.DELETE_NOTE, id);
	}
	
	@Override
	public List<Note> getNotes() {
		List<Note> results = new ArrayList<Note>();
		ResultSet rs = executeGet(Queries.GET_NOTES);
		if (rs != null) {
			try {
				while (rs.next()) {
					results.add(new Note(rs.getInt(1),
							rs.getString(2)));
				}
			} catch (SQLException e) {
	    	  	LOGGER.severe(LogConfig.format("severe reading results", e));
	      }
		}
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			LOGGER.severe(LogConfig.format("error closing connection", e));
		}
		return results;
	}
	
	@Override
	public double getAllocatedIncome() {
		double results = 0.0;
		ResultSet rs = executeGet(Queries.GET_ALLOCATED_INCOME);
		if (rs != null) {
			try {
				while (rs.next()) {
					results = rs.getDouble(1);
				}
			} catch (SQLException e) {
	    	  	LOGGER.severe(LogConfig.format("error reading results", e));
	      }
		}
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			LOGGER.severe(LogConfig.format("error closing connection", e));
		}
		return results;
	}
	@Override
	public List<Income> getAllIncome() {
		List<Income> results = new ArrayList<Income>();
		ResultSet rs = executeGet(Queries.GET_ALL_INCOME);
		if (rs != null) {
			try {
				while (rs.next()) {
					results.add(new Income(rs.getInt(1),
							rs.getString(2),
							rs.getDouble(3),
							rs.getInt(4),
							rs.getDouble(5)));
				}
			} catch (SQLException e) {
	    	  	LOGGER.severe(LogConfig.format("error reading results", e));
	      }
		}
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			LOGGER.severe(LogConfig.format("error closing connection", e));
		}
		return results;
	}
	
	@Override
	public void insertIncome(Income i) throws Exception {
		executeUpdate(Queries.INSERT_INCOME, i.getDesc(), i.getAmount(), i.getFrequencyPerYr(), i.getMonthlyDeduction());
	}

	@Override
	public void deleteIncome(int id) throws Exception {
		executeUpdate(Queries.DELETE_INCOME, id);
	}
	
	@Override
	public void insertTransaction(Transaction t) throws Exception {
		executeUpdate(Queries.INSERT_TRANSACTION, t.getBudget().getId(),
				t.getType().getCode(),
				t.getAmount(),
				t.getDesc(),
				new Date());
		
		double balance = t.getBudget().getBalance();
		if (t.getType().equals(TransactionType.Debit)) {
			balance -= t.getAmount();
		} else {
			balance += t.getAmount();
		}
		
		executeUpdate(Queries.UPDATE_BUDGET_BALANCE, balance, t.getBudget().getId());
	}

	@Override
	public void createScheduledJob(ScheduledJob job) throws Exception {
		executeUpdate(Queries.INSERT_SCHEDULED_TXN, job.getName(),
				job.getBudget_id(),
				job.getDayToRun(),
				job.getAmount(),
				job.calculateNextRunTime());
	}
	
	@Override
	public void deleteScheduledJob(int id) throws Exception {
		executeUpdate(Queries.DELETE_SCHEDULED_TXN, id);
	}
	
	@Override
	public List<ScheduledJob> getSchedueldJobs() {
		List<ScheduledJob> results = new ArrayList<ScheduledJob>();
		ResultSet rs = executeGet(Queries.GET_SCHEDULED_TXNS);
		if (rs != null) {
			try {
				while (rs.next()) {
					Date lastRun = rs.getDate(6) == null ? null : DateHelper.fromString(rs.getString(6));
					Date nextRun = rs.getDate(7) == null ? null : DateHelper.fromString(rs.getString(7));
					results.add(new ScheduledJob(rs.getInt(1),
							rs.getString(2),
							rs.getInt(3),
							rs.getInt(4),
							rs.getDouble(5),
							lastRun,
							nextRun));
				}
			} catch (SQLException | ParseException e) {
	    	  	LOGGER.severe(LogConfig.format("error reading results", e));
	      }
		}
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			LOGGER.severe(LogConfig.format("error closing connection", e));
		}
		return results;
		
	}
	
	@Override
	public void purge(Date olderThan) throws Exception {
		LOGGER.info("Purging transactions older than 18 months...");
		int purged = executeUpdate(Queries.DELETE_OLD_TXNS, olderThan);
		LOGGER.info(LogConfig.format("Purged {} records", ((Integer)purged).toString()));
		
	}
	
	@Override
	public void insertMonthlyIncome(int scheduleId, Date nextRunTime) throws Exception {
		List<Budget> budgets = getAllBudgets();

		Connection c = openConnection();
		PreparedStatement insertTxn = null;
		PreparedStatement updateBalance = null;
		PreparedStatement updateJob = null;
		
		try {
			c.setAutoCommit(false);
			insertTxn = c.prepareStatement(Queries.INSERT_TRANSACTION);
			updateBalance = c.prepareStatement(Queries.UPDATE_BUDGET_BALANCE);
			updateJob = c.prepareStatement(Queries.UPDATE_SCHEDULED_RUN_TIME);
			
			for (Budget b : budgets) {
				insertTxn.setInt(1, b.getId());
				insertTxn.setInt(2, TransactionType.Credit.getCode());
				insertTxn.setDouble(3, b.getAllocation());
				insertTxn.setString(4, "Monthly Allocation");
				insertTxn.setString(5, DateHelper.toString(new Date()));
				insertTxn.executeUpdate();
				
				LOGGER.info(LogConfig.format("Updating balance for budget id {}", ((Integer)b.getId()).toString()));
				double balance = b.getBalance() + b.getAllocation();
				updateBalance.setDouble(1, balance);
				updateBalance.setInt(2, b.getId());
				updateBalance.executeUpdate();
			}
			LOGGER.info(LogConfig.format("Updating next run time for schedule id {}", ((Integer)scheduleId).toString()));
			updateJob.setString(1, DateHelper.toString(new Date()));
			updateJob.setString(2, DateHelper.toString(nextRunTime));
			updateJob.setInt(3, scheduleId);
			updateJob.executeUpdate();
			c.commit();
		} catch (SQLException e) {
			LOGGER.severe(LogConfig.format("Rolling back transaction!", e));
			try {
				c.rollback();
			} catch (SQLException e1) {
				LOGGER.severe(LogConfig.format("error rolling back transaction", e1));
				throw new Exception("error rolling back transaction", e1);
			}
			throw new Exception("error adding monthly income", e);
		} finally {
			try {
				if (insertTxn != null) {
					insertTxn.close();
				}
				if (updateBalance != null) {
					updateBalance.close();
				}
				if (updateJob != null) {
					updateJob.close();
				}
				if (c != null) {
					c.close();
				}
			} catch (SQLException e) {
				LOGGER.severe(LogConfig.format("error cleaning up connection", e));
			}
		}
	}
	
	@Override
	public void insertScheduledTransaction(ScheduledJob job) throws Exception {
		
		double balance = getBalanceForBudget(job.getBudget_id());
		int budgetId = job.getBudget_id();
		double amount = job.getAmount();
		
		Connection c = openConnection();
		PreparedStatement insertTxn = null;
		PreparedStatement updateBalance = null;
		PreparedStatement updateJob = null;
		
		try {
			c.setAutoCommit(false);
			insertTxn = c.prepareStatement(Queries.INSERT_TRANSACTION);
			updateBalance = c.prepareStatement(Queries.UPDATE_BUDGET_BALANCE);
			updateJob = c.prepareStatement(Queries.UPDATE_SCHEDULED_RUN_TIME);
			
			insertTxn.setInt(1, budgetId);
			insertTxn.setInt(2, TransactionType.Debit.getCode());
			insertTxn.setDouble(3, amount);
			insertTxn.setString(4, "Scheduled job: " + job.getName());
			insertTxn.setString(5, DateHelper.toString(new Date()));
			insertTxn.executeUpdate();
			
			double bal = balance - amount;
			updateBalance.setDouble(1, bal);
			updateBalance.setInt(2, budgetId);
			updateBalance.executeUpdate();
				
			updateJob.setString(1, DateHelper.toString(new Date()));
			updateJob.setString(2, DateHelper.toString(new Date()));
			updateJob.setInt(3, job.getId());
			updateJob.executeUpdate();
			c.commit();
		} catch (SQLException e) {
			try {
				c.rollback();
			} catch (SQLException e1) {
				LOGGER.severe(LogConfig.format("error rolling back transaction", e1));
				throw new Exception("error rolling back transaction", e1);
			}
			throw new Exception("error adding monthly income", e);
		} finally {
			if (insertTxn != null) {
				insertTxn.close();
			}
			if (updateBalance != null) {
				updateBalance.close();
			}
			if (updateJob != null) {
				updateJob.close();
			}
			if (c != null) {
				c.close();
			}
		}
	}
	
	@Override
	public void updateNextRunTime(ScheduledJob job) throws Exception {
		executeUpdate(Queries.UPDATE_SCHEDULED_RUN_TIME, 
				new Date(),
				job.calculateNextRunTime(),
				job.getId());
	}
	
	private double getBalanceForBudget(int budgetId) throws SQLException {
		double balance = 0;
		ResultSet rs = executeGet(Queries.GET_BALANCE_FOR_BUDGET, budgetId);
		
		try {
			if (rs != null) {
				rs.next();
				balance = rs.getDouble(1);
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		return balance;
	}
	
	private ResultSet executeGet(String query, Object... parameters) {
		Connection c = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			c = openConnection();
			stmt = c.prepareStatement(query);
			LOGGER.fine(LogConfig.format("Executing query {}", query));
			addParameters(stmt, parameters);
			rs = stmt.executeQuery();
		} catch(Exception e) {
			LOGGER.severe(LogConfig.format("Sql error: ", e));
			return null;
		}
		return rs;
	}

	private int executeUpdate(String query, Object... parameters) throws Exception {
		Connection c = null;
		PreparedStatement stmt = null;
		int id = -1;
		try {
			c = openConnection();
			stmt = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			LOGGER.fine(String.format("Executing query %s", query));
			addParameters(stmt, parameters);
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getInt(1);
			}
		} catch (SQLException | ClassNotFoundException e) {
			throw e;
		} finally {
			if (stmt != null) { 
				try {
					stmt.close();
				} catch (SQLException e) {
					LOGGER.severe(LogConfig.format("error closing prepared statement", e));
				} 
			}
			if (c != null) {
				try {
					c.close();
				} catch (SQLException e) {} 
			}
		}
		return id;
	}
	
	private void addParameters(PreparedStatement stmt, Object ...parameters) throws SQLException {
		if (parameters != null && parameters.length > 0) {
			for(int i = 0; i < parameters.length; i++) {
				LOGGER.fine(LogConfig.format("Setting parameter {}", parameters[i].toString()));
				if (parameters[i] instanceof String) {
					stmt.setString(i + 1, (String)parameters[i]);
				} else if (parameters[i] instanceof Integer) {
					stmt.setInt(i + 1, (Integer)parameters[i]);
				} else if (parameters[i] instanceof Double) {
					stmt.setDouble(i + 1, (Double)parameters[i]);
				} else if (parameters[i] instanceof Boolean) {
					stmt.setBoolean(i + 1, (Boolean)parameters[i]);
				} else if (parameters[i] instanceof Date) {
					stmt.setString(i + 1,  DateHelper.toString((Date)parameters[i]));
				}
			}
		}
	}
	
	private Connection openConnection() throws SQLException, ClassNotFoundException {
	     Connection c = null;
         Class.forName("org.sqlite.JDBC");
         c = DriverManager.getConnection("jdbc:sqlite:/Users/jmorri6/Documents/Github/budget.db");
	     return c;
	}
}
