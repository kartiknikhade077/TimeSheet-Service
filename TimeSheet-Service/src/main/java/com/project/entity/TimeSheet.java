package com.project.entity;

import java.sql.Time;
import java.time.LocalDate;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class TimeSheet {

	@Id
	@UuidGenerator
	private String timeSheetId;
	private String companyId;
	private String employeeId;
	private int itemNumber;
	private String workOrderNo;
	private String designerName; //employeeName or staff name
	private Time startTime;
	private Time endTime;
	private double totalTime;
	private String remarks;
	private LocalDate createDate;
	public TimeSheet(String timeSheetId, String companyId, String employeeId, int itemNumber, String workOrderNo,
			String designerName, Time startTime, Time endTime, double totalTime, String remarks) {
		super();
		this.timeSheetId = timeSheetId;
		this.companyId = companyId;
		this.employeeId = employeeId;
		this.itemNumber = itemNumber;
		this.workOrderNo = workOrderNo;
		this.designerName = designerName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.totalTime = totalTime;
		this.remarks = remarks;
	}
	public TimeSheet() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getTimeSheetId() {
		return timeSheetId;
	}
	public void setTimeSheetId(String timeSheetId) {
		this.timeSheetId = timeSheetId;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public int getItemNumber() {
		return itemNumber;
	}
	public void setItemNumber(int itemNumber) {
		this.itemNumber = itemNumber;
	}
	public String getWorkOrderNo() {
		return workOrderNo;
	}
	public void setWorkOrderNo(String workOrderNo) {
		this.workOrderNo = workOrderNo;
	}
	public String getDesignerName() {
		return designerName;
	}
	public void setDesignerName(String designerName) {
		this.designerName = designerName;
	}
	public Time getStartTime() {
		return startTime;
	}
	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}
	public Time getEndTime() {
		return endTime;
	}
	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}
	public double getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(double totalTime) {
		this.totalTime = totalTime;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public LocalDate getCreateDate() {
		return createDate;
	}
	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}
	
	
}