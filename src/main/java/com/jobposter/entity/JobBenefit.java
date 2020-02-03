package com.jobposter.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="tbl_m_job_benefit")
public class JobBenefit {

	@Id
	@Column(name="id")
	@GeneratedValue(generator="UUID")
	@GenericGenerator(name="UUID", strategy="org.hibernate.id.UUIDGenerator")
	private String id;
	
	@Column(name="job_benefit_code")
	private String jobBenefitCode;
	
	@Column(name="job_benefit_name")
	private String jobBenefitName;
	
	@ManyToOne
	@JoinColumn(name="id_jobp", referencedColumnName="id", nullable=false)
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private JobPosting jobPosting;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJobBenefitCode() {
		return jobBenefitCode;
	}

	public void setJobBenefitCode(String jobBenefitCode) {
		this.jobBenefitCode = jobBenefitCode;
	}

	public String getJobBenefitName() {
		return jobBenefitName;
	}

	public void setJobBenefitName(String jobBenefitName) {
		this.jobBenefitName = jobBenefitName;
	}

	public JobPosting getJobPosting() {
		return jobPosting;
	}

	public void setJobPosting(JobPosting jobPosting) {
		this.jobPosting = jobPosting;
	}
	
	
}
