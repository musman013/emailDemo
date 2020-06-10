package com.fastcode.emaildemo.application.apps;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fastcode.emaildemo.application.apps.dto.CreateAppsInput;
import com.fastcode.emaildemo.application.apps.dto.FindAppsByIdOutput;
import com.fastcode.emaildemo.application.apps.dto.UpdateAppsInput;
import com.fastcode.emaildemo.commons.logging.LoggingHelper;
import com.fastcode.emaildemo.commons.search.SearchCriteria;
import com.fastcode.emaildemo.commons.search.SearchFields;
import com.fastcode.emaildemo.domain.apps.AppsManager;
import com.fastcode.emaildemo.domain.model.AppsEntity;
import com.fastcode.emaildemo.domain.model.QAppsEntity;
import com.fastcode.emaildemo.domain.task.TaskManager;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class AppsAppServiceTest {

	@InjectMocks
	@Spy
	AppsAppService _appService;

	@Mock
	private AppsManager _appsManager;
	
    @Mock
	private TaskManager  _taskManager;
	
	@Mock
	private AppsMapper _mapper;

	@Mock
	private Logger loggerMock;

	@Mock
	private LoggingHelper logHelper;
	

    private static Long ID=15L;
    
	 
	@Before
	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(_appService);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());
	}
	
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void findAppsById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

		Mockito.when(_appsManager.findById(anyLong())).thenReturn(null);
		Assertions.assertThat(_appService.findById(ID)).isEqualTo(null);
	}
	
	@Test
	public void findAppsById_IdIsNotNullAndIdExists_ReturnApps() {

		AppsEntity apps = mock(AppsEntity.class);
		Mockito.when(_appsManager.findById(anyLong())).thenReturn(apps);
		Assertions.assertThat(_appService.findById(ID)).isEqualTo(_mapper.appsEntityToFindAppsByIdOutput(apps));
	}
	
	 @Test 
    public void createApps_AppsIsNotNullAndAppsDoesNotExist_StoreApps() { 
 
       AppsEntity appsEntity = mock(AppsEntity.class); 
       CreateAppsInput apps = new CreateAppsInput();
   
		
        Mockito.when(_mapper.createAppsInputToAppsEntity(any(CreateAppsInput.class))).thenReturn(appsEntity); 
        Mockito.when(_appsManager.create(any(AppsEntity.class))).thenReturn(appsEntity);
      
        Assertions.assertThat(_appService.create(apps)).isEqualTo(_mapper.appsEntityToCreateAppsOutput(appsEntity)); 
    } 
	@Test
	public void updateApps_AppsIdIsNotNullAndIdExists_ReturnUpdatedApps() {

		AppsEntity appsEntity = mock(AppsEntity.class);
		UpdateAppsInput apps= mock(UpdateAppsInput.class);
		
		Mockito.when(_mapper.updateAppsInputToAppsEntity(any(UpdateAppsInput.class))).thenReturn(appsEntity);
		Mockito.when(_appsManager.update(any(AppsEntity.class))).thenReturn(appsEntity);
		Assertions.assertThat(_appService.update(ID,apps)).isEqualTo(_mapper.appsEntityToUpdateAppsOutput(appsEntity));
	}
    
	@Test
	public void deleteApps_AppsIsNotNullAndAppsExists_AppsRemoved() {

		AppsEntity apps= mock(AppsEntity.class);
		Mockito.when(_appsManager.findById(anyLong())).thenReturn(apps);
		
		_appService.delete(ID); 
		verify(_appsManager).delete(apps);
	}
	
	@Test
	public void find_ListIsEmpty_ReturnList() throws Exception {

		List<AppsEntity> list = new ArrayList<>();
		Page<AppsEntity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindAppsByIdOutput> output = new ArrayList<>();
		SearchCriteria search= new SearchCriteria();
//		search.setType(1);
//		search.setValue("xyz");
//		search.setOperator("equals");

		Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
		Mockito.when(_appsManager.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void find_ListIsNotEmpty_ReturnList() throws Exception {

		List<AppsEntity> list = new ArrayList<>();
		AppsEntity apps = mock(AppsEntity.class);
		list.add(apps);
    	Page<AppsEntity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindAppsByIdOutput> output = new ArrayList<>();
        SearchCriteria search= new SearchCriteria();
//		search.setType(1);
//		search.setValue("xyz");
//		search.setOperator("equals");
		output.add(_mapper.appsEntityToFindAppsByIdOutput(apps));
		
		Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
    	Mockito.when(_appsManager.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void searchKeyValuePair_PropertyExists_ReturnBooleanBuilder() {
		QAppsEntity apps = QAppsEntity.appsEntity;
	    SearchFields searchFields = new SearchFields();
		searchFields.setOperator("equals");
		searchFields.setSearchValue("xyz");
	    Map<String,SearchFields> map = new HashMap<>();
        map.put("artifactId",searchFields);
		 Map<String,String> searchMap = new HashMap<>();
        searchMap.put("xyz",String.valueOf(ID));
		BooleanBuilder builder = new BooleanBuilder();
         builder.and(apps.artifactId.eq("xyz"));
		Assertions.assertThat(_appService.searchKeyValuePair(apps,map,searchMap)).isEqualTo(builder);
	}
	
	@Test (expected = Exception.class)
	public void checkProperties_PropertyDoesNotExist_ThrowException() throws Exception {
		List<String> list = new ArrayList<>();
		list.add("xyz");
		_appService.checkProperties(list);
	}
	
	@Test
	public void checkProperties_PropertyExists_ReturnNothing() throws Exception {
		List<String> list = new ArrayList<>();
        list.add("artifactId");
        list.add("authMethod");
        list.add("authTable");
        list.add("description");
        list.add("destinationPath");
        list.add("groupId");
        list.add("jdbcPassword");
        list.add("jdbcUrl");
        list.add("jdbcUsername");
        list.add("name");
        list.add("schema");
		_appService.checkProperties(list);
	}
	
	@Test
	public void  search_SearchIsNotNullAndSearchContainsCaseThree_ReturnBooleanBuilder() throws Exception {
	
		Map<String,SearchFields> map = new HashMap<>();
		QAppsEntity apps = QAppsEntity.appsEntity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(3);
		search.setValue("xyz");
		search.setOperator("equals");
        fields.setFieldName("artifactId");
        fields.setOperator("equals");
		fields.setSearchValue("xyz");
        fieldsList.add(fields);
        search.setFields(fieldsList);
		BooleanBuilder builder = new BooleanBuilder();
        builder.or(apps.artifactId.eq("xyz"));
        Mockito.doNothing().when(_appService).checkProperties(any(List.class));
		Mockito.doReturn(builder).when(_appService).searchKeyValuePair(any(QAppsEntity.class), any(HashMap.class), any(HashMap.class));
        
		Assertions.assertThat(_appService.search(search)).isEqualTo(builder);
	}
	
	@Test
	public void  search_StringIsNull_ReturnNull() throws Exception {

		Assertions.assertThat(_appService.search(null)).isEqualTo(null);
	}
	
	@Test
	public void ParseTaskJoinColumn_KeysStringIsNotEmptyAndKeyValuePairDoesNotExist_ReturnNull()
	{
	    Map<String,String> joinColumnMap = new HashMap<String,String>();
		String keyString= "15";
		joinColumnMap.put("appId", keyString);
		Assertions.assertThat(_appService.parseTaskJoinColumn(keyString)).isEqualTo(joinColumnMap);
	}
}

