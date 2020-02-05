package com.jobposter.entity;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class ReportMasterPojo {

	private String recruiterName;
	private Long totalUploadJob;
	private List<ReportSubReportPojo> jobList = new ArrayList<>();
	
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
	public List<ReportSubReportPojo> getJobList() {
		return jobList;
	}
	public void setJobList(List<ReportSubReportPojo> jobList) {
		this.jobList = jobList;
	}

}
