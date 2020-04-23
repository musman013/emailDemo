package com.fastcode.emaildemo.application.apps.dto;

import java.util.Date;
public class UpdateAppsOutput {

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

  public String getArtifactId() {
  	return artifactId;
  }

  public void setArtifactId(String artifactId){
  	this.artifactId = artifactId;
  }
  
  public String getAuthMethod() {
  	return authMethod;
  }

  public void setAuthMethod(String authMethod){
  	this.authMethod = authMethod;
  }
  
  public String getAuthTable() {
  	return authTable;
  }

  public void setAuthTable(String authTable){
  	this.authTable = authTable;
  }
  
  public Boolean getCaching() {
  	return caching;
  }

  public void setCaching(Boolean caching){
  	this.caching = caching;
  }
  
  public Date getCreatedDate() {
  	return createdDate;
  }

  public void setCreatedDate(Date createdDate){
  	this.createdDate = createdDate;
  }
  
  public String getDescription() {
  	return description;
  }

  public void setDescription(String description){
  	this.description = description;
  }
  
  public String getDestinationPath() {
  	return destinationPath;
  }

  public void setDestinationPath(String destinationPath){
  	this.destinationPath = destinationPath;
  }
  
  public Boolean getEmailTemplates() {
  	return emailTemplates;
  }

  public void setEmailTemplates(Boolean emailTemplates){
  	this.emailTemplates = emailTemplates;
  }
  
  public Boolean getEntityHistory() {
  	return entityHistory;
  }

  public void setEntityHistory(Boolean entityHistory){
  	this.entityHistory = entityHistory;
  }
  
  public String getGroupId() {
  	return groupId;
  }

  public void setGroupId(String groupId){
  	this.groupId = groupId;
  }
  
  public Long getId() {
  	return id;
  }

  public void setId(Long id){
  	this.id = id;
  }
  
  public String getJdbcPassword() {
  	return jdbcPassword;
  }

  public void setJdbcPassword(String jdbcPassword){
  	this.jdbcPassword = jdbcPassword;
  }
  
  public String getJdbcUrl() {
  	return jdbcUrl;
  }

  public void setJdbcUrl(String jdbcUrl){
  	this.jdbcUrl = jdbcUrl;
  }
  
  public String getJdbcUsername() {
  	return jdbcUsername;
  }

  public void setJdbcUsername(String jdbcUsername){
  	this.jdbcUsername = jdbcUsername;
  }
  
  public String getName() {
  	return name;
  }

  public void setName(String name){
  	this.name = name;
  }
  
  public Boolean getProcessAdminApp() {
  	return processAdminApp;
  }

  public void setProcessAdminApp(Boolean processAdminApp){
  	this.processAdminApp = processAdminApp;
  }
  
  public Boolean getProcessManagement() {
  	return processManagement;
  }

  public void setProcessManagement(Boolean processManagement){
  	this.processManagement = processManagement;
  }
  
  public Boolean getScheduler() {
  	return scheduler;
  }

  public void setScheduler(Boolean scheduler){
  	this.scheduler = scheduler;
  }
  
  public String getSchema() {
  	return schema;
  }

  public void setSchema(String schema){
  	this.schema = schema;
  }
  
  public Boolean getUpgrade() {
  	return upgrade;
  }

  public void setUpgrade(Boolean upgrade){
  	this.upgrade = upgrade;
  }
  
  public Long getUserId() {
  	return userId;
  }

  public void setUserId(Long userId){
  	this.userId = userId;
  }
  
  public Boolean getUserOnly() {
  	return userOnly;
  }

  public void setUserOnly(Boolean userOnly){
  	this.userOnly = userOnly;
  }
  

}