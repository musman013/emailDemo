package com.fastcode.emaildemo.emailbuilder.application.emailvariable;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.fastcode.emaildemo.emailbuilder.domain.irepository.IEmailTemplateRepository;
import com.fastcode.emaildemo.emailbuilder.domain.irepository.IEmailVariableTypeRepository;
import com.fastcode.emaildemo.emailbuilder.domain.model.EmailVariableTypeEntity;

@Service
@Validated
public class EmailCategoryService implements IEmailCategoryService {
	@Autowired
	IEmailTemplateRepository emailTemplateRepository;

	@Override
	public List<String> getAllCategories() throws Exception {
		return emailTemplateRepository.findAllDistinctCategories();
	}

}
