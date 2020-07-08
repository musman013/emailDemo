package com.fastcode.emaildemo.emailbuilder.restcontrollers;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.json.JSONException;
import org.json.JSONObject;
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

import com.fastcode.emaildemo.commons.application.OffsetBasedPageRequest;
import com.fastcode.emaildemo.commons.domain.EmptyJsonResponse;
import com.fastcode.emaildemo.commons.logging.LoggingHelper;
import com.fastcode.emaildemo.commons.search.SearchCriteria;
import com.fastcode.emaildemo.commons.search.SearchUtils;
import com.fastcode.emaildemo.emailbuilder.application.datasource.DataSourceAppService;
import com.fastcode.emaildemo.emailbuilder.application.datasource.dto.CreateDataSourceInput;
import com.fastcode.emaildemo.emailbuilder.application.datasource.dto.CreateDataSourceOutput;
import com.fastcode.emaildemo.emailbuilder.application.datasource.dto.FindDataSourceByIdOutput;
import com.fastcode.emaildemo.emailbuilder.application.datasource.dto.FindDataSourceByNameOutput;
import com.fastcode.emaildemo.emailbuilder.application.datasource.dto.PreviewDataSourceOutput;
import com.fastcode.emaildemo.emailbuilder.application.datasource.dto.ResponseEntityMergeField;
import com.fastcode.emaildemo.emailbuilder.application.datasource.dto.UpdateDataSourceInput;
import com.fastcode.emaildemo.emailbuilder.application.datasource.dto.UpdateDataSourceOutput;
import com.fastcode.emaildemo.emailbuilder.application.emailvariable.EmailVariableAppService;
import com.fastcode.emaildemo.emailbuilder.application.emailvariable.dto.CreateEmailVariableInput;
import com.fastcode.emaildemo.emailbuilder.application.emailvariable.dto.CreateEmailVariableOutput;
import com.fastcode.emaildemo.emailbuilder.application.emailvariable.dto.FindEmailVariableByIdOutput;
import com.fastcode.emaildemo.emailbuilder.application.emailvariable.dto.FindEmailVariableByNameOutput;
import com.fastcode.emaildemo.emailbuilder.application.emailvariable.dto.UpdateEmailVariableInput;
import com.fastcode.emaildemo.emailbuilder.application.emailvariable.dto.UpdateEmailVariableOutput;

@RestController
@RequestMapping("/datasource")
public class DataSourceController {

	@Autowired
	private DataSourceAppService dataSourceAppService;

	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private Environment env;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<CreateDataSourceOutput> create(@RequestBody @Valid CreateDataSourceInput dataSource) {
		FindDataSourceByNameOutput foundDataSource = dataSourceAppService.findByName(dataSource.getName());
		if (foundDataSource != null) {
			logHelper.getLogger().error("There already exists a datasource with a name=%s", foundDataSource.getName());
			throw new EntityExistsException(String.format("There already exists a datasource with name=%s", foundDataSource.getName()));
		}
		return new ResponseEntity(dataSourceAppService.create(dataSource), HttpStatus.OK);
	}

	// ------------ Delete an email ------------
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> delete(@PathVariable String id) {
		FindDataSourceByIdOutput dob = dataSourceAppService.findById(Long.valueOf(id));

		if (dob == null) {
			logHelper.getLogger().error("There does not exist a data source wth a id=%s", id);
			throw new EntityNotFoundException(String.format("There does not exist a data source wth a id=%s", id));
		}
		return new ResponseEntity(dataSourceAppService.delete(Long.valueOf(id)),HttpStatus.OK);
	}
	// ------------ Update an email ------------

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UpdateDataSourceOutput> update(@PathVariable String id, @RequestBody @Valid UpdateDataSourceInput dataSource) {
		FindDataSourceByIdOutput currentDataSource = dataSourceAppService.findById(Long.valueOf(id));
		if (currentDataSource == null) {
			logHelper.getLogger().error("Unable to update. Email with id {} not found.", id);
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(dataSourceAppService.update(Long.valueOf(id), dataSource), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FindDataSourceByIdOutput> findById(@PathVariable String id) {

		FindDataSourceByIdOutput eo = dataSourceAppService.findById(Long.valueOf(id));

		if (eo == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(eo, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity find(@RequestParam(value = "search", required = false) String search, @RequestParam(value = "offset", required = false) String offset,
			@RequestParam(value = "limit", required = false) String limit, Sort sort) throws Exception {
		if (offset == null) {
			offset = env.getProperty("fastCode.offset.default");
		}
		if (limit == null) {
			limit = env.getProperty("fastCode.limit.default");
		}
		Pageable Pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);

		return ResponseEntity.ok(dataSourceAppService.find(searchCriteria, Pageable));
	}

	
	@RequestMapping(value="/list",method = RequestMethod.GET)
	public ResponseEntity list() throws Exception {
		return ResponseEntity.ok(dataSourceAppService.findAll());
	}
	

	/**
	 * 
	 * @param dataSource
	 * @return Table generated by query and its data type
	 * @throws JSONException
	 */
	@RequestMapping(value="/preview/table",method = RequestMethod.GET)
	public ResponseEntity<PreviewDataSourceOutput> preview(@RequestParam(value="query") String dataSource) throws JSONException {
		PreviewDataSourceOutput pds= dataSourceAppService.preview(dataSource);
		return new ResponseEntity(pds, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param id
	 * @return Merge Field mapped comma seperated
	 * @throws JSONException
	 */
	@RequestMapping(value="/getAllMappedForMergeField/{id}",method = RequestMethod.GET)
	public ResponseEntity<ResponseEntityMergeField> getAllMappedMergeField(@PathVariable(value="id") Long id) throws JSONException {
		String pds= dataSourceAppService.getAllMappedMergeField(id);
		ResponseEntityMergeField response = new ResponseEntityMergeField();
		response.setFields(pds);
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param id
	 * @returnEmail template mapped
	 * @throws JSONException
	 */
	@RequestMapping(value="/getAllMappedForEmailTemplate/{id}",method = RequestMethod.GET)
	public ResponseEntity<ResponseEntityMergeField> getAllMappedForEmailTemplate(@PathVariable(value="id") Long id) throws JSONException {
		String pds= dataSourceAppService.getAllMappedForEmailTemplate(id);
		ResponseEntityMergeField response = new ResponseEntityMergeField();
		response.setFields(pds);
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param id
	 * @return Mapped DataSource for a email tempalte
	 * @throws JSONException
	 */
	@RequestMapping(value="/getAlreadyMappedDatasourceForEmailTemplate/{id}",method = RequestMethod.GET)
	public ResponseEntity<ResponseEntityMergeField> getAlreadyMappedDatasourceForEmailTemplate(@PathVariable(value="id") Long id) throws JSONException {
		String pds= dataSourceAppService.getAlreadyMappedDatasourceForEmailTemplate(id);
		ResponseEntityMergeField response = new ResponseEntityMergeField();
		response.setFields(pds);
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	
	
}
