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
import com.fastcode.emaildemo.application.apps.AppsAppService;
import com.fastcode.emaildemo.application.apps.dto.*;
import com.fastcode.emaildemo.application.task.TaskAppService;
import com.fastcode.emaildemo.application.task.dto.FindTaskByIdOutput;
import java.util.List;
import java.util.Map;
import com.fastcode.emaildemo.commons.logging.LoggingHelper;

@RestController
@RequestMapping("/apps")
public class AppsController {

	@Autowired
	private AppsAppService _appsAppService;
    
    @Autowired
	private TaskAppService  _taskAppService;

	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private Environment env;
	
	
    
    public AppsController(AppsAppService appsAppService, TaskAppService taskAppService,
	 LoggingHelper helper) {
		super();
		this._appsAppService = appsAppService;
    	this._taskAppService = taskAppService;
		this.logHelper = helper;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<CreateAppsOutput> create(@RequestBody @Valid CreateAppsInput apps) {
		CreateAppsOutput output=_appsAppService.create(apps);
		return new ResponseEntity(output, HttpStatus.OK);
	}
   
	// ------------ Delete apps ------------
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String id) {
    FindAppsByIdOutput output = _appsAppService.findById(Long.valueOf(id));
	if (output == null) {
		logHelper.getLogger().error("There does not exist a apps with a id=%s", id);
		throw new EntityNotFoundException(
			String.format("There does not exist a apps with a id=%s", id));
	}
    _appsAppService.delete(Long.valueOf(id));
    }
	
	// ------------ Update apps ------------
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UpdateAppsOutput> update(@PathVariable String id, @RequestBody @Valid UpdateAppsInput apps) {
	    FindAppsByIdOutput currentApps = _appsAppService.findById(Long.valueOf(id));
			
		if (currentApps == null) {
			logHelper.getLogger().error("Unable to update. Apps with id {} not found.", id);
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
	    return new ResponseEntity(_appsAppService.update(Long.valueOf(id),apps), HttpStatus.OK);
	}
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FindAppsByIdOutput> findById(@PathVariable String id) {
    FindAppsByIdOutput output = _appsAppService.findById(Long.valueOf(id));
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
		
		return ResponseEntity.ok(_appsAppService.find(searchCriteria,Pageable));
	}
    
	@RequestMapping(value = "/{id}/task", method = RequestMethod.GET)
	public ResponseEntity getTask(@PathVariable String id, @RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort)throws Exception {
   		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		Map<String,String> joinColDetails=_appsAppService.parseTaskJoinColumn(id);
		if(joinColDetails== null)
		{
			logHelper.getLogger().error("Invalid Join Column");
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		searchCriteria.setJoinColumns(joinColDetails);
		
    	List<FindTaskByIdOutput> output = _taskAppService.find(searchCriteria,pageable);
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity(output, HttpStatus.OK);
	}   
 


}

