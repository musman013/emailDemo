package com.fastcode.emaildemo.domain.apps;

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

import com.fastcode.emaildemo.domain.model.AppsEntity;
import com.fastcode.emaildemo.domain.irepository.ITaskRepository;
import com.fastcode.emaildemo.domain.irepository.IAppsRepository;
import com.fastcode.emaildemo.commons.logging.LoggingHelper;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class AppsManagerTest {

	@InjectMocks
	AppsManager _appsManager;
	
	@Mock
	IAppsRepository  _appsRepository;
    
    @Mock
	ITaskRepository  _taskRepository;
	@Mock
    private Logger loggerMock;
   
	@Mock
	private LoggingHelper logHelper;
	
	private static Long ID=15L;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(_appsManager);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void findAppsById_IdIsNotNullAndIdExists_ReturnApps() {
		AppsEntity apps =mock(AppsEntity.class);

        Optional<AppsEntity> dbApps = Optional.of((AppsEntity) apps);
		Mockito.<Optional<AppsEntity>>when(_appsRepository.findById(anyLong())).thenReturn(dbApps);
		Assertions.assertThat(_appsManager.findById(ID)).isEqualTo(apps);
	}

	@Test 
	public void findAppsById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

	    Mockito.<Optional<AppsEntity>>when(_appsRepository.findById(anyLong())).thenReturn(Optional.empty());
		Assertions.assertThat(_appsManager.findById(ID)).isEqualTo(null);
	}
	
	@Test
	public void createApps_AppsIsNotNullAndAppsDoesNotExist_StoreApps() {

		AppsEntity apps =mock(AppsEntity.class);
		Mockito.when(_appsRepository.save(any(AppsEntity.class))).thenReturn(apps);
		Assertions.assertThat(_appsManager.create(apps)).isEqualTo(apps);
	}

	@Test
	public void deleteApps_AppsExists_RemoveApps() {

		AppsEntity apps =mock(AppsEntity.class);
		_appsManager.delete(apps);
		verify(_appsRepository).delete(apps);
	}

	@Test
	public void updateApps_AppsIsNotNullAndAppsExists_UpdateApps() {
		
		AppsEntity apps =mock(AppsEntity.class);
		Mockito.when(_appsRepository.save(any(AppsEntity.class))).thenReturn(apps);
		Assertions.assertThat(_appsManager.update(apps)).isEqualTo(apps);
		
	}

	@Test
	public void findAll_PageableIsNotNull_ReturnPage() {
		Page<AppsEntity> apps = mock(Page.class);
		Pageable pageable = mock(Pageable.class);
		Predicate predicate = mock(Predicate.class);

		Mockito.when(_appsRepository.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(apps);
		Assertions.assertThat(_appsManager.findAll(predicate,pageable)).isEqualTo(apps);
	}
	
}
