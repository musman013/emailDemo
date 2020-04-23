package com.fastcode.emaildemo.application.task;

import com.fastcode.emaildemo.application.task.dto.*;
import com.fastcode.emaildemo.domain.task.ITaskManager;
import com.fastcode.emaildemo.domain.model.QTaskEntity;
import com.fastcode.emaildemo.domain.model.TaskEntity;
import com.fastcode.emaildemo.domain.apps.AppsManager;
import com.fastcode.emaildemo.domain.model.AppsEntity;
import com.fastcode.emaildemo.commons.search.*;
import com.fastcode.emaildemo.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

@Service
@Validated
public class TaskAppService implements ITaskAppService {

    static final int case1=1;
	static final int case2=2;
	static final int case3=3;
	
	@Autowired
	private ITaskManager _taskManager;

    @Autowired
	private AppsManager _appsManager;
	@Autowired
	private TaskMapper mapper;
	
	@Autowired
	private LoggingHelper logHelper;

    @Transactional(propagation = Propagation.REQUIRED)
	public CreateTaskOutput create(CreateTaskInput input) {

		TaskEntity task = mapper.createTaskInputToTaskEntity(input);
	  	if(input.getAppId()!=null) {
			AppsEntity foundApps = _appsManager.findById(input.getAppId());
			if(foundApps!=null) {
				task.setApps(foundApps);
			}
		}
		TaskEntity createdTask = _taskManager.create(task);
		
		return mapper.taskEntityToCreateTaskOutput(createdTask);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public UpdateTaskOutput update(Long  taskId, UpdateTaskInput input) {

		TaskEntity task = mapper.updateTaskInputToTaskEntity(input);
	  	if(input.getAppId()!=null) {
			AppsEntity foundApps = _appsManager.findById(input.getAppId());
			if(foundApps!=null) {
				task.setApps(foundApps);
			}
		}
		
		TaskEntity updatedTask = _taskManager.update(task);
		
		return mapper.taskEntityToUpdateTaskOutput(updatedTask);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Long taskId) {

		TaskEntity existing = _taskManager.findById(taskId) ; 
		_taskManager.delete(existing);
		
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindTaskByIdOutput findById(Long taskId) {

		TaskEntity foundTask = _taskManager.findById(taskId);
		if (foundTask == null)  
			return null ; 
 	   
 	    FindTaskByIdOutput output=mapper.taskEntityToFindTaskByIdOutput(foundTask); 
		return output;
	}
    //Apps
	// ReST API Call - GET /task/1/apps
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	public GetAppsOutput getApps(Long taskId) {

		TaskEntity foundTask = _taskManager.findById(taskId);
		if (foundTask == null) {
			logHelper.getLogger().error("There does not exist a task wth a id=%s", taskId);
			return null;
		}
		AppsEntity re = _taskManager.getApps(taskId);
		return mapper.appsEntityToGetAppsOutput(re, foundTask);
	}
	
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<FindTaskByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<TaskEntity> foundTask = _taskManager.findAll(search(search), pageable);
		List<TaskEntity> taskList = foundTask.getContent();
		Iterator<TaskEntity> taskIterator = taskList.iterator(); 
		List<FindTaskByIdOutput> output = new ArrayList<>();

		while (taskIterator.hasNext()) {
			output.add(mapper.taskEntityToFindTaskByIdOutput(taskIterator.next()));
		}
		return output;
	}
	
	public BooleanBuilder search(SearchCriteria search) throws Exception {

		QTaskEntity task= QTaskEntity.taskEntity;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(task, map,search.getJoinColumns());
		}
		return null;
	}
	
	public void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
			if(!(
				list.get(i).replace("%20","").trim().equals("appId") ||
				list.get(i).replace("%20","").trim().equals("apps") ||
				list.get(i).replace("%20","").trim().equals("description") ||
				list.get(i).replace("%20","").trim().equals("id") ||
				list.get(i).replace("%20","").trim().equals("status") ||
				list.get(i).replace("%20","").trim().equals("type")
			)) 
			{
			 throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
			}
		}
	}
	
	public BooleanBuilder searchKeyValuePair(QTaskEntity task, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();
        
		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
            if(details.getKey().replace("%20","").trim().equals("description")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(task.description.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(task.description.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(task.description.ne(details.getValue().getSearchValue()));
			}
            if(details.getKey().replace("%20","").trim().equals("status")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(task.status.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(task.status.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(task.status.ne(details.getValue().getSearchValue()));
			}
            if(details.getKey().replace("%20","").trim().equals("type")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(task.type.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(task.type.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(task.type.ne(details.getValue().getSearchValue()));
			}
		}
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
        if(joinCol != null && joinCol.getKey().equals("appId")) {
		    builder.and(task.apps.id.eq(Long.parseLong(joinCol.getValue())));
		}
        }
		return builder;
	}
	
	
    
	
}


