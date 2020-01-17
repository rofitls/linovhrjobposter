package com.jobposter.entity;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class Mail {

    private String from;
    private String to;
    private String name;
    private String subject;
    private String content;
    private Map<String, String> model;
    
    public Mail() {}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Map<String, String> getModel() {
		return model;
	}
	public void setModel(Map<String, String> model) {
		this.model = model;
	}
    
    
}

