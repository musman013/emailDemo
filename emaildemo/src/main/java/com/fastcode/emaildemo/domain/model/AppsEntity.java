package com.fastcode.emaildemo.domain.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;

@Entity
@Table(name = "apps", schema = "s1")
public class AppsEntity implements Serializable {

  	private String artifactId;
  	private String authMethod;
  	private String authTable;
  	private Boolean caching;
  	private Date createdDate;
  	private String description;
  	private String destinationPath;
  	private Boolean emailTemplates;
  	private Boolean entityHistory;
  	private String groupId;
	private Long id;
  	private String jdbcPassword;
  	private String jdbcUrl;
  	private String jdbcUsername;
  	private String name;
  	private Boolean processAdminApp;
  	private Boolean processManagement;
  	private Boolean scheduler;
  	private String schema;
  	private Boolean upgrade;
	private Long userId;
  	private Boolean userOnly;
 
  	public AppsEntity() {
  	}

  	@Basic
  	@Column(name = "artifactId", nullable = false, length =30)
  	public String getArtifactId() {
  		return artifactId;
  	}

  	public void setArtifactId(String artifactId) {
  		this.artifactId = artifactId;
  	}
  
  	@Basic
  	@Column(name = "authMethod", nullable = true, length =20)
  	public String getAuthMethod() {
  		return authMethod;
  	}

  	public void setAuthMethod(String authMethod) {
  		this.authMethod = authMethod;
  	}
  
  	@Basic
  	@Column(name = "authTable", nullable = true, length =255)
  	public String getAuthTable() {
  		return authTable;
  	}

  	public void setAuthTable(String authTable) {
  		this.authTable = authTable;
  	}
  
  	@Basic
  	@Column(name = "caching", nullable = true)
  	public Boolean getCaching() {
  		return caching;
  	}

	public void setCaching(Boolean caching) {
  		this.caching = caching;
	}
  
  	@Basic
  	@Column(name = "createdDate", nullable = true)
  	public Date getCreatedDate() {
  		return createdDate;
  	}

  	public void setCreatedDate(Date createdDate) {
  		this.createdDate = createdDate;
  	}
  
  	@Basic
  	@Column(name = "description", nullable = true, length =500)
  	public String getDescription() {
  		return description;
  	}

  	public void setDescription(String description) {
  		this.description = description;
  	}
  
  	@Basic
  	@Column(name = "destinationPath", nullable = false, length =30)
  	public String getDestinationPath() {
  		return destinationPath;
  	}

  	public void setDestinationPath(String destinationPath) {
  		this.destinationPath = destinationPath;
  	}
  
  	@Basic
  	@Column(name = "emailTemplates", nullable = true)
  	public Boolean getEmailTemplates() {
  		return emailTemplates;
  	}

	public void setEmailTemplates(Boolean emailTemplates) {
  		this.emailTemplates = emailTemplates;
	}
  
  	@Basic
  	@Column(name = "entityHistory", nullable = true)
  	public Boolean getEntityHistory() {
  		return entityHistory;
  	}

	public void setEntityHistory(Boolean entityHistory) {
  		this.entityHistory = entityHistory;
	}
  
  	@Basic
  	@Column(name = "groupId", nullable = false, length =30)
  	public String getGroupId() {
  		return groupId;
  	}

  	public void setGroupId(String groupId) {
  		this.groupId = groupId;
  	}
  
  	@Id
  	@GeneratedValue(strategy = GenerationType.IDENTITY)
  	@Column(name = "id", nullable = false)
  	public Long getId() {
  		return id;
  	}

  	public void setId(Long id) {
  		this.id = id;
  	}
  
  	@Basic
  	@Column(name = "jdbcPassword", nullable = false, length =255)
  	public String getJdbcPassword() {
  		return jdbcPassword;
  	}

  	public void setJdbcPassword(String jdbcPassword) {
  		this.jdbcPassword = jdbcPassword;
  	}
  
  	@Basic
  	@Column(name = "jdbcUrl", nullable = false, length =255)
  	public String getJdbcUrl() {
  		return jdbcUrl;
  	}

  	public void setJdbcUrl(String jdbcUrl) {
  		this.jdbcUrl = jdbcUrl;
  	}
  
  	@Basic
  	@Column(name = "jdbcUsername", nullable = false, length =255)
  	public String getJdbcUsername() {
  		return jdbcUsername;
  	}

  	public void setJdbcUsername(String jdbcUsername) {
  		this.jdbcUsername = jdbcUsername;
  	}
  
  	@Basic
  	@Column(name = "name", nullable = true, length =255)
  	public String getName() {
  		return name;
  	}

  	public void setName(String name) {
  		this.name = name;
  	}
  
  	@Basic
  	@Column(name = "processAdminApp", nullable = true)
  	public Boolean getProcessAdminApp() {
  		return processAdminApp;
  	}

	public void setProcessAdminApp(Boolean processAdminApp) {
  		this.processAdminApp = processAdminApp;
	}
  
  	@Basic
  	@Column(name = "processManagement", nullable = true)
  	public Boolean getProcessManagement() {
  		return processManagement;
  	}

	public void setProcessManagement(Boolean processManagement) {
  		this.processManagement = processManagement;
	}
  
  	@Basic
  	@Column(name = "scheduler", nullable = true)
  	public Boolean getScheduler() {
  		return scheduler;
  	}

	public void setScheduler(Boolean scheduler) {
  		this.scheduler = scheduler;
	}
  
  	@Basic
  	@Column(name = "schema", nullable = true, length =20)
  	public String getSchema() {
  		return schema;
  	}

  	public void setSchema(String schema) {
  		this.schema = schema;
  	}
  
  	@OneToMany(mappedBy = "apps", cascade = CascadeType.ALL) 
  	public Set<TaskEntity> getTaskSet() { 
    	return taskSet; 
  	} 
 
  	public void setTaskSet(Set<TaskEntity> task) { 
    	this.taskSet = task; 
  	} 
 
  	private Set<TaskEntity> taskSet = new HashSet<TaskEntity>(); 
  	@Basic
  	@Column(name = "upgrade", nullable = true)
  	public Boolean getUpgrade() {
  		return upgrade;
  	}

	public void setUpgrade(Boolean upgrade) {
  		this.upgrade = upgrade;
	}
  
  	@Basic
  	@Column(name = "userId", nullable = true)
  	public Long getUserId() {
  		return userId;
  	}

  	public void setUserId(Long userId) {
  		this.userId = userId;
  	}
  
  	@Basic
  	@Column(name = "userOnly", nullable = true)
  	public Boolean getUserOnly() {
  		return userOnly;
  	}

	public void setUserOnly(Boolean userOnly) {
  		this.userOnly = userOnly;
	}
  

//  @Override
//  public boolean equals(Object o) {
//    if (this == o) return true;
//      if (!(o instanceof AppsEntity)) return false;
//        AppsEntity apps = (AppsEntity) o;
//        return id != null && id.equals(apps.id);
//  }

}

  
      


