package com.jobposter.entity;

public class FilterJob {

	private String provinceName;
	private String jobCategoryName;
	private Double salaryMin;
	private Double salaryMax;
	
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getJobCategoryName() {
		return jobCategoryName;
	}
	public void setJobCategoryName(String jobCategoryName) {
		this.jobCategoryName = jobCategoryName;
	}
	public Double getSalaryMin() {
		return salaryMin;
	}
	public void setSalaryMin(Double salaryMin) {
		this.salaryMin = salaryMin;
	}
	public Double getSalaryMax() {
		return salaryMax;
	}
	public void setSalaryMax(Double salaryMax) {
		this.salaryMax = salaryMax;
	}
	
	
}
