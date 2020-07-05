package com.fastcode.emaildemo.emailbuilder.application.datasource.dto;

import com.fastcode.emaildemo.emailbuilder.domain.model.EmailTemplateEntity;

public class FindDataSourceOutputForMapping {

	
	private Long id;

	private String name;
	
    private String sqlQuery;

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

	public String getSqlQuery() {
		return sqlQuery;
	}

	public void setSqlQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}
    
    
    
}
