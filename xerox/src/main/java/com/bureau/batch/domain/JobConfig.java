package com.bureau.batch.domain;

import java.io.Serializable;

public class JobConfig implements Serializable {

    private static final long serialVersionUID = 1L;
    private int dayOfMonth;
    private int month;
    private int year;
    private int hour;
    private int minute;
    private String jobName;
    private String jobProcessorParameter;
    private String codigoRegistro;

    public JobConfig() {
        super();
    }

	public int getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobProcessorParameter() {
		return jobProcessorParameter;
	}

	public void setJobProcessorParameter(String jobProcessorParameter) {
		this.jobProcessorParameter = jobProcessorParameter;
	}

	public String getCodigoRegistro() {
		return codigoRegistro;
	}

	public void setCodigoRegistro(String codigoRegistro) {
		this.codigoRegistro = codigoRegistro;
	}
}
