package com.bureau.batch.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import com.everis.enums.FormatoFecha;
import com.everis.util.FechaUtil;

public class JobInstanceExecution implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long jobInstanceId;
	private String jobName;
	private Long jobExecutionId;
	private Timestamp createTime;
	private Timestamp startTime;
	private Timestamp endTime;
	private String status;
	private String exitCode;
	private String exitMessage;
	private String createTimeTemp;

	public Long getJobInstanceId() {
		return jobInstanceId;
	}

	public void setJobInstanceId(Long jobInstanceId) {
		this.jobInstanceId = jobInstanceId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public Long getJobExecutionId() {
		return jobExecutionId;
	}

	public void setJobExecutionId(Long jobExecutionId) {
		this.jobExecutionId = jobExecutionId;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getExitCode() {
		return exitCode;
	}

	public void setExitCode(String exitCode) {
		this.exitCode = exitCode;
	}

	public String getExitMessage() {
		return exitMessage;
	}

	public void setExitMessage(String exitMessage) {
		this.exitMessage = exitMessage;
	}

	public String getCreateTimeTemp() {
		return createTimeTemp;
	}

	public void setCreateTimeTemp(String createTimeTemp) {
		this.createTimeTemp = createTimeTemp;
		this.createTime = new Timestamp(FechaUtil.parseFecha(createTimeTemp, FormatoFecha.DDMMYYYY_WITH_SEPARATOR).getTime());
	}

}
