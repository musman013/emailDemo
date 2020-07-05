package com.fastcode.emaildemo.emailbuilder.domain.emailvariable;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fastcode.emaildemo.emailbuilder.application.emailvariable.dto.FindEmailVariableByIdOutput;
import com.fastcode.emaildemo.emailbuilder.domain.model.EmailVariableEntity;

import com.querydsl.core.types.Predicate;

public interface IEmailVariableManager {

	 // CRUD Operations
    public EmailVariableEntity create(EmailVariableEntity email);

    public void delete(EmailVariableEntity email);

    public EmailVariableEntity update(EmailVariableEntity email);

    public EmailVariableEntity findById(Long emailId);
    
    public EmailVariableEntity findByName (String name);

    public Page<EmailVariableEntity> findAll(Predicate predicate,Pageable pageable);

	
    public List<EmailVariableEntity> findAll();

	public List<Long> findByNameIn(Set<String> allFieldsId);
	
}
