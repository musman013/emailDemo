package com.fastcode.emaildemo.emailbuilder.domain.irepository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.fastcode.emaildemo.emailbuilder.domain.model.DataSourceEntity;
import com.fastcode.emaildemo.emailbuilder.domain.model.DataSourceMetaEntity;
import com.fastcode.emaildemo.emailbuilder.domain.model.EmailTemplateMappingEntity;
import com.fastcode.emaildemo.emailbuilder.domain.model.EmailVariableEntity;

public interface IEmailTemplateMappingRepo extends JpaRepository<EmailTemplateMappingEntity, Long>, QuerydslPredicateExecutor<EmailTemplateMappingEntity> {

	@Transactional
	@Modifying
	@Query(value="delete from EmailTemplateMappingEntity e where  e.emailTemplateEntity.id=?1")
	void updatePreviousMappig(Long emailTemplateId);

	@Query(value="select e.dataSourceMetaEntity from EmailTemplateMappingEntity e where e.emailTemplateEntity.id=?1 and e.mergeField.id=?2")
	List<DataSourceMetaEntity> findByEmailTemplateEntityIdAndMergeFieldId(Long emailTemplateId, Long id);

	
	boolean existsByDataSourceEntiry(DataSourceEntity dataSourceEntity);

	@Query(value="select count(distinct e.mergeField) from EmailTemplateMappingEntity e where e.emailTemplateEntity.id=?1  ")
	int getMappedTillNowDataSource(Long emailTemplateId);

	@Query(value="select distinct (e.dataSourceMetaEntity.metaColumn) from EmailTemplateMappingEntity e where e.dataSourceEntiry.id=?1  ")
	List<String> getAllMappedEmailTemplate(Long id);

	@Query(value="select distinct (e.mergeField.propertyName) from EmailTemplateMappingEntity e where e.emailTemplateEntity.id=?1  ")
	List<String> getAllMappedForEmailTemplate(Long id);

}
