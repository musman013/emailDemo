package com.fastcode.emaildemo.emailbuilder.application.emailvariable;

import java.util.List;

import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fastcode.emaildemo.commons.search.SearchCriteria;
import com.fastcode.emaildemo.emailbuilder.application.emailvariable.dto.*;
import com.fastcode.emaildemo.emailbuilder.domain.irepository.IEmailVariableTypeRepository;
import com.fastcode.emaildemo.emailbuilder.domain.model.EmailVariableTypeEntity;

@Service
public interface IEmailVariableTypeAppService {
	


	List<EmailVariableTypeEntity> getAllTypes() throws Exception;

}
