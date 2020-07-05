package com.fastcode.emaildemo.emailbuilder.domain.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name = "DataSource")
public class DataSourceEntity {

	private Long id;
	
	private String name;
	
	private EmailTemplateEntity emailTemplate;
	
	private String sqlQuery;
	
	private Date creation;

	  public DataSourceEntity(Long dataSourceId) {
		this.id=dataSourceId;
	}
	  public DataSourceEntity() {
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
	@Column(name = "Name", nullable = false ,length = 256)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne
	@JoinColumn(name="EmailTemplate")
	public EmailTemplateEntity getEmailTemplate() {
		return emailTemplate;
	}

	public void setEmailTemplate(EmailTemplateEntity emailTemplate) {
		this.emailTemplate = emailTemplate;
	}

	@Basic
	@Column(name = "SqlQuery", nullable = false ,length = 256)
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
	
	
	@PrePersist
	public void setDates()
	{
		this.creation = new Date();
	}
	
}
