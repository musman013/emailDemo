package com.fastcode.emaildemo.restcontrollers;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.fastcode.emaildemo.commons.search.SearchCriteria;
import com.fastcode.emaildemo.commons.search.SearchUtils;
import com.fastcode.emaildemo.commons.application.OffsetBasedPageRequest;
import com.fastcode.emaildemo.commons.domain.EmptyJsonResponse;
import com.fastcode.emaildemo.application.task.TaskAppService;
import com.fastcode.emaildemo.application.task.dto.*;
import com.fastcode.emaildemo.application.apps.AppsAppService;
import java.util.List;
import java.util.Map;
import com.fastcode.emaildemo.commons.logging.LoggingHelper;

@RestController
@RequestMapping("/task")
public class TaskController {

	@Autowired
	private TaskAppService _taskAppService;
    
    @Autowired
	private AppsAppService  _appsAppService;

	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private Environment env;
	
	
    
    public TaskController(TaskAppService taskAppService, AppsAppService appsAppService,
	 LoggingHelper helper) {
		super();
		this._taskAppService = taskAppService;
    	this._appsAppService = appsAppService;
		this.logHelper = helper;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<CreateTaskOutput> create(@RequestBody @Valid CreateTaskInput task) {
		CreateTaskOutput output=_taskAppService.create(task);
		return new ResponseEntity(output, HttpStatus.OK);
	}
   
	// ------------ Delete task ------------
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String id) {
    FindTaskByIdOutput output = _taskAppService.findById(Long.valueOf(id));
	if (output == null) {
		logHelper.getLogger().error("There does not exist a task with a id=%s", id);
		throw new EntityNotFoundException(
			String.format("There does not exist a task with a id=%s", id));
	}
    _taskAppService.delete(Long.valueOf(id));
    }
	
	// ------------ Update task ------------
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UpdateTaskOutput> update(@PathVariable String id, @RequestBody @Valid UpdateTaskInput task) {
	    FindTaskByIdOutput currentTask = _taskAppService.findById(Long.valueOf(id));
			
		if (currentTask == null) {
			logHelper.getLogger().error("Unable to update. Task with id {} not found.", id);
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
	    return new ResponseEntity(_taskAppService.update(Long.valueOf(id),task), HttpStatus.OK);
	}
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FindTaskByIdOutput> findById(@PathVariable String id) {
    FindTaskByIdOutput output = _taskAppService.findById(Long.valueOf(id));
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity(output, HttpStatus.OK);
	}
    
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity find(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {
		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable Pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		
		return ResponseEntity.ok(_taskAppService.find(searchCriteria,Pageable));
	}
	@RequestMapping(value = "/{id}/apps", method = RequestMethod.GET)
	public ResponseEntity<GetAppsOutput> getApps(@PathVariable String id) {
    GetAppsOutput output= _taskAppService.getApps(Long.valueOf(id));
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(output, HttpStatus.OK);
	}


}

