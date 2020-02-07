package com.jobposter.entity;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class ReportMasterPojo {

	private String recruiterName;
	private Long totalUploadJob;
	private String jobPosting;
	private String year;
	private Long countHire;
	private Long countInterview;
	private Long countApplicant;
	
	public String getRecruiterName() {
		return recruiterName;
	}
	public void setRecruiterName(String recruiterName) {
		this.recruiterName = recruiterName;
	}
	public Long getTotalUploadJob() {
		return totalUploadJob;
	}
	public void setTotalUploadJob(Long totalUploadJob) {
		this.totalUploadJob = totalUploadJob;
	}
	public String getJobPosting() {
		return jobPosting;
	}
	public void setJobPosting(String jobPosting) {
		this.jobPosting = jobPosting;
	}
	public Long getCountHire() {
		return countHire;
	}
	public void setCountHire(Long countHire) {
		this.countHire = countHire;
	}
	public Long getCountInterview() {
		return countInterview;
	}
	public void setCountInterview(Long countInterview) {
		this.countInterview = countInterview;
	}
	public Long getCountApplicant() {
		return countApplicant;
	}
	public void setCountApplicant(Long countApplicant) {
		this.countApplicant = countApplicant;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
}
