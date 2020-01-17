package com.jobposter.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="tbl_m_skill_level", uniqueConstraints = @UniqueConstraint(columnNames = {"skill_level_code"}))
public class SkillLevel {

	@Id
	@Column(name="id")
	@GeneratedValue(generator="UUID")
	@GenericGenerator(name="UUID", strategy="org.hibernate.id.UUIDGenerator")
	private String id;
	
	@Column(name="skill_level_code")
	private String skillLevelCode;
	
	@Column(name="skill_level_name")
	private String skillLevelName;
	
	@Column(name="active_state")
	private boolean activeState;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSkillLevelCode() {
		return skillLevelCode;
	}

	public void setSkillLevelCode(String skillLevelCode) {
		this.skillLevelCode = skillLevelCode;
	}

	public String getSkillLevelName() {
		return skillLevelName;
	}

	public void setSkillLevelName(String skillLevelName) {
		this.skillLevelName = skillLevelName;
	}

	public boolean isActiveState() {
		return activeState;
	}

	public void setActiveState(boolean activeState) {
		this.activeState = activeState;
	}
	
	
}
