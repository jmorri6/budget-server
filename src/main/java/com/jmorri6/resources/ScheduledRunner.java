package com.jmorri6.resources;

import java.util.Calendar;
import java.util.List;

import javax.ejb.Schedule;
import javax.ejb.Singleton;

import java.util.logging.Logger;

import com.jmorri6.pojo.ScheduledJob;
import com.jmorri6.util.DbHelper;
import com.jmorri6.util.IDbHelper;
import com.jmorri6.util.LogConfig;

@Singleton
public class ScheduledRunner {
	private static final Logger LOGGER = Logger.getLogger(ScheduledRunner.class.getName());
	private IDbHelper db = new DbHelper();
	
	@Schedule(hour="*", minute="0,30")
	public void runJobs() {	
		LOGGER.info("ScheduledRunner starting...");
		List<ScheduledJob> jobs;
		try {
			jobs = db.getSchedueldJobs();
		} catch (Exception e) {
			LOGGER.severe(LogConfig.format("Error retrieving scheduled jobs", e));
			return;
		}
		
		for (ScheduledJob job: jobs) {
			if (job.getNextRunTime() == null) {
				try {
					LOGGER.info(LogConfig.format("Setting next scheduled run time for job {}", job.getName()));
					db.updateNextRunTime(job);
				} catch (Exception e) {
					LOGGER.severe("Error updating next run time for job");
				}
			} else if (job.shouldRun()) {
				runJob(job);
			} 
		}
		purge();
	}
	
	private void runJob(ScheduledJob job) {
		if (job.getName().equals("addMonthlyIncome")) {
			insertMonthlyIncome(job);
		} else {
			try {
				LOGGER.info(LogConfig.format("Starting job {}...", job.getName()));
				db.insertScheduledTransaction(job);
			} catch (Exception e) {
				LOGGER.severe(LogConfig.format("Error running job " + job.getName(), e));
			}
			LOGGER.info(LogConfig.format("Finished {}!", job.getName()));
		}
	}
	
	private void purge() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, - 18);
		try {
			db.purge(new java.sql.Date(c.getTimeInMillis()));
		} catch (Exception e) {
			LOGGER.severe(LogConfig.format("Error during purge", e));
		}
	}
	
	private void insertMonthlyIncome(ScheduledJob job) {
		LOGGER.info(LogConfig.format("Starting job {}...", job.getName()));
		try {
			db.insertMonthlyIncome(job.getId(), new java.sql.Date(job.calculateNextRunTime().getTime()));
		} catch (Exception e) {
			LOGGER.severe(LogConfig.format(LogConfig.format("{} had an error", job.getName()), e));
		}

		LOGGER.info(LogConfig.format("Finished {}!", job.getName()));
	}

}
