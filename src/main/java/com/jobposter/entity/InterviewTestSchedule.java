package com.jobposter.entity;

import java.sql.Time;
import java.util.Date;

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
@Table(name="tbl_m_interview_test_schedule",  uniqueConstraints = @UniqueConstraint(columnNames = {"interview_code"}))
public class InterviewTestSchedule {

	@Id
	@Column(name="id")
	@GeneratedValue(generator="UUID")
	@GenericGenerator(name="UUID", strategy="org.hibernate.id.UUIDGenerator")
	private String id;

	@Column(name="interview_code")
	private String interviewCode;
	
	@Column(name="interview_date")
	private Date interviewDate;
	
	@Column(name="interview_time")
	private Time interviewTime;
	
	@Column(name="interview_result", nullable=true)
	private String interviewResult;
	
	@Column(name="is_attend", nullable=true)
	private boolean attend;
	
	@Column(name="is_reschedule", nullable=true)
	private boolean reschedule;
	
	@Column(name="is_reject", nullable=true)
	private boolean reject;
	
	@Column(name="reschedule_reason", nullable=true)
	private String rescheduleReason;
	
	@Column(name="reject_reason", nullable=true)
	private String rejectReason;
	
	@ManyToOne
	@JoinColumn(name="id_application", referencedColumnName="id", nullable=false)
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Application application;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInterviewCode() {
		return interviewCode;
	}

	public void setInterviewCode(String interviewCode) {
		this.interviewCode = interviewCode;
	}

	public Date getInterviewDate() {
		return interviewDate;
	}

	public void setInterviewDate(Date interviewDate) {
		this.interviewDate = interviewDate;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public Time getInterviewTime() {
		return interviewTime;
	}

	public void setInterviewTime(Time interviewTime) {
		this.interviewTime = interviewTime;
	}

	public String getInterviewResult() {
		return interviewResult;
	}

	public void setInterviewResult(String interviewResult) {
		this.interviewResult = interviewResult;
	}

	public boolean isReschedule() {
		return reschedule;
	}

	public void setReschedule(boolean reschedule) {
		this.reschedule = reschedule;
	}

	public String getRescheduleReason() {
		return rescheduleReason;
	}

	public void setRescheduleReason(String rescheduleReason) {
		this.rescheduleReason = rescheduleReason;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public boolean isAttend() {
		return attend;
	}

	public void setAttend(boolean attend) {
		this.attend = attend;
	}

	public boolean isReject() {
		return reject;
	}

	public void setReject(boolean reject) {
		this.reject = reject;
	}
	
	

}
