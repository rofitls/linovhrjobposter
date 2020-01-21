package com.jobposter.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="tbl_m_job_quota", uniqueConstraints = @UniqueConstraint(columnNames = {"id_jobp"}))
public class JobQuota {
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="UUID")
	@GenericGenerator(name="UUID", strategy="org.hibernate.id.UUIDGenerator")
	private String id;
	
	@ManyToOne
	@JoinColumn(name="id_jobp", referencedColumnName="id", nullable=false)
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private JobPosting jobPosting;
	
	@Column(name="quota")
	private Integer jobQuota;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public JobPosting getJobPosting() {
		return jobPosting;
	}

	public void setJobPosting(JobPosting jobPosting) {
		this.jobPosting = jobPosting;
	}

	public Integer getJobQuota() {
		return jobQuota;
	}

	public void setJobQuota(Integer jobQuota) {
		this.jobQuota = jobQuota;
	}

}
