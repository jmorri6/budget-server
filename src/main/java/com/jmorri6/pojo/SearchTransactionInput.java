package com.jmorri6.pojo;

public class SearchTransactionInput {
	private String fromDate;
	private String toDate;
	private Integer budgetId;
	private Integer txnType;
	
	public void setTxnType(Integer txnType) {
		this.txnType = txnType;
	}
	public Integer getTxnType() {
		return txnType;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public Integer getBudgetId() {
		return budgetId;
	}
	public void setBudgetId(Integer budgetId) {
		this.budgetId = budgetId;
	}
}
