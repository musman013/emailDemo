package com.fastcode.emaildemo.emailbuilder.application.datasource.dto;

import java.util.List;

import com.fastcode.emaildemo.emailbuilder.application.emailvariable.dto.FindEmailVariableByIdOutput;

public class FindDataSourceMetaOutputForMapping {

	
	private int totalMergeField;
	
	private int mappedMergeField;
	
	private FindEmailVariableByIdOutput mergeField;
	
	private List<DataSourceMetaOutput> dataSourceMetaList;
	
	private List<DataSourceMetaOutput> alreadyMappedList;

	
	
	public int getTotalMergeField() {
		return totalMergeField;
	}

	public void setTotalMergeField(int totalMergeField) {
		this.totalMergeField = totalMergeField;
	}

	public int getMappedMergeField() {
		return mappedMergeField;
	}

	public void setMappedMergeField(int mappedMergeField) {
		this.mappedMergeField = mappedMergeField;
	}

	public FindEmailVariableByIdOutput getMergeField() {
		return mergeField;
	}

	public void setMergeField(FindEmailVariableByIdOutput mergeField) {
		this.mergeField = mergeField;
	}

	public List<DataSourceMetaOutput> getDataSourceMetaList() {
		return dataSourceMetaList;
	}

	public void setDataSourceMetaList(List<DataSourceMetaOutput> dataSourceMetaList) {
		this.dataSourceMetaList = dataSourceMetaList;
	}

	public List<DataSourceMetaOutput> getAlreadyMappedList() {
		return alreadyMappedList;
	}

	public void setAlreadyMappedList(List<DataSourceMetaOutput> alreadyMappedList) {
		this.alreadyMappedList = alreadyMappedList;
	}
	
	
	
	
	
}
