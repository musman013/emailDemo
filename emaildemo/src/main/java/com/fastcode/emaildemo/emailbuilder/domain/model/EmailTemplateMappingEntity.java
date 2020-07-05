package com.fastcode.emaildemo.emailbuilder.domain.model;

import java.util.Date;

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
@Table(name = "EmailTemplateMappingEntity")
public class EmailTemplateMappingEntity {
	
	
	 	private Long id;
	    private EmailTemplateEntity emailTemplateEntity;
	    private EmailVariableEntity mergeField;
	    private DataSourceEntity dataSourceEntiry;
	    private DataSourceMetaEntity dataSourceMetaEntity;
	    private Date creation;
	    
	    
	    @PrePersist
	    public void setDate()
	    {
	    	this.creation= new Date();
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

		@ManyToOne
		@JoinColumn(name="emailTemplateEntity")
		public EmailTemplateEntity getEmailTemplateEntity() {
			return emailTemplateEntity;
		}


		public void setEmailTemplateEntity(EmailTemplateEntity emailTemplateEntity) {
			this.emailTemplateEntity = emailTemplateEntity;
		}

		@ManyToOne
		@JoinColumn(name="mergeField")
		public EmailVariableEntity getMergeField() {
			return mergeField;
		}


		public void setMergeField(EmailVariableEntity mergeField) {
			this.mergeField = mergeField;
		}

		@ManyToOne
		@JoinColumn(name="dataSourceEntity")
		public DataSourceEntity getDataSourceEntiry() {
			return dataSourceEntiry;
		}


		public void setDataSourceEntiry(DataSourceEntity dataSourceEntiry) {
			this.dataSourceEntiry = dataSourceEntiry;
		}

		@ManyToOne
		@JoinColumn(name="dataSourceMetaEntity")
		public DataSourceMetaEntity getDataSourceMetaEntity() {
			return dataSourceMetaEntity;
		}


		public void setDataSourceMetaEntity(DataSourceMetaEntity dataSourceMetaEntity) {
			this.dataSourceMetaEntity = dataSourceMetaEntity;
		}


		public Date getCreation() {
			return creation;
		}


		public void setCreation(Date creation) {
			this.creation = creation;
		}
	    
	    
	    

}
