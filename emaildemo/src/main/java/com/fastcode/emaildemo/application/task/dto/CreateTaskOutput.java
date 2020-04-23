package com.fastcode.emaildemo.application.task.dto;

import java.util.Date;
public class CreateTaskOutput {

    private String description;
    private Long id;
    private String status;
    private String type;
	private Long appId;
	private String appsDescriptiveField;

  public Long getAppId() {
  	return appId;
  }

  public void setAppId(Long appId){
    this.appId = appId;
  }
  
  public String getAppsDescriptiveField() {
  	return appsDescriptiveField;
  }

  public void setAppsDescriptiveField(String appsDescriptiveField){
  	this.appsDescriptiveField = appsDescriptiveField;
  }
 
  public String getDescription() {
  	return description;
  }

  public void setDescription(String description){
  	this.description = description;
  }
  
  public Long getId() {
  	return id;
  }

  public void setId(Long id){
  	this.id = id;
  }
  
  public String getStatus() {
  	return status;
  }

  public void setStatus(String status){
  	this.status = status;
  }
  
  public String getType() {
  	return type;
  }

  public void setType(String type){
  	this.type = type;
  }
  
}
