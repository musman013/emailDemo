package com.fastcode.emaildemo.emailbuilder.application.datasource;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.fastcode.emaildemo.emailbuilder.application.datasource.dto.CreateDataSourceInput;
import com.fastcode.emaildemo.emailbuilder.application.datasource.dto.CreateDataSourceOutput;
import com.fastcode.emaildemo.emailbuilder.application.datasource.dto.DataSourceMetaInput;
import com.fastcode.emaildemo.emailbuilder.application.datasource.dto.FindDataSourceByIdOutput;
import com.fastcode.emaildemo.emailbuilder.application.datasource.dto.FindDataSourceByNameOutput;
import com.fastcode.emaildemo.emailbuilder.application.datasource.dto.UpdateDataSourceInput;
import com.fastcode.emaildemo.emailbuilder.application.datasource.dto.UpdateDataSourceOutput;
import com.fastcode.emaildemo.emailbuilder.domain.model.DataSourceEntity;
import com.fastcode.emaildemo.emailbuilder.domain.model.DataSourceMetaEntity;

@Mapper(componentModel = "spring")
public interface DataSourceMapper {

	  DataSourceEntity createDataSourceInputToDataSourceEntity(CreateDataSourceInput emailDto);

	    CreateDataSourceOutput dataSourceEntityToCreateDataSourceOutput(DataSourceEntity entity);

	    DataSourceEntity updateDataSourceInputToDataSourceEntity(UpdateDataSourceInput emailDto);

	    UpdateDataSourceOutput dataSourceEntityToUpdateDataSourceOutput(DataSourceEntity entity);

	   
	    FindDataSourceByIdOutput dataSourceEntityToFindDataSourceByIdOutput(DataSourceEntity entity);

	    FindDataSourceByNameOutput dataSourceEntityToFindDataSourceByNameOutput(DataSourceEntity entity);
	    
	   
	    List<DataSourceMetaInput> dataSourceMetaEntityToDataSourceMetaInput(List<DataSourceMetaEntity> entities);
}
