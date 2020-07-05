package com.fastcode.emaildemo.emailbuilder.domain.irepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.fastcode.emaildemo.emailbuilder.domain.model.DataSourceEntity;
import com.fastcode.emaildemo.emailbuilder.domain.model.EmailVariableEntity;

@RepositoryRestResource(collectionResourceRel = "dataSource", path = "dataSource")
public interface IDataSourceRepository extends JpaRepository<DataSourceEntity, Long>, QuerydslPredicateExecutor<DataSourceEntity>{


	   @Query("select e from DataSourceEntity e where e.id = ?1")
	   DataSourceEntity findById(long id);

	   @Query("select e from DataSourceEntity e where e.name = ?1")
	   DataSourceEntity findByName(String value);

	List<DataSourceEntity> findByEmailTemplateId(Long emailTemplateId);
	
}
