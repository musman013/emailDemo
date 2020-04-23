package com.fastcode.emaildemo.restcontrollers;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fastcode.emaildemo.commons.logging.LoggingHelper;
import com.fastcode.emaildemo.application.apps.AppsAppService;
import com.fastcode.emaildemo.application.apps.dto.*;
import com.fastcode.emaildemo.domain.irepository.IAppsRepository;
import com.fastcode.emaildemo.domain.model.AppsEntity;
import com.fastcode.emaildemo.application.task.TaskAppService;    

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
				properties = "spring.profiles.active=test")
public class AppsControllerTest {
	@Autowired
	private SortHandlerMethodArgumentResolver sortArgumentResolver;

	@Autowired 
	private IAppsRepository apps_repository;
	
	@SpyBean
	private AppsAppService appsAppService;
    
    @SpyBean
	private TaskAppService taskAppService;

	@SpyBean
	private LoggingHelper logHelper;

	@Mock
	private Logger loggerMock;

	private AppsEntity apps;

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
		em.createNativeQuery("drop table s1.apps CASCADE").executeUpdate();
		em.getTransaction().commit();
	}


	public AppsEntity createEntity() {
	
		AppsEntity apps = new AppsEntity();
  		apps.setArtifactId("1");
  		apps.setAuthMethod("1");
  		apps.setAuthTable("1");
		apps.setCaching(false);
		apps.setCreatedDate(new Date());
  		apps.setDescription("1");
  		apps.setDestinationPath("1");
		apps.setEmailTemplates(false);
		apps.setEntityHistory(false);
  		apps.setGroupId("1");
		apps.setId(1L);
  		apps.setJdbcPassword("1");
  		apps.setJdbcUrl("1");
  		apps.setJdbcUsername("1");
  		apps.setName("1");
		apps.setProcessAdminApp(false);
		apps.setProcessManagement(false);
		apps.setScheduler(false);
  		apps.setSchema("1");
		apps.setUpgrade(false);
		apps.setUserId(1L);
		apps.setUserOnly(false);
		
		return apps;
	}

	public CreateAppsInput createAppsInput() {
	
	    CreateAppsInput apps = new CreateAppsInput();
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
	    
		
		
		return apps;
	}

	public AppsEntity createNewEntity() {
		AppsEntity apps = new AppsEntity();
  		apps.setArtifactId("3");
  		apps.setAuthMethod("3");
  		apps.setAuthTable("3");
		apps.setCaching(false);
		apps.setCreatedDate(new Date());
  		apps.setDescription("3");
  		apps.setDestinationPath("3");
		apps.setEmailTemplates(false);
		apps.setEntityHistory(false);
  		apps.setGroupId("3");
		apps.setId(3L);
  		apps.setJdbcPassword("3");
  		apps.setJdbcUrl("3");
  		apps.setJdbcUsername("3");
  		apps.setName("3");
		apps.setProcessAdminApp(false);
		apps.setProcessManagement(false);
		apps.setScheduler(false);
  		apps.setSchema("3");
		apps.setUpgrade(false);
		apps.setUserId(3L);
		apps.setUserOnly(false);
		return apps;
	}
	

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final AppsController appsController = new AppsController(appsAppService,taskAppService,
	logHelper);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());

		this.mvc = MockMvcBuilders.standaloneSetup(appsController)
				.setCustomArgumentResolvers(sortArgumentResolver)
				.setControllerAdvice()
				.build();
	}

	@Before
	public void initTest() {

		apps= createEntity();
		List<AppsEntity> list= apps_repository.findAll();
		if(!list.contains(apps)) {
			apps=apps_repository.save(apps);
		}

	}

	@Test
	public void FindById_IdIsValid_ReturnStatusOk() throws Exception {
	
		mvc.perform(get("/apps/" + apps.getId())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}  

	@Test
	public void FindById_IdIsNotValid_ReturnStatusNotFound() throws Exception {

		mvc.perform(get("/apps/111")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());

	}    
	@Test
	public void CreateApps_AppsDoesNotExist_ReturnStatusOk() throws Exception {
		CreateAppsInput apps = createAppsInput();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(apps);

		mvc.perform(post("/apps").contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

	}     

	@Test
	public void DeleteApps_IdIsNotValid_ThrowEntityNotFoundException() throws Exception {

        doReturn(null).when(appsAppService).findById(111L);
        org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(delete("/apps/111")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("There does not exist a apps with a id=111"));

	}  

	@Test
	public void Delete_IdIsValid_ReturnStatusNoContent() throws Exception {
	
	 AppsEntity entity =  createNewEntity();
		
		entity = apps_repository.save(entity);

		FindAppsByIdOutput output= new FindAppsByIdOutput();
  		output.setArtifactId(entity.getArtifactId());
  		output.setDestinationPath(entity.getDestinationPath());
  		output.setGroupId(entity.getGroupId());
  		output.setId(entity.getId());
  		output.setJdbcPassword(entity.getJdbcPassword());
  		output.setJdbcUrl(entity.getJdbcUrl());
  		output.setJdbcUsername(entity.getJdbcUsername());
        Mockito.when(appsAppService.findById(entity.getId())).thenReturn(output);
        
		mvc.perform(delete("/apps/" + entity.getId())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
	}  


	@Test
	public void UpdateApps_AppsDoesNotExist_ReturnStatusNotFound() throws Exception {

        doReturn(null).when(appsAppService).findById(111L);

		UpdateAppsInput apps = new UpdateAppsInput();
  		apps.setArtifactId("111");
  		apps.setAuthMethod("111");
  		apps.setAuthTable("111");
		apps.setCaching(true);
		apps.setCreatedDate(new Date());
  		apps.setDescription("111");
  		apps.setDestinationPath("111");
		apps.setEmailTemplates(true);
		apps.setEntityHistory(true);
  		apps.setGroupId("111");
		apps.setId(111L);
  		apps.setJdbcPassword("111");
  		apps.setJdbcUrl("111");
  		apps.setJdbcUsername("111");
  		apps.setName("111");
		apps.setProcessAdminApp(true);
		apps.setProcessManagement(true);
		apps.setScheduler(true);
  		apps.setSchema("111");
		apps.setUpgrade(true);
		apps.setUserId(111L);
		apps.setUserOnly(true);

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(apps);
		mvc.perform(put("/apps/111").contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isNotFound());

	}    

	@Test
	public void UpdateApps_AppsExists_ReturnStatusOk() throws Exception {
		AppsEntity entity =  createNewEntity();
		entity = apps_repository.save(entity);
		FindAppsByIdOutput output= new FindAppsByIdOutput();
  		output.setArtifactId(entity.getArtifactId());
  		output.setAuthMethod(entity.getAuthMethod());
  		output.setAuthTable(entity.getAuthTable());
  		output.setCaching(entity.getCaching());
  		output.setCreatedDate(entity.getCreatedDate());
  		output.setDescription(entity.getDescription());
  		output.setDestinationPath(entity.getDestinationPath());
  		output.setEmailTemplates(entity.getEmailTemplates());
  		output.setEntityHistory(entity.getEntityHistory());
  		output.setGroupId(entity.getGroupId());
  		output.setId(entity.getId());
  		output.setJdbcPassword(entity.getJdbcPassword());
  		output.setJdbcUrl(entity.getJdbcUrl());
  		output.setJdbcUsername(entity.getJdbcUsername());
  		output.setName(entity.getName());
  		output.setProcessAdminApp(entity.getProcessAdminApp());
  		output.setProcessManagement(entity.getProcessManagement());
  		output.setScheduler(entity.getScheduler());
  		output.setSchema(entity.getSchema());
  		output.setUpgrade(entity.getUpgrade());
  		output.setUserId(entity.getUserId());
  		output.setUserOnly(entity.getUserOnly());
        Mockito.when(appsAppService.findById(entity.getId())).thenReturn(output);
        
		UpdateAppsInput apps = new UpdateAppsInput();
  		apps.setArtifactId(entity.getArtifactId());
  		apps.setDestinationPath(entity.getDestinationPath());
  		apps.setGroupId(entity.getGroupId());
  		apps.setId(entity.getId());
  		apps.setJdbcPassword(entity.getJdbcPassword());
  		apps.setJdbcUrl(entity.getJdbcUrl());
  		apps.setJdbcUsername(entity.getJdbcUsername());
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(apps);
	
		mvc.perform(put("/apps/" + entity.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

		AppsEntity de = createEntity();
		de.setId(entity.getId());
		apps_repository.delete(de);

	}    
	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsValid_ReturnStatusOk() throws Exception {

		mvc.perform(get("/apps?search=id[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}    

	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsNotValid_ThrowException() throws Exception {

		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/apps?search=appsid[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property appsid not found!"));

	} 
	
	@Test
	public void GetTask_searchIsNotEmptyAndPropertyIsNotValid_ThrowException() throws Exception {
	
		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("appId", "1");

		Mockito.when(appsAppService.parseTaskJoinColumn("appId")).thenReturn(joinCol);
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/apps/1/task?search=abc[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property abc not found!"));
	
	}    
	
	@Test
	public void GetTask_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
	
		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("appId", "1");
		
        Mockito.when(appsAppService.parseTaskJoinColumn("appId")).thenReturn(joinCol);
		mvc.perform(get("/apps/1/task?search=appId[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}  
	
	@Test
	public void GetTask_searchIsNotEmpty() throws Exception {
	
		Mockito.when(appsAppService.parseTaskJoinColumn(anyString())).thenReturn(null);
		mvc.perform(get("/apps/1/task?search=appid[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isNotFound());
	}    
    

}
