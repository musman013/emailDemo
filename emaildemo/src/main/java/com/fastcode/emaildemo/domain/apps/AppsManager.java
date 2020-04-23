package com.fastcode.emaildemo.domain.apps;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.fastcode.emaildemo.domain.model.AppsEntity;
import com.fastcode.emaildemo.domain.irepository.ITaskRepository;
import com.fastcode.emaildemo.domain.irepository.IAppsRepository;
import com.querydsl.core.types.Predicate;

@Repository
public class AppsManager implements IAppsManager {

    @Autowired
    IAppsRepository  _appsRepository;
    
    @Autowired
	ITaskRepository  _taskRepository;
    
	public AppsEntity create(AppsEntity apps) {

		return _appsRepository.save(apps);
	}

	public void delete(AppsEntity apps) {

		_appsRepository.delete(apps);	
	}

	public AppsEntity update(AppsEntity apps) {

		return _appsRepository.save(apps);
	}

	public AppsEntity findById(Long appsId) {
    	Optional<AppsEntity> dbApps= _appsRepository.findById(appsId);
		if(dbApps.isPresent()) {
			AppsEntity existingApps = dbApps.get();
		    return existingApps;
		} else {
		    return null;
		}

	}

	public Page<AppsEntity> findAll(Predicate predicate, Pageable pageable) {

		return _appsRepository.findAll(predicate,pageable);
	}
}
