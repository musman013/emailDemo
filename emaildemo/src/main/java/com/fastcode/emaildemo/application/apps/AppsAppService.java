package com.fastcode.emaildemo.application.apps;

import com.fastcode.emaildemo.application.apps.dto.*;
import com.fastcode.emaildemo.domain.apps.IAppsManager;
import com.fastcode.emaildemo.domain.model.QAppsEntity;
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
public class AppsAppService implements IAppsAppService {

    static final int case1=1;
	static final int case2=2;
	static final int case3=3;
	
	@Autowired
	private IAppsManager _appsManager;

	@Autowired
	private AppsMapper mapper;
	
	@Autowired
	private LoggingHelper logHelper;

    @Transactional(propagation = Propagation.REQUIRED)
	public CreateAppsOutput create(CreateAppsInput input) {

		AppsEntity apps = mapper.createAppsInputToAppsEntity(input);
		AppsEntity createdApps = _appsManager.create(apps);
		
		return mapper.appsEntityToCreateAppsOutput(createdApps);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public UpdateAppsOutput update(Long  appsId, UpdateAppsInput input) {

		AppsEntity apps = mapper.updateAppsInputToAppsEntity(input);
		
		AppsEntity updatedApps = _appsManager.update(apps);
		
		return mapper.appsEntityToUpdateAppsOutput(updatedApps);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Long appsId) {

		AppsEntity existing = _appsManager.findById(appsId) ; 
		_appsManager.delete(existing);
		
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindAppsByIdOutput findById(Long appsId) {

		AppsEntity foundApps = _appsManager.findById(appsId);
		if (foundApps == null)  
			return null ; 
 	   
 	    FindAppsByIdOutput output=mapper.appsEntityToFindAppsByIdOutput(foundApps); 
		return output;
	}
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<FindAppsByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<AppsEntity> foundApps = _appsManager.findAll(search(search), pageable);
		List<AppsEntity> appsList = foundApps.getContent();
		Iterator<AppsEntity> appsIterator = appsList.iterator(); 
		List<FindAppsByIdOutput> output = new ArrayList<>();

		while (appsIterator.hasNext()) {
			output.add(mapper.appsEntityToFindAppsByIdOutput(appsIterator.next()));
		}
		return output;
	}
	
	public BooleanBuilder search(SearchCriteria search) throws Exception {

		QAppsEntity apps= QAppsEntity.appsEntity;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(apps, map,search.getJoinColumns());
		}
		return null;
	}
	
	public void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
			if(!(
				list.get(i).replace("%20","").trim().equals("artifactId") ||
				list.get(i).replace("%20","").trim().equals("authMethod") ||
				list.get(i).replace("%20","").trim().equals("authTable") ||
				list.get(i).replace("%20","").trim().equals("caching") ||
				list.get(i).replace("%20","").trim().equals("createdDate") ||
				list.get(i).replace("%20","").trim().equals("description") ||
				list.get(i).replace("%20","").trim().equals("destinationPath") ||
				list.get(i).replace("%20","").trim().equals("emailTemplates") ||
				list.get(i).replace("%20","").trim().equals("entityHistory") ||
				list.get(i).replace("%20","").trim().equals("groupId") ||
				list.get(i).replace("%20","").trim().equals("id") ||
				list.get(i).replace("%20","").trim().equals("jdbcPassword") ||
				list.get(i).replace("%20","").trim().equals("jdbcUrl") ||
				list.get(i).replace("%20","").trim().equals("jdbcUsername") ||
				list.get(i).replace("%20","").trim().equals("name") ||
				list.get(i).replace("%20","").trim().equals("processAdminApp") ||
				list.get(i).replace("%20","").trim().equals("processManagement") ||
				list.get(i).replace("%20","").trim().equals("scheduler") ||
				list.get(i).replace("%20","").trim().equals("schema") ||
				list.get(i).replace("%20","").trim().equals("task") ||
				list.get(i).replace("%20","").trim().equals("upgrade") ||
				list.get(i).replace("%20","").trim().equals("userId") ||
				list.get(i).replace("%20","").trim().equals("userOnly")
			)) 
			{
			 throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
			}
		}
	}
	
	public BooleanBuilder searchKeyValuePair(QAppsEntity apps, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();
        
		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
            if(details.getKey().replace("%20","").trim().equals("artifactId")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(apps.artifactId.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(apps.artifactId.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(apps.artifactId.ne(details.getValue().getSearchValue()));
			}
            if(details.getKey().replace("%20","").trim().equals("authMethod")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(apps.authMethod.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(apps.authMethod.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(apps.authMethod.ne(details.getValue().getSearchValue()));
			}
            if(details.getKey().replace("%20","").trim().equals("authTable")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(apps.authTable.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(apps.authTable.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(apps.authTable.ne(details.getValue().getSearchValue()));
			}
			if(details.getKey().replace("%20","").trim().equals("caching")) {
				if(details.getValue().getOperator().equals("equals") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(apps.caching.eq(Boolean.parseBoolean(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(apps.caching.ne(Boolean.parseBoolean(details.getValue().getSearchValue())));
			}
			if(details.getKey().replace("%20","").trim().equals("createdDate")) {
				if(details.getValue().getOperator().equals("equals") && SearchUtils.stringToDate(details.getValue().getSearchValue()) !=null)
					builder.and(apps.createdDate.eq(SearchUtils.stringToDate(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && SearchUtils.stringToDate(details.getValue().getSearchValue()) !=null)
					builder.and(apps.createdDate.ne(SearchUtils.stringToDate(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("range"))
				{
				   Date startDate= SearchUtils.stringToDate(details.getValue().getStartingValue());
				   Date endDate= SearchUtils.stringToDate(details.getValue().getEndingValue());
				   if(startDate!=null && endDate!=null)	 
					   builder.and(apps.createdDate.between(startDate,endDate));
				   else if(endDate!=null)
					   builder.and(apps.createdDate.loe(endDate));
                   else if(startDate!=null)
                	   builder.and(apps.createdDate.goe(startDate));  
                 }
                   
			}
            if(details.getKey().replace("%20","").trim().equals("description")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(apps.description.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(apps.description.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(apps.description.ne(details.getValue().getSearchValue()));
			}
            if(details.getKey().replace("%20","").trim().equals("destinationPath")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(apps.destinationPath.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(apps.destinationPath.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(apps.destinationPath.ne(details.getValue().getSearchValue()));
			}
			if(details.getKey().replace("%20","").trim().equals("emailTemplates")) {
				if(details.getValue().getOperator().equals("equals") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(apps.emailTemplates.eq(Boolean.parseBoolean(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(apps.emailTemplates.ne(Boolean.parseBoolean(details.getValue().getSearchValue())));
			}
			if(details.getKey().replace("%20","").trim().equals("entityHistory")) {
				if(details.getValue().getOperator().equals("equals") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(apps.entityHistory.eq(Boolean.parseBoolean(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(apps.entityHistory.ne(Boolean.parseBoolean(details.getValue().getSearchValue())));
			}
            if(details.getKey().replace("%20","").trim().equals("groupId")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(apps.groupId.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(apps.groupId.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(apps.groupId.ne(details.getValue().getSearchValue()));
			}
            if(details.getKey().replace("%20","").trim().equals("jdbcPassword")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(apps.jdbcPassword.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(apps.jdbcPassword.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(apps.jdbcPassword.ne(details.getValue().getSearchValue()));
			}
            if(details.getKey().replace("%20","").trim().equals("jdbcUrl")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(apps.jdbcUrl.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(apps.jdbcUrl.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(apps.jdbcUrl.ne(details.getValue().getSearchValue()));
			}
            if(details.getKey().replace("%20","").trim().equals("jdbcUsername")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(apps.jdbcUsername.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(apps.jdbcUsername.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(apps.jdbcUsername.ne(details.getValue().getSearchValue()));
			}
            if(details.getKey().replace("%20","").trim().equals("name")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(apps.name.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(apps.name.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(apps.name.ne(details.getValue().getSearchValue()));
			}
			if(details.getKey().replace("%20","").trim().equals("processAdminApp")) {
				if(details.getValue().getOperator().equals("equals") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(apps.processAdminApp.eq(Boolean.parseBoolean(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(apps.processAdminApp.ne(Boolean.parseBoolean(details.getValue().getSearchValue())));
			}
			if(details.getKey().replace("%20","").trim().equals("processManagement")) {
				if(details.getValue().getOperator().equals("equals") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(apps.processManagement.eq(Boolean.parseBoolean(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(apps.processManagement.ne(Boolean.parseBoolean(details.getValue().getSearchValue())));
			}
			if(details.getKey().replace("%20","").trim().equals("scheduler")) {
				if(details.getValue().getOperator().equals("equals") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(apps.scheduler.eq(Boolean.parseBoolean(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(apps.scheduler.ne(Boolean.parseBoolean(details.getValue().getSearchValue())));
			}
            if(details.getKey().replace("%20","").trim().equals("schema")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(apps.schema.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(apps.schema.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(apps.schema.ne(details.getValue().getSearchValue()));
			}
			if(details.getKey().replace("%20","").trim().equals("upgrade")) {
				if(details.getValue().getOperator().equals("equals") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(apps.upgrade.eq(Boolean.parseBoolean(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(apps.upgrade.ne(Boolean.parseBoolean(details.getValue().getSearchValue())));
			}
			if(details.getKey().replace("%20","").trim().equals("userId")) {
				if(details.getValue().getOperator().equals("equals") && StringUtils.isNumeric(details.getValue().getSearchValue()))
					builder.and(apps.userId.eq(Long.valueOf(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && StringUtils.isNumeric(details.getValue().getSearchValue()))
					builder.and(apps.userId.ne(Long.valueOf(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("range"))
				{
				   if(StringUtils.isNumeric(details.getValue().getStartingValue()) && StringUtils.isNumeric(details.getValue().getEndingValue()))
                	   builder.and(apps.userId.between(Long.valueOf(details.getValue().getStartingValue()), Long.valueOf(details.getValue().getEndingValue())));
                   else if(StringUtils.isNumeric(details.getValue().getStartingValue()))
                	   builder.and(apps.userId.goe(Long.valueOf(details.getValue().getStartingValue())));
                   else if(StringUtils.isNumeric(details.getValue().getEndingValue()))
                	   builder.and(apps.userId.loe(Long.valueOf(details.getValue().getEndingValue())));
				}
			}
			if(details.getKey().replace("%20","").trim().equals("userOnly")) {
				if(details.getValue().getOperator().equals("equals") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(apps.userOnly.eq(Boolean.parseBoolean(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(apps.userOnly.ne(Boolean.parseBoolean(details.getValue().getSearchValue())));
			}
		}
		return builder;
	}
	
	
	public Map<String,String> parseTaskJoinColumn(String keysString) {
		
		Map<String,String> joinColumnMap = new HashMap<String,String>();
		joinColumnMap.put("appId", keysString);
		return joinColumnMap;
	}
    
	
}


