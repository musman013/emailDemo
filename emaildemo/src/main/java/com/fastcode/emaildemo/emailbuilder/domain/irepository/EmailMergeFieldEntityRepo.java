package com.fastcode.emaildemo.emailbuilder.domain.irepository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.fastcode.emaildemo.emailbuilder.domain.model.DataSourceEntity;
import com.fastcode.emaildemo.emailbuilder.domain.model.DataSourceMetaEntity;
import com.fastcode.emaildemo.emailbuilder.domain.model.EmailMergeFieldEntity;
import com.fastcode.emaildemo.emailbuilder.domain.model.EmailTemplateEntity;

public interface EmailMergeFieldEntityRepo extends JpaRepository<EmailMergeFieldEntity, Long>, QuerydslPredicateExecutor<EmailMergeFieldEntity>{

	List<EmailMergeFieldEntity> findByEmailTemplateId(Long emailTemplateId);

	@Transactional
	@Modifying
	@Query(value ="delete from  EmailMergeFieldEntity e where e.emailTemplate = ?1")
	void updateEmailMergeField(EmailTemplateEntity email);
	
	
	@Query(value ="select e.mergeField.propertyName from  EmailMergeFieldEntity e where e.emailTemplate.id = ?1")
	List<String> getPropertyNames(Long email);

}
