package com.fastcode.emaildemo.emailbuilder.domain.datasource;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fastcode.emaildemo.emailbuilder.application.datasource.dto.UpdateDataSourceInput;
import com.fastcode.emaildemo.emailbuilder.domain.model.DataSourceEntity;
import com.querydsl.core.types.Predicate;

public interface IDataSourceManager {

	 // CRUD Operations
    public DataSourceEntity create(DataSourceEntity email);

    public boolean delete(DataSourceEntity email);

    public DataSourceEntity update(DataSourceEntity email);

    public DataSourceEntity findById(Long emailId);
    
    public DataSourceEntity findByName (String name);

    public Page<DataSourceEntity> findAll(Predicate predicate,Pageable pageable);

	public List<DataSourceEntity> findAll();

	public DataSourceEntity update(Long eid, UpdateDataSourceInput dataSource);

	public String getAllMappedMergeField(Long id);

	public String getAllMappedForEmailTemplate(Long id);
}
