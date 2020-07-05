package com.fastcode.emaildemo.emailbuilder.domain.irepository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.fastcode.emaildemo.emailbuilder.domain.model.EmailVariableEntity;
@RepositoryRestResource(collectionResourceRel = "emailVariable", path = "emailVariable")
public interface IEmailVariableRepository extends JpaRepository<EmailVariableEntity, Long>, QuerydslPredicateExecutor<EmailVariableEntity> {

	   @Query("select e from EmailVariableEntity e where e.id = ?1")
	   EmailVariableEntity findById(long id);

	   @Query("select e from EmailVariableEntity e where e.propertyName = ?1")
	   EmailVariableEntity findByEmailName(String value);

	   @Query("select e.id from EmailVariableEntity e where e.propertyName in (?1) ")
	List<Long> findByNameIn(Set<String> allFieldsId);
	
}