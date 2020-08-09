package com.fastcode.emaildemo.domain.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "email_history", schema = "s1")
public class EmailHistory {

	private Long id;
	private String body;
	private String to;
	private String cc;
	private String bcc;
	private String subject;

	public EmailHistory(Long emailTemplateId) {
		this.id = emailTemplateId;
	}

	public EmailHistory() {
	}

	@Id
	@Column(name = "Id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "body", nullable = true, length = 32768)
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Basic
	@Column(name = "To_", nullable = false, length = 256)
	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	@Basic
	@Column(name = "Cc", nullable = true, length = 256)
	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	@Basic
	@Column(name = "Bcc", nullable = true, length = 256)
	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	@Basic
	@Column(name = "Subject", nullable = true, length = 256)
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

}
