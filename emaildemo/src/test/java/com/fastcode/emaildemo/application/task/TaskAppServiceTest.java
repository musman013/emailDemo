package com.fastcode.emaildemo.application.task;

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

import com.fastcode.emaildemo.application.task.dto.CreateTaskInput;
import com.fastcode.emaildemo.application.task.dto.FindTaskByIdOutput;
import com.fastcode.emaildemo.application.task.dto.UpdateTaskInput;
import com.fastcode.emaildemo.commons.logging.LoggingHelper;
import com.fastcode.emaildemo.commons.search.SearchCriteria;
import com.fastcode.emaildemo.commons.search.SearchFields;
import com.fastcode.emaildemo.domain.apps.AppsManager;
import com.fastcode.emaildemo.domain.model.AppsEntity;
import com.fastcode.emaildemo.domain.model.QTaskEntity;
import com.fastcode.emaildemo.domain.model.TaskEntity;
import com.fastcode.emaildemo.domain.task.TaskManager;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class TaskAppServiceTest {

	@InjectMocks
	@Spy
	TaskAppService _appService;

	@Mock
	private TaskManager _taskManager;
	
    @Mock
	private AppsManager  _appsManager;
	
	@Mock
	private TaskMapper _mapper;

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
	public void findTaskById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

		Mockito.when(_taskManager.findById(anyLong())).thenReturn(null);
		Assertions.assertThat(_appService.findById(ID)).isEqualTo(null);
	}
	
	@Test
	public void findTaskById_IdIsNotNullAndIdExists_ReturnTask() {

		TaskEntity task = mock(TaskEntity.class);
		Mockito.when(_taskManager.findById(anyLong())).thenReturn(task);
		Assertions.assertThat(_appService.findById(ID)).isEqualTo(_mapper.taskEntityToFindTaskByIdOutput(task));
	}
	
	 @Test 
    public void createTask_TaskIsNotNullAndTaskDoesNotExist_StoreTask() { 
 
       TaskEntity taskEntity = mock(TaskEntity.class); 
       CreateTaskInput task = new CreateTaskInput();
   
		AppsEntity apps= mock(AppsEntity.class);
        task.setAppId(Long.valueOf(ID));
		Mockito.when(_appsManager.findById(
        any(Long.class))).thenReturn(apps);
		
        Mockito.when(_mapper.createTaskInputToTaskEntity(any(CreateTaskInput.class))).thenReturn(taskEntity); 
        Mockito.when(_taskManager.create(any(TaskEntity.class))).thenReturn(taskEntity);
      
        Assertions.assertThat(_appService.create(task)).isEqualTo(_mapper.taskEntityToCreateTaskOutput(taskEntity)); 
    } 
    @Test
	public void createTask_TaskIsNotNullAndTaskDoesNotExistAndChildIsNullAndChildIsNotMandatory_StoreTask() {

		TaskEntity taskEntity = mock(TaskEntity.class);
		CreateTaskInput task = mock(CreateTaskInput.class);
		
		Mockito.when(_mapper.createTaskInputToTaskEntity(any(CreateTaskInput.class))).thenReturn(taskEntity);
		Mockito.when(_taskManager.create(any(TaskEntity.class))).thenReturn(taskEntity);
		Assertions.assertThat(_appService.create(task)).isEqualTo(_mapper.taskEntityToCreateTaskOutput(taskEntity));

	}
	
    @Test
	public void updateTask_TaskIsNotNullAndTaskDoesNotExistAndChildIsNullAndChildIsNotMandatory_ReturnUpdatedTask() {

		TaskEntity taskEntity = mock(TaskEntity.class);
		UpdateTaskInput task = mock(UpdateTaskInput.class);
		
		Mockito.when(_mapper.updateTaskInputToTaskEntity(any(UpdateTaskInput.class))).thenReturn(taskEntity);
		Mockito.when(_taskManager.update(any(TaskEntity.class))).thenReturn(taskEntity);
		Assertions.assertThat(_appService.update(ID,task)).isEqualTo(_mapper.taskEntityToUpdateTaskOutput(taskEntity));
	}
	
		
	@Test
	public void updateTask_TaskIdIsNotNullAndIdExists_ReturnUpdatedTask() {

		TaskEntity taskEntity = mock(TaskEntity.class);
		UpdateTaskInput task= mock(UpdateTaskInput.class);
		
		Mockito.when(_mapper.updateTaskInputToTaskEntity(any(UpdateTaskInput.class))).thenReturn(taskEntity);
		Mockito.when(_taskManager.update(any(TaskEntity.class))).thenReturn(taskEntity);
		Assertions.assertThat(_appService.update(ID,task)).isEqualTo(_mapper.taskEntityToUpdateTaskOutput(taskEntity));
	}
    
	@Test
	public void deleteTask_TaskIsNotNullAndTaskExists_TaskRemoved() {

		TaskEntity task= mock(TaskEntity.class);
		Mockito.when(_taskManager.findById(anyLong())).thenReturn(task);
		
		_appService.delete(ID); 
		verify(_taskManager).delete(task);
	}
	
	@Test
	public void find_ListIsEmpty_ReturnList() throws Exception {

		List<TaskEntity> list = new ArrayList<>();
		Page<TaskEntity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindTaskByIdOutput> output = new ArrayList<>();
		SearchCriteria search= new SearchCriteria();
//		search.setType(1);
//		search.setValue("xyz");
//		search.setOperator("equals");

		Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
		Mockito.when(_taskManager.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void find_ListIsNotEmpty_ReturnList() throws Exception {

		List<TaskEntity> list = new ArrayList<>();
		TaskEntity task = mock(TaskEntity.class);
		list.add(task);
    	Page<TaskEntity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindTaskByIdOutput> output = new ArrayList<>();
        SearchCriteria search= new SearchCriteria();
//		search.setType(1);
//		search.setValue("xyz");
//		search.setOperator("equals");
		output.add(_mapper.taskEntityToFindTaskByIdOutput(task));
		
		Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
    	Mockito.when(_taskManager.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void searchKeyValuePair_PropertyExists_ReturnBooleanBuilder() {
		QTaskEntity task = QTaskEntity.taskEntity;
	    SearchFields searchFields = new SearchFields();
		searchFields.setOperator("equals");
		searchFields.setSearchValue("xyz");
	    Map<String,SearchFields> map = new HashMap<>();
        map.put("description",searchFields);
		 Map<String,String> searchMap = new HashMap<>();
        searchMap.put("xyz",String.valueOf(ID));
		BooleanBuilder builder = new BooleanBuilder();
         builder.and(task.description.eq("xyz"));
		Assertions.assertThat(_appService.searchKeyValuePair(task,map,searchMap)).isEqualTo(builder);
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
        list.add("description");
        list.add("status");
        list.add("type");
		_appService.checkProperties(list);
	}
	
	@Test
	public void  search_SearchIsNotNullAndSearchContainsCaseThree_ReturnBooleanBuilder() throws Exception {
	
		Map<String,SearchFields> map = new HashMap<>();
		QTaskEntity task = QTaskEntity.taskEntity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(3);
		search.setValue("xyz");
		search.setOperator("equals");
        fields.setFieldName("description");
        fields.setOperator("equals");
		fields.setSearchValue("xyz");
        fieldsList.add(fields);
        search.setFields(fieldsList);
		BooleanBuilder builder = new BooleanBuilder();
        builder.or(task.description.eq("xyz"));
        Mockito.doNothing().when(_appService).checkProperties(any(List.class));
		Mockito.doReturn(builder).when(_appService).searchKeyValuePair(any(QTaskEntity.class), any(HashMap.class), any(HashMap.class));
        
		Assertions.assertThat(_appService.search(search)).isEqualTo(builder);
	}
	
	@Test
	public void  search_StringIsNull_ReturnNull() throws Exception {

		Assertions.assertThat(_appService.search(null)).isEqualTo(null);
	}
   
   //Apps
	@Test
	public void GetApps_IfTaskIdAndAppsIdIsNotNullAndTaskExists_ReturnApps() {
		TaskEntity task = mock(TaskEntity.class);
		AppsEntity apps = mock(AppsEntity.class);

		Mockito.when(_taskManager.findById(anyLong())).thenReturn(task);
		Mockito.when(_taskManager.getApps(anyLong())).thenReturn(apps);
		Assertions.assertThat(_appService.getApps(ID)).isEqualTo(_mapper.appsEntityToGetAppsOutput(apps, task));
	}

	@Test 
	public void GetApps_IfTaskIdAndAppsIdIsNotNullAndTaskDoesNotExist_ReturnNull() {
		Mockito.when(_taskManager.findById(anyLong())).thenReturn(null);
		Assertions.assertThat(_appService.getApps(ID)).isEqualTo(null);
	}
	
}

