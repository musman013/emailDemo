package com.fastcode.emaildemo.domain.task;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.fastcode.emaildemo.domain.model.TaskEntity;
import com.fastcode.emaildemo.domain.irepository.IAppsRepository;
import com.fastcode.emaildemo.domain.model.AppsEntity;
import com.fastcode.emaildemo.domain.irepository.ITaskRepository;
import com.querydsl.core.types.Predicate;

@Repository
public class TaskManager implements ITaskManager {

    @Autowired
    ITaskRepository  _taskRepository;
    
    @Autowired
	IAppsRepository  _appsRepository;
    
	public TaskEntity create(TaskEntity task) {

		return _taskRepository.save(task);
	}

	public void delete(TaskEntity task) {

		_taskRepository.delete(task);	
	}

	public TaskEntity update(TaskEntity task) {

		return _taskRepository.save(task);
	}

	public TaskEntity findById(Long taskId) {
    	Optional<TaskEntity> dbTask= _taskRepository.findById(taskId);
		if(dbTask.isPresent()) {
			TaskEntity existingTask = dbTask.get();
		    return existingTask;
		} else {
		    return null;
		}

	}

	public Page<TaskEntity> findAll(Predicate predicate, Pageable pageable) {

		return _taskRepository.findAll(predicate,pageable);
	}
  
   //Apps
	public AppsEntity getApps(Long taskId) {
		
		Optional<TaskEntity> dbTask= _taskRepository.findById(taskId);
		if(dbTask.isPresent()) {
			TaskEntity existingTask = dbTask.get();
		    return existingTask.getApps();
		} else {
		    return null;
		}
	}
}
