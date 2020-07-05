package com.fastcode.emaildemo.emailbuilder.domain.datasource;

import org.json.JSONException;
import org.json.JSONObject;

import com.fastcode.emaildemo.emailbuilder.application.datasource.dto.PreviewDataSourceOutput;

public interface IDataSourceMetaManager {

	public PreviewDataSourceOutput getData(String query) throws JSONException;

}
