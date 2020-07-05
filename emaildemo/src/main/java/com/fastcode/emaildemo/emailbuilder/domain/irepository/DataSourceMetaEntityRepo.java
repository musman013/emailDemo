package com.fastcode.emaildemo.emailbuilder.domain.irepository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.fastcode.emaildemo.emailbuilder.application.datasource.dto.DataSourceMetaInput;
import com.fastcode.emaildemo.emailbuilder.domain.model.DataSourceEntity;
import com.fastcode.emaildemo.emailbuilder.domain.model.DataSourceMetaEntity;

public interface DataSourceMetaEntityRepo extends JpaRepository<DataSourceMetaEntity, Long>, QuerydslPredicateExecutor<DataSourceEntity>{

	@Modifying
	@Transactional
	@Query("delete from DataSourceMetaEntity d where d.dataSourceEntity =?1  ")
	void deleteAllMeta(DataSourceEntity oldsourceEntity);
	
	@Modifying
	@Transactional
	@Query("delete from DataSourceMetaEntity d where d.dataSourceEntity in (?1)  ")
	void deleteAllMetaList(List<DataSourceEntity> oldsourceEntity);
	
	List<DataSourceMetaEntity> findByDataSourceEntityId(Long id);

	List<DataSourceMetaEntity> findByMetaColumnDataTypeAndDataSourceEntityIn(String propertyType,
			List<DataSourceEntity> dataSourceEntityList);

	List<DataSourceMetaEntity> findByMetaColumnDataTypeNotInAndDataSourceEntityIn(List<String> excludedString,
			List<DataSourceEntity> dataSourceEntityList);

	
}