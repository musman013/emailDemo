package com.fastcode.emaildemo.restcontrollers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fastcode.emaildemo.application.apps.AppsAppService;
import com.fastcode.emaildemo.application.task.TaskAppService;
import com.fastcode.emaildemo.application.task.dto.CreateTaskInput;
import com.fastcode.emaildemo.application.task.dto.FindTaskByIdOutput;
import com.fastcode.emaildemo.application.task.dto.UpdateTaskInput;
import com.fastcode.emaildemo.commons.logging.LoggingHelper;
import com.fastcode.emaildemo.domain.irepository.IAppsRepository;
import com.fastcode.emaildemo.domain.irepository.ITaskRepository;
import com.fastcode.emaildemo.domain.model.AppsEntity;
import com.fastcode.emaildemo.domain.model.TaskEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;    

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
				properties = "spring.profiles.active=test")
public class TaskControllerTest {
	@Autowired
	private SortHandlerMethodArgumentResolver sortArgumentResolver;

	@Autowired 
	private ITaskRepository task_repository;
	
	@Autowired 
	private IAppsRepository appsRepository;
	
	@SpyBean
	private TaskAppService taskAppService;
    
    @SpyBean
	private AppsAppService appsAppService;

	@SpyBean
	private LoggingHelper logHelper;

	@Mock
	private Logger loggerMock;

	private TaskEntity task;

	private MockMvc mvc;
	
	@Autowired
	EntityManagerFactory emf;
	
    static EntityManagerFactory emfs;
	
	@PostConstruct
	public void init() {
	this.emfs = emf;
	}

	@AfterClass
	public static void cleanup() {
		EntityManager em = emfs.createEntityManager();
		em.getTransaction().begin();
		em.createNativeQuery("drop table s1.task CASCADE").executeUpdate();
		em.createNativeQuery("drop table s1.apps CASCADE").executeUpdate();
		em.getTransaction().commit();
	}


	public TaskEntity createEntity() {
		AppsEntity apps = createAppsEntity();
		if(!appsRepository.findAll().contains(apps))
		{
			apps=appsRepository.save(apps);
		}
	
		TaskEntity task = new TaskEntity();
  		task.setDescription("1");
		task.setId(1L);
  		task.setStatus("1");
  		task.setType("1");
		task.setApps(apps);
		
		return task;
	}

	public CreateTaskInput createTaskInput() {
	
	    CreateTaskInput task = new CreateTaskInput();
  		task.setDescription("2");
  		task.setStatus("2");
  		task.setType("2");
	    
		AppsEntity apps = new AppsEntity();
  		apps.setArtifactId("2");
  		apps.setAuthMethod("2");
  		apps.setAuthTable("2");
		apps.setCaching(false);
		apps.setCreatedDate(new Date());
  		apps.setDescription("2");
  		apps.setDestinationPath("2");
		apps.setEmailTemplates(false);
		apps.setEntityHistory(false);
  		apps.setGroupId("2");
		apps.setId(2L);
  		apps.setJdbcPassword("2");
  		apps.setJdbcUrl("2");
  		apps.setJdbcUsername("2");
  		apps.setName("2");
		apps.setProcessAdminApp(false);
		apps.setProcessManagement(false);
		apps.setScheduler(false);
  		apps.setSchema("2");
		apps.setUpgrade(false);
		apps.setUserId(2L);
		apps.setUserOnly(false);
		apps=appsRepository.save(apps);
		task.setAppId(apps.getId());
		
		
		return task;
	}

	public TaskEntity createNewEntity() {
		TaskEntity task = new TaskEntity();
  		task.setDescription("3");
		task.setId(3L);
  		task.setStatus("3");
  		task.setType("3");
		return task;
	}
	
	public AppsEntity createAppsEntity() {
		AppsEntity apps = new AppsEntity();
  		apps.setArtifactId("1");
  		apps.setAuthMethod("1");
  		apps.setAuthTable("1");
		apps.setCaching(true);
		apps.setCreatedDate(new Date());
  		apps.setDescription("1");
  		apps.setDestinationPath("1");
		apps.setEmailTemplates(true);
		apps.setEntityHistory(true);
  		apps.setGroupId("1");
		apps.setId(1L);
  		apps.setJdbcPassword("1");
  		apps.setJdbcUrl("1");
  		apps.setJdbcUsername("1");
  		apps.setName("1");
		apps.setProcessAdminApp(true);
		apps.setProcessManagement(true);
		apps.setScheduler(true);
  		apps.setSchema("1");
		apps.setUpgrade(true);
		apps.setUserId(1L);
		apps.setUserOnly(true);
		return apps;
		 
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final TaskController taskController = new TaskController(taskAppService,appsAppService,
	logHelper);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());

		this.mvc = MockMvcBuilders.standaloneSetup(taskController)
				.setCustomArgumentResolvers(sortArgumentResolver)
				.setControllerAdvice()
				.build();
	}

	@Before
	public void initTest() {

		task= createEntity();
		List<TaskEntity> list= task_repository.findAll();
		if(!list.contains(task)) {
			task=task_repository.save(task);
		}

	}

	@Test
	public void FindById_IdIsValid_ReturnStatusOk() throws Exception {
	
		mvc.perform(get("/task/" + task.getId())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}  

	@Test
	public void FindById_IdIsNotValid_ReturnStatusNotFound() throws Exception {

		mvc.perform(get("/task/111")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());

	}    
	@Test
	public void CreateTask_TaskDoesNotExist_ReturnStatusOk() throws Exception {
		CreateTaskInput task = createTaskInput();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(task);

		mvc.perform(post("/task").contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

	}     

	@Test
	public void DeleteTask_IdIsNotValid_ThrowEntityNotFoundException() throws Exception {

        doReturn(null).when(taskAppService).findById(111L);
        org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(delete("/task/111")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("There does not exist a task with a id=111"));

	}  

	@Test
	public void Delete_IdIsValid_ReturnStatusNoContent() throws Exception {
	
	 TaskEntity entity =  createNewEntity();
		AppsEntity apps = new AppsEntity();
  		apps.setArtifactId("3");
  		apps.setAuthMethod("3");
  		apps.setAuthTable("3");
		apps.setCaching(true);
		apps.setCreatedDate(new Date());
  		apps.setDescription("3");
  		apps.setDestinationPath("3");
		apps.setEmailTemplates(true);
		apps.setEntityHistory(true);
  		apps.setGroupId("3");
		apps.setId(3L);
  		apps.setJdbcPassword("3");
  		apps.setJdbcUrl("3");
  		apps.setJdbcUsername("3");
  		apps.setName("3");
		apps.setProcessAdminApp(true);
		apps.setProcessManagement(true);
		apps.setScheduler(true);
  		apps.setSchema("3");
		apps.setUpgrade(true);
		apps.setUserId(3L);
		apps.setUserOnly(true);
		apps=appsRepository.save(apps);
		
		entity.setApps(apps);
		
		entity = task_repository.save(entity);

		FindTaskByIdOutput output= new FindTaskByIdOutput();
  		output.setId(entity.getId());
        Mockito.when(taskAppService.findById(entity.getId())).thenReturn(output);
        
		mvc.perform(delete("/task/" + entity.getId())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
	}  


	@Test
	public void UpdateTask_TaskDoesNotExist_ReturnStatusNotFound() throws Exception {

        doReturn(null).when(taskAppService).findById(111L);

		UpdateTaskInput task = new UpdateTaskInput();
  		task.setDescription("111");
		task.setId(111L);
  		task.setStatus("111");
  		task.setType("111");

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(task);
		mvc.perform(put("/task/111").contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isNotFound());

	}    

	@Test
	public void UpdateTask_TaskExists_ReturnStatusOk() throws Exception {
		TaskEntity entity =  createNewEntity();
		AppsEntity apps = new AppsEntity();
  		apps.setArtifactId("5");
  		apps.setAuthMethod("5");
  		apps.setAuthTable("5");
		apps.setCaching(true);
		apps.setCreatedDate(new Date());
  		apps.setDescription("5");
  		apps.setDestinationPath("5");
		apps.setEmailTemplates(true);
		apps.setEntityHistory(true);
  		apps.setGroupId("5");
		apps.setId(5L);
  		apps.setJdbcPassword("5");
  		apps.setJdbcUrl("5");
  		apps.setJdbcUsername("5");
  		apps.setName("5");
		apps.setProcessAdminApp(true);
		apps.setProcessManagement(true);
		apps.setScheduler(true);
  		apps.setSchema("5");
		apps.setUpgrade(true);
		apps.setUserOnly(true);
		apps=appsRepository.save(apps);
		entity.setApps(apps);
		entity = task_repository.save(entity);
		FindTaskByIdOutput output= new FindTaskByIdOutput();
  		output.setDescription(entity.getDescription());
  		output.setId(entity.getId());
  		output.setStatus(entity.getStatus());
  		output.setType(entity.getType());
        Mockito.when(taskAppService.findById(entity.getId())).thenReturn(output);
        
		UpdateTaskInput task = new UpdateTaskInput();
  		task.setId(entity.getId());
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(task);
	
		mvc.perform(put("/task/" + entity.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

		TaskEntity de = createEntity();
		de.setId(entity.getId());
		task_repository.delete(de);

	}    
	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsValid_ReturnStatusOk() throws Exception {

		mvc.perform(get("/task?search=id[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}    

	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsNotValid_ThrowException() throws Exception {

		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/task?search=taskid[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property taskid not found!"));

	} 
	
	@Test
	public void GetApps_IdIsNotEmptyAndIdDoesNotExist_ReturnNotFound() throws Exception {
	
	    mvc.perform(get("/task/111/apps")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isNotFound());
	
	}    
	
	@Test
	public void GetApps_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
	
	   mvc.perform(get("/task/" + task.getId()+ "/apps")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}  
    

}
