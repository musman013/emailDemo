package com.fastcode.emaildemo.application.apps.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class CreateAppsInput {

  @NotNull(message = "artifactId Should not be null")
  @Length(max = 30, message = "artifactId must be less than 30 characters")
  private String artifactId;
  
  @Length(max = 20, message = "authMethod must be less than 20 characters")
  private String authMethod;
  
  @Length(max = 255, message = "authTable must be less than 255 characters")
  private String authTable;
  
  private Boolean caching;
  
  private Date createdDate;
  
  @Length(max = 500, message = "description must be less than 500 characters")
  private String description;
  
  @NotNull(message = "destinationPath Should not be null")
  @Length(max = 30, message = "destinationPath must be less than 30 characters")
  private String destinationPath;
  
  private Boolean emailTemplates;
  
  private Boolean entityHistory;
  
  @NotNull(message = "groupId Should not be null")
  @Length(max = 30, message = "groupId must be less than 30 characters")
  private String groupId;
  
  @NotNull(message = "jdbcPassword Should not be null")
  @Length(max = 255, message = "jdbcPassword must be less than 255 characters")
  private String jdbcPassword;
  
  @NotNull(message = "jdbcUrl Should not be null")
  @Length(max = 255, message = "jdbcUrl must be less than 255 characters")
  private String jdbcUrl;
  
  @NotNull(message = "jdbcUsername Should not be null")
  @Length(max = 255, message = "jdbcUsername must be less than 255 characters")
  private String jdbcUsername;
  
  private String name;
  
  private Boolean processAdminApp;
  
  private Boolean processManagement;
  
  private Boolean scheduler;
  
  @Length(max = 20, message = "schema must be less than 20 characters")
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
