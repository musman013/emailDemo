package com.fastcode.emaildemo.emailbuilder.application.datasource.dto;

import java.util.Date;
import java.util.List;

import com.fastcode.emaildemo.emailbuilder.domain.model.EmailTemplateEntity;

public class FindDataSourceByIdOutput {

	private Long id;

	private String name;
	
    private EmailTemplateEntity emailTemplate;
	
    private String sqlQuery;
	
	private Date creation;
	
	private List<DataSourceMetaInput> metaList;
	
	private boolean readOnlyQuery;
	
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

	public List<DataSourceMetaInput> getMetaList() {
		return metaList;
	}

	public void setMetaList(List<DataSourceMetaInput> metaList) {
		this.metaList = metaList;
	}

	public boolean isReadOnlyQuery() {
		return readOnlyQuery;
	}

	public void setReadOnlyQuery(boolean readOnlyQuery) {
		this.readOnlyQuery = readOnlyQuery;
	}

	

	
}
