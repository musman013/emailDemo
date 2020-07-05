package com.fastcode.emaildemo.emailbuilder.application.emailtemplate.dto;

import java.util.List;

import com.fastcode.emaildemo.emailbuilder.application.datasource.dto.FindDataSourceByIdOutput;
import com.fastcode.emaildemo.emailbuilder.application.datasource.dto.FindDataSourceMetaOutputForMapping;
import com.fastcode.emaildemo.emailbuilder.application.emailvariable.dto.FindEmailVariableByIdOutput;

public class FindEmailTemplateMappingDTO {

	
	private FindEmailVariableByIdOutput mergeField;
	
	private List<FindDataSourceMetaOutputForMapping> dataSourceMetaList;
}
