package com.fastcode.emaildemo.domain.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;

@Entity
@Table(name = "task", schema = "s1")
public class TaskEntity implements Serializable {

  	private String description;
	private Long id;
  	private String status;
  	private String type;
 
  	public TaskEntity() {
  	}

  	@ManyToOne
  	@JoinColumn(name = "appId")
  	public AppsEntity getApps() {
    	return apps;
  	}
  	public void setApps(AppsEntity apps) {
    	this.apps = apps;
  	}
  
  	private AppsEntity apps;
 
  	@Basic
  	@Column(name = "description", nullable = true, length =2147483647)
  	public String getDescription() {
  		return description;
  	}

  	public void setDescription(String description) {
  		this.description = description;
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
  	@Column(name = "status", nullable = true, length =2147483647)
  	public String getStatus() {
  		return status;
  	}

  	public void setStatus(String status) {
  		this.status = status;
  	}
  
  	@Basic
  	@Column(name = "type", nullable = true, length =2147483647)
  	public String getType() {
  		return type;
  	}

  	public void setType(String type) {
  		this.type = type;
  	}
  

//  @Override
//  public boolean equals(Object o) {
//    if (this == o) return true;
//      if (!(o instanceof TaskEntity)) return false;
//        TaskEntity task = (TaskEntity) o;
//        return id != null && id.equals(task.id);
//  }

}

  
      


