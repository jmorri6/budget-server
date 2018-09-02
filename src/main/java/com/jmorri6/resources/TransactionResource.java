package com.jmorri6.resources;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ejb.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.jmorri6.pojo.BudgetTrend;
import com.jmorri6.pojo.SearchTransactionInput;
import com.jmorri6.pojo.Transaction;
import com.jmorri6.pojo.TransactionReport;
import com.jmorri6.pojo.TrendReport;
import com.jmorri6.util.DateHelper;
import com.jmorri6.util.DbHelper;
import com.jmorri6.util.LogConfig;

@Singleton
@Path("/transactions")
public class TransactionResource {
	private static final Logger LOGGER = Logger.getLogger(TransactionResource.class.getName());
	private DbHelper dbHelper = new DbHelper();
	private Gson gson = new Gson();

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	public Response addTransaction(String json) {
		Transaction t = gson.fromJson(json, Transaction.class);
		try {
			dbHelper.insertTransaction(t);
		} catch (Exception e) {
			LOGGER.severe(LogConfig.format("Error inserting transaction", e));
			return Response.status(409).build();
		}
		
		return Response.ok().build();
	}
	
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/history")
	public Response getHistory(String json) {
		List<Transaction> results = new ArrayList<Transaction>();
		SearchTransactionInput input = gson.fromJson(json, SearchTransactionInput.class);
		LOGGER.fine(LogConfig.format("Searching for transactions for input {}", json));
		
		try {
			results = dbHelper.getTransactions(input);
		} catch (Exception e) {
			LOGGER.severe(LogConfig.format("Error searching for transactions", e));
			return Response.status(409).build();
		}
		
		return Response.ok().entity(results).build();
	}
	
	@GET
	@Path("/getSpendingReport")
	public Response getSpendingReport(@QueryParam("txnType") Integer txnType, 
			@QueryParam("groupByBudget") Boolean groupByBudget, @QueryParam("groupByMonth") Boolean groupByMonth) {
		
		if (groupByBudget == null) {
			groupByBudget = false;
		}
		if (groupByMonth == null) {
			groupByMonth = false;
		}
		
		List<Transaction> transactions = new ArrayList<>();
		
		try {
			SearchTransactionInput input = new SearchTransactionInput();
			input.setTxnType(txnType);
			transactions = dbHelper.getTransactions(input);
		} catch (Exception e) {
			LOGGER.severe(LogConfig.format("Error searching for transactions", e));
			return Response.status(409).build();
		}
		
		if (groupByBudget && groupByMonth) {
			return Response.ok().entity(getTrendReport(transactions)).build();
		} else  {
			return Response.ok().entity(getTransactionReports(transactions, groupByBudget)).build();
		}
	}
	
	private TrendReport getTrendReport(List<Transaction> transactions) {
		TrendReport report = new TrendReport();
		List<String> months = getDistinctMonths(transactions);
		report.setMonths(months);
		
		List<String> budgets = transactions.stream().map(t -> t.getBudget().getName()).distinct().collect(Collectors.toList());
		
		List<BudgetTrend> budgetAmounts = new ArrayList<>();

		for (String budget: budgets) {
			List<Double> amounts = new ArrayList<Double>();
			List<Transaction> txnsForBudget = transactions.stream()
					.filter(t -> t.getBudget().getName().equals(budget))
					.collect(Collectors.toList());
			
			for(String month : months) {
				amounts.add(txnsForBudget.stream()
						.filter(t -> month.equals(getMonthString(t.getCreated())))
						.mapToDouble(t -> t.getAmount())
						.sum());
						
			}
			budgetAmounts.add(new BudgetTrend(budget, amounts));
		}
		
		report.setBudgetAmounts(budgetAmounts);
		return report;
	}
	
	private List<TransactionReport> getTransactionReports(List<Transaction> transactions, boolean groupByBudget) {
		List<TransactionReport> results = new ArrayList<>();
		
		if (transactions != null && !transactions.isEmpty()) {
			Integer nbrOfMonths = DateHelper.getNbrOfMonths(transactions.get(0).getCreated(), new Date());
			//get overall average
			if (!groupByBudget) {
				results.add(new TransactionReport(null, getTotalAmountForTransactions(transactions) / nbrOfMonths));
			} 
			//get overall average per budget
			else {
				return getAverageAmountPerBudget(transactions, nbrOfMonths);
			}
			
		}
		
		return results;
	}
	
	private List<TransactionReport> getAverageAmountPerBudget(List<Transaction> transactions, int nbrOfMonths) {
		List<TransactionReport> results = new ArrayList<>();
		List<Integer> budgetIds = transactions.stream().map(t -> t.getBudget().getId()).distinct().collect(Collectors.toList());
		for (Integer id : budgetIds) {
			List<Transaction> transactionsForBudget = transactions.stream().filter(t -> t.getBudget().getId() == id).collect(Collectors.toList());
			String budgetName = transactionsForBudget.get(0).getBudget().getName();
			double average = getTotalAmountForTransactions(transactionsForBudget) / nbrOfMonths;
			
			results.add(new TransactionReport(budgetName, average));
		}
		return results;
	}
	
	private List<String> getDistinctMonths(List<Transaction> transactions) {
		return transactions.stream().map(t -> { 
			return getMonthString(t.getCreated());
		}).distinct().collect(Collectors.toList());
	}
	
	private String getMonthString(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		String month = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
		String year = new Integer(c.get(Calendar.YEAR)).toString();
		return month.concat(" " ).concat(year.substring(2));
	}
	
	private Double getTotalAmountForTransactions(List<Transaction> transactions) {
		return transactions.stream().mapToDouble(t -> t.getAmount()).sum();
	}
	
}
