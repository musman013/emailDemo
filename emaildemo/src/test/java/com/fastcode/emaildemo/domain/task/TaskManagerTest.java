package com.fastcode.emaildemo.domain.task;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fastcode.emaildemo.domain.model.TaskEntity;
import com.fastcode.emaildemo.domain.irepository.IAppsRepository;
import com.fastcode.emaildemo.domain.model.AppsEntity;
import com.fastcode.emaildemo.domain.irepository.ITaskRepository;
import com.fastcode.emaildemo.commons.logging.LoggingHelper;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class TaskManagerTest {

	@InjectMocks
	TaskManager _taskManager;
	
	@Mock
	ITaskRepository  _taskRepository;
    
    @Mock
	IAppsRepository  _appsRepository;
	@Mock
    private Logger loggerMock;
   
	@Mock
	private LoggingHelper logHelper;
	
	private static Long ID=15L;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(_taskManager);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void findTaskById_IdIsNotNullAndIdExists_ReturnTask() {
		TaskEntity task =mock(TaskEntity.class);

        Optional<TaskEntity> dbTask = Optional.of((TaskEntity) task);
		Mockito.<Optional<TaskEntity>>when(_taskRepository.findById(anyLong())).thenReturn(dbTask);
		Assertions.assertThat(_taskManager.findById(ID)).isEqualTo(task);
	}

	@Test 
	public void findTaskById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

	    Mockito.<Optional<TaskEntity>>when(_taskRepository.findById(anyLong())).thenReturn(Optional.empty());
		Assertions.assertThat(_taskManager.findById(ID)).isEqualTo(null);
	}
	
	@Test
	public void createTask_TaskIsNotNullAndTaskDoesNotExist_StoreTask() {

		TaskEntity task =mock(TaskEntity.class);
		Mockito.when(_taskRepository.save(any(TaskEntity.class))).thenReturn(task);
		Assertions.assertThat(_taskManager.create(task)).isEqualTo(task);
	}

	@Test
	public void deleteTask_TaskExists_RemoveTask() {

		TaskEntity task =mock(TaskEntity.class);
		_taskManager.delete(task);
		verify(_taskRepository).delete(task);
	}

	@Test
	public void updateTask_TaskIsNotNullAndTaskExists_UpdateTask() {
		
		TaskEntity task =mock(TaskEntity.class);
		Mockito.when(_taskRepository.save(any(TaskEntity.class))).thenReturn(task);
		Assertions.assertThat(_taskManager.update(task)).isEqualTo(task);
		
	}

	@Test
	public void findAll_PageableIsNotNull_ReturnPage() {
		Page<TaskEntity> task = mock(Page.class);
		Pageable pageable = mock(Pageable.class);
		Predicate predicate = mock(Predicate.class);

		Mockito.when(_taskRepository.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(task);
		Assertions.assertThat(_taskManager.findAll(predicate,pageable)).isEqualTo(task);
	}
	
    //Apps
	@Test
	public void getApps_if_TaskIdIsNotNull_returnApps() {

		TaskEntity task = mock(TaskEntity.class);
		AppsEntity apps = mock(AppsEntity.class);
		
        Optional<TaskEntity> dbTask = Optional.of((TaskEntity) task);
		Mockito.<Optional<TaskEntity>>when(_taskRepository.findById(anyLong())).thenReturn(dbTask);
		Mockito.when(task.getApps()).thenReturn(apps);
		Assertions.assertThat(_taskManager.getApps(ID)).isEqualTo(apps);

	}
	
}
