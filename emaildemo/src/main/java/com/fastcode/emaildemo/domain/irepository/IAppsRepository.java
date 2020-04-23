package com.fastcode.emaildemo.domain.irepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.fastcode.emaildemo.domain.model.TaskEntity; 
import java.util.List;
import com.fastcode.emaildemo.domain.model.AppsEntity;
@RepositoryRestResource(collectionResourceRel = "apps", path = "apps")
public interface IAppsRepository extends JpaRepository<AppsEntity, Long>,QuerydslPredicateExecutor<AppsEntity> {

}
