package com.fastcode.emaildemo.emailbuilder.application.datasource.dto;

import java.util.Date;

import com.fastcode.emaildemo.emailbuilder.domain.model.EmailTemplateEntity;

public class UpdateDataSourceOutput {

	private Long id;

	private String name;
	
    private EmailTemplateEntity emailTemplate;
	
    private String sqlQuery;
	
	private Date creation;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public EmailTemplateEntity getEmailTemplate() {
		return emailTemplate;
	}

	public void setEmailTemplate(EmailTemplateEntity emailTemplate) {
		this.emailTemplate = emailTemplate;
	}

	public String getSqlQuery() {
		return sqlQuery;
	}

	public void setSqlQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}

	public Date getCreation() {
		return creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	
}
