package com.jmorri6.pojo;

import java.util.Calendar;
import java.util.Date;

public class ScheduledJob {
	private int id;
	private String name;
	private int budget_id;
	private int dayToRun;
	private double amount;
	private Date lastRunTime;
	private Date nextRunTime;
	
	public ScheduledJob(int id, String name, int budget_id,
			int dayToRun, double amount, Date lastRunTime, Date nextRunTime) {
		this.id = id;
		this.name = name;
		this.budget_id = budget_id;
		this.dayToRun = dayToRun;
		this.amount = amount;
		this.lastRunTime = lastRunTime;
		this.nextRunTime = nextRunTime;
	}
	
	public boolean shouldRun() {
		return new Date().after(nextRunTime);
	}
	
	public Date calculateNextRunTime() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 1);
		c.set(Calendar.DAY_OF_MONTH, this.dayToRun);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 1);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public int getBudget_id() {
		return budget_id;
	}
	public int getDayToRun() {
		return dayToRun;
	}
	public double getAmount() {
		return amount;
	}
	
	public Date getLastRunTime() {
		return lastRunTime;
	}
	
	public Date getNextRunTime() {
		return nextRunTime;
	}
	
	@Override
	public String toString() {
		return "Name: ".concat(getName()).concat(" amount: ").concat(((Double)getAmount()).toString());
	}
}
