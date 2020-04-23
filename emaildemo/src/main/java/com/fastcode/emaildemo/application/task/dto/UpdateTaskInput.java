package com.fastcode.emaildemo.application.task.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class UpdateTaskInput {

  @Length(max = 2147483647, message = "description must be less than 2147483647 characters")
  private String description;
  @NotNull(message = "id Should not be null")
  private Long id;
  @Length(max = 2147483647, message = "status must be less than 2147483647 characters")
  private String status;
  @Length(max = 2147483647, message = "type must be less than 2147483647 characters")
  private String type;
  private Long appId;

  public Long getAppId() {
  	return appId;
  }

  public void setAppId(Long appId){
  	this.appId = appId;
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
