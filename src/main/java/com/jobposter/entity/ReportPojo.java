package com.jobposter.entity;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class ReportPojo {

	private String recruiterName;
	private Long totalUploadJob;
	private List<ReportPerJobPojo> jobList = new ArrayList<>();
	private JRBeanCollectionDataSource coursedataSource;
	
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
	public List<ReportPerJobPojo> getJobList() {
		return jobList;
	}
	public void setJobList(List<ReportPerJobPojo> jobList) {
		this.jobList = jobList;
	}
	public JRBeanCollectionDataSource getCoursedataSource() {
		return new JRBeanCollectionDataSource(jobList, false);
	}
	public void setCoursedataSource(JRBeanCollectionDataSource coursedataSource) {
		this.coursedataSource = coursedataSource;
	}
	

}
