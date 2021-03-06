package com.fastcode.emaildemo.domain.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.content.commons.annotations.ContentId;
import org.springframework.content.commons.annotations.ContentLength;
import org.springframework.content.commons.annotations.MimeType;
import org.springframework.versions.LockOwner;

@Entity
@Table(name = "file", schema = "s1")
public class File implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = true)
	private String name;

	@Column(nullable = true)
	private Date created = new Date();

	@Column(nullable = true)
	private String summary;

	@Column(nullable = true)
	@ContentId
	private String contentId;

	@Column(nullable = true)
	@ContentLength
	private long contentLength;

	@Column(nullable = true)
	@MimeType
	private String mimeType;

	@Column(nullable = true)
	private Long emailTemplateId;
	
	@Column(nullable = true)
	private Long emailVariableId;
	
	@Column
	private boolean deleted;
	

	// Copy Constructor
	public File(File file) {
		this.name = file.getName();
		this.summary = file.getSummary();
	}

	public File() {
	}

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

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public Long getEmailTemplateId() {
		return emailTemplateId;
	}

	public void setEmailTemplateId(Long emailTemplateId) {
		this.emailTemplateId = emailTemplateId;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Long getEmailVariableId() {
		return emailVariableId;
	}

	public void setEmailVariableId(Long emailVariableId) {
		this.emailVariableId = emailVariableId;
	}


	
	

}
