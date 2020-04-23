package com.fastcode.emaildemo.domain.apps;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.validation.constraints.Positive;
import com.fastcode.emaildemo.domain.model.AppsEntity;
import com.fastcode.emaildemo.domain.model.TaskEntity;

public interface IAppsManager {
    // CRUD Operations
    AppsEntity create(AppsEntity apps);

    void delete(AppsEntity apps);

    AppsEntity update(AppsEntity apps);

    AppsEntity findById(Long id);
	
    Page<AppsEntity> findAll(Predicate predicate, Pageable pageable);
}
