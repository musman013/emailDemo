package com.fastcode.emaildemo.emailbuilder.application.emailtemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import com.fastcode.emaildemo.emailbuilder.domain.emailtemplate.IEmailTemplateManager;
import com.fastcode.emaildemo.emailbuilder.domain.emailtemplate.IEmailTemplateManagerHistory;
import com.fastcode.emaildemo.emailbuilder.domain.irepository.DataSourceMetaEntityRepo;
import com.fastcode.emaildemo.emailbuilder.domain.irepository.EmailMergeFieldEntityRepo;
import com.fastcode.emaildemo.emailbuilder.domain.irepository.IDataSourceRepository;
import com.fastcode.emaildemo.emailbuilder.domain.irepository.IEmailTemplateMappingRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.core.env.Environment;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fastcode.emaildemo.emailbuilder.application.datasource.dto.FindDataSourceMetaOutputForMapping;
import com.fastcode.emaildemo.emailbuilder.application.emailtemplate.dto.*;
import com.fastcode.emaildemo.emailbuilder.application.emailvariable.EmailVariableMapper;
import com.fastcode.emaildemo.emailbuilder.application.emailvariable.dto.FindEmailVariableByIdOutput;
import com.fastcode.emaildemo.emailbuilder.domain.model.DataSourceEntity;
import com.fastcode.emaildemo.emailbuilder.domain.model.DataSourceMetaEntity;
import com.fastcode.emaildemo.emailbuilder.domain.model.EmailMergeFieldEntity;
import com.fastcode.emaildemo.emailbuilder.domain.model.EmailTemplateEntity;
import com.fastcode.emaildemo.emailbuilder.domain.model.EmailTemplateMappingEntity;
import com.fastcode.emaildemo.emailbuilder.domain.model.EmailVariableEntity;
import com.fastcode.emaildemo.emailbuilder.domain.model.EmailtemplateEntityHistory;
import com.fastcode.emaildemo.emailbuilder.domain.model.QEmailTemplateEntity;
import com.fastcode.emaildemo.commons.search.*;
import com.fastcode.emaildemo.domain.irepository.FileContentStore;
import com.fastcode.emaildemo.domain.irepository.FileHistoryRepository;
import com.fastcode.emaildemo.domain.irepository.FileRepository;
import com.fastcode.emaildemo.domain.model.File;
import com.fastcode.emaildemo.domain.model.FileHistory;
import com.fastcode.emaildemo.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;
import com.fastcode.emaildemo.emailbuilder.emailconverter.dto.request.Request;
import com.fastcode.emaildemo.emailbuilder.emailconverter.dto.response.Response;
import com.fastcode.emaildemo.emailbuilder.emailconverter.service.MjmlOwnService;

@Service
public class EmailTemplateAppService implements IEmailTemplateAppService {

	static final int case1 = 1;
	static final int case2 = 2;
	static final int case3 = 3;

	@Autowired
	private IEmailTemplateManager _emailTemplateManager;

	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private EmailTemplateMapper emailTemplateMapper;

	@Autowired
	private FileContentStore contentStore;

	@Autowired
	private Environment env;

	@Autowired
	private MjmlOwnService mjmlOwnService;

	@Autowired
	private FileRepository filesRepo;
	
	@Autowired
	private FileHistoryRepository fileHistoryrepo;
	
	@Autowired
	private IEmailTemplateManagerHistory _emailTemplateManagerHistory;
	
	@Autowired
	private EmailMergeFieldEntityRepo emailMergeFieldEntityRepo;
	
	@Autowired
	private IDataSourceRepository dataSourceRepository;
	
	@Autowired
	private DataSourceMetaEntityRepo dataSourceMetaEntityRepo;
	
	@Autowired
	private EmailVariableMapper emailVariableMapper;
	
	@Autowired
	private IEmailTemplateMappingRepo emailTemplateMappingRepo;

	@Transactional(propagation = Propagation.REQUIRED)
	public CreateEmailTemplateOutput create(CreateEmailTemplateInput email) {

		EmailTemplateEntity re = emailTemplateMapper.createEmailTemplateInputToEmailTemplateEntity(email);
		EmailTemplateEntity createdEmail = _emailTemplateManager.create(re);
		EmailtemplateEntityHistory resetData = emailTemplateMapper.createEmailTemplateInputToEmailTemplateEntityforReset(email);
		resetData.setId(re.getId());
		_emailTemplateManagerHistory.create(resetData);
		if (email.getAttachments() != null && email.getAttachments().size() > 0) {
			email.getAttachments().forEach(e -> {
				if (e.getId() != null) {
					
					filesRepo.updateFileEmailTemplate(e.getId(), createdEmail.getId());
					Optional<File> updatedFileOpt=filesRepo.findById(e.getId());
					if(updatedFileOpt.isPresent())
					{
					File updatedFile=updatedFileOpt.get();
					setFileHistoryContent(updatedFile);
					}
					
				}
			});
		}

		CreateEmailTemplateOutput emailTemplateEntityToCreateEmailTemplateOutput = emailTemplateMapper.emailTemplateEntityToCreateEmailTemplateOutput(createdEmail);
		List<File> fileByEmailTemplateId = filesRepo.getFileByEmailTemplateIdAndDeletedFalse(emailTemplateEntityToCreateEmailTemplateOutput.getId());

		emailTemplateEntityToCreateEmailTemplateOutput.setAttachments(fileByEmailTemplateId);
		return emailTemplateEntityToCreateEmailTemplateOutput;
	}

	private void setFileHistoryContent(File updatedFile) {
		FileHistory fh = new FileHistory();
		fh.setId(updatedFile.getId());
		fh.setContentId(updatedFile.getContentId());
		fh.setContentLength(updatedFile.getContentLength());
		fh.setCreated(updatedFile.getCreated());
		fh.setEmailTemplateId(updatedFile.getEmailTemplateId());
		fh.setId(updatedFile.getId());
		fh.setMimeType(updatedFile.getMimeType());
		fh.setName(updatedFile.getName());
		fh.setSummary(updatedFile.getSummary());
		fileHistoryrepo.save(fh);
		
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Long eid) {
		EmailTemplateEntity existing = _emailTemplateManager.findById(eid);
		//deleting all reference
		//deleting last child
		emailTemplateMappingRepo.updatePreviousMappig(eid);
		emailMergeFieldEntityRepo.updateEmailMergeField(existing);
		List<DataSourceEntity> dataSourceMapped=dataSourceRepository.findByEmailTemplateId(eid);
		if(dataSourceMapped!=null && dataSourceMapped.size()>0)
		{
			//deleting second last child
			dataSourceMetaEntityRepo.deleteAllMetaList(dataSourceMapped);
			//deleting first child
			dataSourceRepository.deleteAll(dataSourceMapped);
		}
		
		_emailTemplateManager.delete(existing);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public UpdateEmailTemplateOutput update(Long eid, UpdateEmailTemplateInput email) {

		EmailTemplateEntity ue = emailTemplateMapper.updateEmailTemplateInputToEmailTemplateEntity(email);
		EmailTemplateEntity updatedEmail = _emailTemplateManager.update(ue);
		filesRepo.deletePreviousTemplate(updatedEmail.getId());
		if (email.getAttachments() != null && email.getAttachments().size() > 0) {
			email.getAttachments().forEach(e -> {
				if (e.getId() != null) {
					filesRepo.updateFileEmailTemplate(e.getId(), updatedEmail.getId());
				}
			});
		}

		UpdateEmailTemplateOutput emailTemplateEntityToUpdateEmailTemplateOutput = emailTemplateMapper.emailTemplateEntityToUpdateEmailTemplateOutput(updatedEmail);
		List<File> fileByEmailTemplateId = filesRepo.getFileByEmailTemplateIdAndDeletedFalse(emailTemplateEntityToUpdateEmailTemplateOutput.getId());

		emailTemplateEntityToUpdateEmailTemplateOutput.setAttachments(fileByEmailTemplateId);
		return emailTemplateEntityToUpdateEmailTemplateOutput;

	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindEmailTemplateByIdOutput findById(Long eid) {
		EmailTemplateEntity foundEmail = _emailTemplateManager.findById(eid);

		if (foundEmail == null) {
			logHelper.getLogger().error("There does not exist a email wth a id=%s", eid);
			return null;
		}
		FindEmailTemplateByIdOutput emailTemplateEntityToFindEmailTemplateByIdOutput = emailTemplateMapper.emailTemplateEntityToFindEmailTemplateByIdOutput(foundEmail);

		List<File> fileByEmailTemplateId = filesRepo.getFileByEmailTemplateIdAndDeletedFalse(emailTemplateEntityToFindEmailTemplateByIdOutput.getId());
		emailTemplateEntityToFindEmailTemplateByIdOutput.setAttachments(fileByEmailTemplateId);

		return emailTemplateEntityToFindEmailTemplateByIdOutput;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindEmailTemplateByNameOutput findByName(String name) {
		EmailTemplateEntity foundEmail = _emailTemplateManager.findByName(name);
		if (foundEmail == null) {
			logHelper.getLogger().error("There does not exist a email wth a name=%s", name);
			return null;
		}
		return emailTemplateMapper.emailTemplateEntityToFindEmailTemplateByNameOutput(foundEmail);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<FindEmailTemplateByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception {
		Page<EmailTemplateEntity> foundEmail = _emailTemplateManager.findAll(search(search), pageable);
		List<EmailTemplateEntity> emailList = foundEmail.getContent();

		Iterator<EmailTemplateEntity> emailIterator = emailList.iterator();
		List<FindEmailTemplateByIdOutput> output = new ArrayList<>();

		while (emailIterator.hasNext()) {
			output.add(emailTemplateMapper.emailTemplateEntityToFindEmailTemplateByIdOutput(emailIterator.next()));
		}

		return output;
	}

	public BooleanBuilder search(SearchCriteria search) throws Exception {

		QEmailTemplateEntity emailTemplate = QEmailTemplateEntity.emailTemplateEntity;
		if (search != null) {
			if (search.getType() == case1) {
				return searchAllProperties(emailTemplate, search.getValue(), search.getOperator());
			} else if (search.getType() == case2) {
				List<String> keysList = new ArrayList<String>();
				for (SearchFields f : search.getFields()) {
					keysList.add(f.getFieldName());
				}
				checkProperties(keysList);
				return searchSpecificProperty(emailTemplate, keysList, search.getValue(), search.getOperator());
			} else if (search.getType() == case3) {
				Map<String, SearchFields> map = new HashMap<>();
				for (SearchFields fieldDetails : search.getFields()) {
					map.put(fieldDetails.getFieldName(), fieldDetails);
				}
				List<String> keysList = new ArrayList<String>(map.keySet());
				checkProperties(keysList);
				return searchKeyValuePair(emailTemplate, map);
			}

		}
		return null;
	}

	public BooleanBuilder searchAllProperties(QEmailTemplateEntity emailTemplate, String value, String operator) {
		BooleanBuilder builder = new BooleanBuilder();

		if (operator.equals("contains")) {
			builder.or(emailTemplate.templateName.likeIgnoreCase("%" + value + "%"));
			builder.or(emailTemplate.category.likeIgnoreCase("%" + value + "%"));
			builder.or(emailTemplate.to.likeIgnoreCase("%" + value + "%"));
			builder.or(emailTemplate.cc.likeIgnoreCase("%" + value + "%"));
			builder.or(emailTemplate.bcc.likeIgnoreCase("%" + value + "%"));
			builder.or(emailTemplate.subject.likeIgnoreCase("%" + value + "%"));
		} else if (operator.equals("equals")) {
			builder.or(emailTemplate.templateName.eq(value));
			builder.or(emailTemplate.category.eq(value));
			builder.or(emailTemplate.to.eq(value));
			builder.or(emailTemplate.cc.eq(value));
			builder.or(emailTemplate.bcc.eq(value));
			builder.or(emailTemplate.subject.eq(value));
		}

		return builder;
	}

	public void checkProperties(List<String> list) throws Exception {
		for (int i = 0; i < list.size(); i++) {
			if (!((list.get(i).replace("%20", "").trim().equals("templateName")) || (list.get(i).replace("%20", "").trim().equals("category")) || (list.get(i).replace("%20", "").trim().equals("to"))
					|| (list.get(i).replace("%20", "").trim().equals("cc")) || (list.get(i).replace("%20", "").trim().equals("bcc")) || (list.get(i).replace("%20", "").trim().equals("subject"))
					|| (list.get(i).replace("%20", "").trim().equals("contentJson")))) {

				// Throw an exception
				throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!");
			}
		}
	}

	public BooleanBuilder searchSpecificProperty(QEmailTemplateEntity emailTemplate, List<String> list, String value, String operator) {
		BooleanBuilder builder = new BooleanBuilder();

		for (int i = 0; i < list.size(); i++) {

			if (list.get(i).replace("%20", "").trim().equals("templateName")) {
				if (operator.equals("contains")) {
					builder.or(emailTemplate.templateName.likeIgnoreCase("%" + value + "%"));
				} else if (operator.equals("equals")) {
					builder.or(emailTemplate.templateName.eq(value));
				}
			}
			if (list.get(i).replace("%20", "").trim().equals("category")) {
				if (operator.equals("contains")) {
					builder.or(emailTemplate.category.likeIgnoreCase("%" + value + "%"));
				} else if (operator.equals("equals")) {
					builder.or(emailTemplate.category.eq(value));
				}
			}
			if (list.get(i).replace("%20", "").trim().equals("to")) {
				if (operator.equals("contains")) {
					builder.or(emailTemplate.to.likeIgnoreCase("%" + value + "%"));
				} else if (operator.equals("equals")) {
					builder.or(emailTemplate.to.eq(value));
				}
			}
			if (list.get(i).replace("%20", "").trim().equals("cc")) {
				if (operator.equals("contains")) {
					builder.or(emailTemplate.cc.likeIgnoreCase("%" + value + "%"));
				} else if (operator.equals("equals")) {
					builder.or(emailTemplate.cc.eq(value));
				}
			}
			if (list.get(i).replace("%20", "").trim().equals("bcc")) {
				if (operator.equals("contains")) {
					builder.or(emailTemplate.bcc.likeIgnoreCase("%" + value + "%"));
				} else if (operator.equals("equals")) {
					builder.or(emailTemplate.bcc.eq(value));
				}
			}
			if (list.get(i).replace("%20", "").trim().equals("subject")) {
				if (operator.equals("contains")) {
					builder.or(emailTemplate.subject.likeIgnoreCase("%" + value + "%"));
				} else if (operator.equals("equals")) {
					builder.or(emailTemplate.subject.eq(value));
				}
			}
		}
		return builder;
	}

	public BooleanBuilder searchKeyValuePair(QEmailTemplateEntity emailTemplate, Map<String, SearchFields> map) {
		BooleanBuilder builder = new BooleanBuilder();

		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
			if (details.getKey().replace("%20", "").trim().equals("templateName")) {
				if (details.getValue().getOperator().equals("contains")) {
					builder.and(emailTemplate.templateName.likeIgnoreCase("%" + details.getValue().getSearchValue() + "%"));
				} else if (details.getValue().getOperator().equals("equals")) {
					builder.and(emailTemplate.templateName.eq(details.getValue().getSearchValue()));
				} else if (details.getValue().getOperator().equals("notEqual")) {
					builder.and(emailTemplate.templateName.ne(details.getValue().getSearchValue()));
				}
			}
			if (details.getKey().replace("%20", "").trim().equals("category")) {
				if (details.getValue().getOperator().equals("contains")) {
					builder.and(emailTemplate.category.likeIgnoreCase("%" + details.getValue().getSearchValue() + "%"));
				} else if (details.getValue().getOperator().equals("equals")) {
					builder.and(emailTemplate.category.eq(details.getValue().getSearchValue()));
				} else if (details.getValue().getOperator().equals("notEqual")) {
					builder.and(emailTemplate.category.ne(details.getValue().getSearchValue()));
				}
			}
			if (details.getKey().replace("%20", "").trim().equals("to")) {
				if (details.getValue().getOperator().equals("contains")) {
					builder.and(emailTemplate.to.likeIgnoreCase("%" + details.getValue().getSearchValue() + "%"));
				} else if (details.getValue().getOperator().equals("equals")) {
					builder.and(emailTemplate.to.eq(details.getValue().getSearchValue()));
				} else if (details.getValue().getOperator().equals("notEqual")) {
					builder.and(emailTemplate.to.ne(details.getValue().getSearchValue()));
				}
			}
			if (details.getKey().replace("%20", "").trim().equals("cc")) {
				if (details.getValue().getOperator().equals("contains")) {
					builder.and(emailTemplate.cc.likeIgnoreCase("%" + details.getValue().getSearchValue() + "%"));
				} else if (details.getValue().getOperator().equals("equals")) {
					builder.and(emailTemplate.cc.eq(details.getValue().getSearchValue()));
				} else if (details.getValue().getOperator().equals("notEqual")) {
					builder.and(emailTemplate.cc.ne(details.getValue().getSearchValue()));
				}
			}
			if (details.getKey().replace("%20", "").trim().equals("bcc")) {
				if (details.getValue().getOperator().equals("contains")) {
					builder.and(emailTemplate.bcc.likeIgnoreCase("%" + details.getValue().getSearchValue() + "%"));
				} else if (details.getValue().getOperator().equals("equals")) {
					builder.and(emailTemplate.bcc.eq(details.getValue().getSearchValue()));
				} else if (details.getValue().getOperator().equals("notEqual")) {
					builder.and(emailTemplate.bcc.ne(details.getValue().getSearchValue()));
				}
			}
			if (details.getKey().replace("%20", "").trim().equals("subject")) {
				if (details.getValue().getOperator().equals("contains")) {
					builder.and(emailTemplate.subject.likeIgnoreCase("%" + details.getValue().getSearchValue() + "%"));
				} else if (details.getValue().getOperator().equals("equals")) {
					builder.and(emailTemplate.subject.eq(details.getValue().getSearchValue()));
				} else if (details.getValue().getOperator().equals("notEqual")) {
					builder.and(emailTemplate.subject.ne(details.getValue().getSearchValue()));
				}
			}

			if (details.getKey().replace("%20", "").trim().equals("contentJson")) {
				if (details.getValue().getOperator().equals("contains")) {
					builder.and(emailTemplate.contentJson.likeIgnoreCase("%" + details.getValue().getSearchValue() + "%"));
				} else if (details.getValue().getOperator().equals("equals")) {
					builder.and(emailTemplate.contentJson.eq(details.getValue().getSearchValue()));
				} else if (details.getValue().getOperator().equals("notEqual")) {
					builder.and(emailTemplate.contentJson.ne(details.getValue().getSearchValue()));
				}
			}

		}
		return builder;
	}

	public ClientHttpRequestFactory getClientHttpRequestFactory() {
		int timeout = 5000;
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setConnectTimeout(timeout);
		return clientHttpRequestFactory;
	}

	public String convertJsonToHtml(String jsonString) throws IOException {
		String html = " ";
		ObjectMapper mapper = new ObjectMapper();

		mapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		Request request = mapper.readValue(jsonString, Request.class);
		Response response = mjmlOwnService.genrateHtml(request);
		logHelper.getLogger().error("Error", response.getErrors());
		html = response.getHtml();

		return html;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindEmailTemplateByIdOutput findByResetId(Long eid) {
		EmailtemplateEntityHistory foundEmail = _emailTemplateManagerHistory.findById(eid);

		if (foundEmail == null) {
			logHelper.getLogger().error("There does not exist a email wth a id=%s", eid);
			return null;
		}
		FindEmailTemplateByIdOutput emailTemplateEntityToFindEmailTemplateByIdOutput = emailTemplateMapper.emailTemplateEntityToFindEmailTemplateByIdOutputforReset(foundEmail);

		
		List<Long> allHistoryFiles = fileHistoryrepo.getFileByEmailTemplateId(emailTemplateEntityToFindEmailTemplateByIdOutput.getId());
	    
	    if (allHistoryFiles!=null && !allHistoryFiles.isEmpty()) {    	
			       filesRepo.setDeleteAdditionalFileEmailTemplate(allHistoryFiles, emailTemplateEntityToFindEmailTemplateByIdOutput.getId());

	    }
	    
		List<File> fileByEmailTemplateId = filesRepo.getFileByEmailTemplateIdAndDeletedFalse(emailTemplateEntityToFindEmailTemplateByIdOutput.getId());
		emailTemplateEntityToFindEmailTemplateByIdOutput.setAttachments(fileByEmailTemplateId);

		return emailTemplateEntityToFindEmailTemplateByIdOutput;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public UpdateEmailTemplateOutput reset(Long valueOf, UpdateEmailTemplateInput email) {

		EmailTemplateEntity ue = emailTemplateMapper.updateEmailTemplateInputToEmailTemplateEntity(email);
		EmailTemplateEntity updatedEmail = _emailTemplateManager.update(ue);
		if (email.getAttachments() != null && email.getAttachments().size() > 0) {
			email.getAttachments().forEach(e -> {
				if (e.getId() != null) {
					filesRepo.updateFileEmailTemplate(e.getId(), updatedEmail.getId());
				}
			});
		}

		UpdateEmailTemplateOutput emailTemplateEntityToUpdateEmailTemplateOutput = emailTemplateMapper.emailTemplateEntityToUpdateEmailTemplateOutput(updatedEmail);
		
		List<File> fileByEmailTemplateId = filesRepo.getFileByEmailTemplateIdAndDeletedFalse(emailTemplateEntityToUpdateEmailTemplateOutput.getId());

		emailTemplateEntityToUpdateEmailTemplateOutput.setAttachments(fileByEmailTemplateId);
		return emailTemplateEntityToUpdateEmailTemplateOutput;

	}

	public List<FindEmailTemplateByIdOutput> findAll() {
		List<EmailTemplateEntity> foundEmail= _emailTemplateManager.findAll();
		
		Iterator<EmailTemplateEntity> emailIterator = foundEmail.iterator();
		List<FindEmailTemplateByIdOutput> output = new ArrayList<>();

		  while (emailIterator.hasNext()) {
	      output.add(emailTemplateMapper.emailTemplateEntityToFindEmailTemplateByIdOutput(emailIterator.next()));    
		 }

		return output;
	}

	public List<FindDataSourceMetaOutputForMapping> getMappingForEmail(Long emailTemplateId) {
		List<EmailMergeFieldEntity> emailMergeFieldList = emailMergeFieldEntityRepo.findByEmailTemplateId(emailTemplateId);
		List<DataSourceEntity> dataSourceEntityList = dataSourceRepository.findByEmailTemplateId(emailTemplateId);
		List<FindDataSourceMetaOutputForMapping> outputList = new ArrayList<>();
		int totalMergeField=0;
		if(emailMergeFieldList!=null && emailMergeFieldList.size()>0)
		{
			totalMergeField=emailMergeFieldList.size();
		}
		int mappedTillNow=0;
		 mappedTillNow= emailTemplateMappingRepo.getMappedTillNowDataSource(emailTemplateId);
		
		for(EmailMergeFieldEntity emailMergeField : emailMergeFieldList)
		{
			FindDataSourceMetaOutputForMapping data = new FindDataSourceMetaOutputForMapping();
			
			//already mapped list
			List<DataSourceMetaEntity> alreadyMappedList = emailTemplateMappingRepo.findByEmailTemplateEntityIdAndMergeFieldId(emailTemplateId,emailMergeField.getMergeField().getId());
			if(alreadyMappedList!=null && alreadyMappedList.size()>0)
			{
			data.setAlreadyMappedList(emailVariableMapper.dataSourceEntityToDataSourceMetaList(alreadyMappedList));
			}
			data.setTotalMergeField(totalMergeField);
			data.setMappedMergeField(mappedTillNow);
			if(emailMergeField.getMergeField().getPropertyType().equals("text"))
			{
				List<String> excludedString= new ArrayList<>();
				excludedString.add("MULTI-LINE");
				excludedString.add("List");
				excludedString.add("List of Images");
				excludedString.add("Clickable Image");
				excludedString.add("Image");
				List<DataSourceMetaEntity> metaSourceList=dataSourceMetaEntityRepo.findByMetaColumnDataTypeNotInAndDataSourceEntityIn(excludedString,dataSourceEntityList);
				data.setMergeField(emailVariableMapper.emailVariableEntityToFindEmailVariableByIdOutput(emailMergeField.getMergeField()));
				data.setDataSourceMetaList(emailVariableMapper.dataSourceEntityToDataSourceMetaList(metaSourceList));
				outputList.add(data);	
				
			}
			else if(emailMergeField.getMergeField().getPropertyType().equals("Number"))
			{
				String dataType="bigint";
			List<DataSourceMetaEntity> metaSourceList=dataSourceMetaEntityRepo.findByMetaColumnDataTypeAndDataSourceEntityIn(dataType,dataSourceEntityList);
			data.setMergeField(emailVariableMapper.emailVariableEntityToFindEmailVariableByIdOutput(emailMergeField.getMergeField()));
			data.setDataSourceMetaList(emailVariableMapper.dataSourceEntityToDataSourceMetaList(metaSourceList));
			outputList.add(data);
			}
			else if(emailMergeField.getMergeField().getPropertyType().equals("Email"))
			{
				String dataType="email";
			List<DataSourceMetaEntity> metaSourceList=dataSourceMetaEntityRepo.findByMetaColumnDataTypeAndDataSourceEntityIn(dataType,dataSourceEntityList);
			data.setMergeField(emailVariableMapper.emailVariableEntityToFindEmailVariableByIdOutput(emailMergeField.getMergeField()));
			data.setDataSourceMetaList(emailVariableMapper.dataSourceEntityToDataSourceMetaList(metaSourceList));
			outputList.add(data);
			}
			else if(emailMergeField.getMergeField().getPropertyType().equals("Multi-line Text"))
			{
				String dataType="Multi-line Text";
				List<DataSourceMetaEntity> metaSourceList=dataSourceMetaEntityRepo.findByMetaColumnDataTypeAndDataSourceEntityIn(dataType,dataSourceEntityList);
				data.setMergeField(emailVariableMapper.emailVariableEntityToFindEmailVariableByIdOutput(emailMergeField.getMergeField()));
				data.setDataSourceMetaList(emailVariableMapper.dataSourceEntityToDataSourceMetaList(metaSourceList));
				outputList.add(data);
			}
			else  if(emailMergeField.getMergeField().getPropertyType().equals("List"))
			{
				String dataType="character varying";
				List<DataSourceMetaEntity> metaSourceList=dataSourceMetaEntityRepo.findByMetaColumnDataTypeAndDataSourceEntityIn(dataType,dataSourceEntityList);
				data.setMergeField(emailVariableMapper.emailVariableEntityToFindEmailVariableByIdOutput(emailMergeField.getMergeField()));
				data.setDataSourceMetaList(emailVariableMapper.dataSourceEntityToDataSourceMetaList(metaSourceList));
				outputList.add(data);
			}
			else 
			{
			List<DataSourceMetaEntity> metaSourceList=dataSourceMetaEntityRepo.findByMetaColumnDataTypeAndDataSourceEntityIn(emailMergeField.getMergeField().getPropertyType(),dataSourceEntityList);
			data.setMergeField(emailVariableMapper.emailVariableEntityToFindEmailVariableByIdOutput(emailMergeField.getMergeField()));
			data.setDataSourceMetaList(emailVariableMapper.dataSourceEntityToDataSourceMetaList(metaSourceList));
			outputList.add(data);
			}
			}
		return outputList;
	}

	public List<CreateEmailTemplateMappingInput> createMapping(@Valid List<CreateEmailTemplateMappingInput> mappings) {
			List<EmailTemplateMappingEntity> totalList = new ArrayList<>();
			if(mappings!=null && mappings.size()>0)
			{
			emailTemplateMappingRepo.updatePreviousMappig(mappings.get(0).getEmailTemplateId());
			}
			for(CreateEmailTemplateMappingInput mapping : mappings)
			{
				EmailTemplateMappingEntity emailMapping = new EmailTemplateMappingEntity();
				emailMapping.setDataSourceEntiry(new DataSourceEntity(mapping.getDataSourceId()));
				emailMapping.setDataSourceMetaEntity(new DataSourceMetaEntity(mapping.getDataSourceMetaId()));
				emailMapping.setEmailTemplateEntity(new EmailTemplateEntity(mapping.getEmailTemplateId()));
				emailMapping.setMergeField(new EmailVariableEntity(mapping.getMergeFieldId()));
				totalList.add(emailMapping);
			}
		emailTemplateMappingRepo.saveAll(totalList);
		return mappings;
	}

}