package com.fastcode.emaildemo.emailbuilder.domain.emailtemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fastcode.emaildemo.emailbuilder.application.emailtemplate.dto.FindEmailTemplateByIdOutput;
import com.fastcode.emaildemo.emailbuilder.application.emailvariable.EmailVariableAppService;
import com.fastcode.emaildemo.emailbuilder.application.emailvariable.dto.FindEmailVariableByIdOutput;
import com.fastcode.emaildemo.emailbuilder.application.emailvariable.dto.FindEmailVariableByNameOutput;
import com.fastcode.emaildemo.emailbuilder.domain.irepository.EmailMergeFieldEntityRepo;
import com.fastcode.emaildemo.emailbuilder.domain.irepository.IEmailTemplateRepository;
import com.fastcode.emaildemo.emailbuilder.domain.model.EmailMergeFieldEntity;
import com.fastcode.emaildemo.emailbuilder.domain.model.EmailTemplateEntity;
import com.fastcode.emaildemo.emailbuilder.domain.model.EmailVariableEntity;
import com.querydsl.core.types.Predicate;

@Repository
public class EmailTemplateManager implements IEmailTemplateManager {

	@Autowired
	IEmailTemplateRepository  _emailTemplateRepository;

	@Autowired
	private EmailVariableAppService emailVariableAppService;
	
	@Autowired
	private EmailMergeFieldEntityRepo emailMergeFieldEntityRepo;
	
	public EmailTemplateEntity create(EmailTemplateEntity email) {

		List<Long> allContentJsonData = findAndCreateEmailMergeFieldEntity(email);
		
		email = _emailTemplateRepository.save(email);
		if(allContentJsonData!=null && allContentJsonData.size()>0 )
		{
		saveEmailMergeFieldData(email,allContentJsonData);
		}
		return email;
	}

	private void saveEmailMergeFieldData(EmailTemplateEntity email, List<Long> ids) {
	List<EmailMergeFieldEntity> emailMergeFieldList = new ArrayList<>();
		
	for(Long id:ids)
	{
		EmailMergeFieldEntity emailMergeField = new EmailMergeFieldEntity();
		emailMergeField.setEmailTemplate(email);
		emailMergeField.setMergeField(new EmailVariableEntity(id));
		emailMergeFieldList.add(emailMergeField);
	}
	emailMergeFieldEntityRepo.saveAll(emailMergeFieldList);
	}

	public void delete(EmailTemplateEntity email) {

		_emailTemplateRepository.delete(email);	
	}

	public EmailTemplateEntity update(EmailTemplateEntity email) {

		
		
		List<Long> allContentJsonData = findAndCreateEmailMergeFieldEntity(email);
		email = _emailTemplateRepository.save(email);
		emailMergeFieldEntityRepo.updateEmailMergeField(email);
		
		if(allContentJsonData!=null && allContentJsonData.size()>0 )
		{
		saveEmailMergeFieldData(email,allContentJsonData);
		}
		
		return email;
	}

	public EmailTemplateEntity findById(Long emailId) {

		return _emailTemplateRepository.findById(emailId.longValue());
	}

	public Page<EmailTemplateEntity> findAll(Predicate predicate, Pageable pageable) {

		return _emailTemplateRepository.findAll(predicate,pageable);
	}

	public EmailTemplateEntity findByName(String name) {

		return _emailTemplateRepository.findByEmailName(name);
	}
	
	public List<EmailTemplateEntity> findAll()
	{
		return _emailTemplateRepository.findAll();
		
	}
	
	public List<Long> findAndCreateEmailMergeFieldEntity(EmailTemplateEntity email)
	{
		
		String cc = email.getCc();
		String bcc = email.getBcc();
		String contentJson = email.getContentJson();
		String subject = email.getSubject();
		
		List<String> allFields = new ArrayList<>();
		allFields.add(cc);
		allFields.add(bcc);
		allFields.add(contentJson);
		allFields.add(subject);
		
		Set<String> allNames = new HashSet<String>();
		for(String data : allFields) {
			if(StringUtils.isNotBlank(data)) {
				final String regex="\\{\\{([^}]*.?)\\}\\}";
				
				final Matcher m = Pattern.compile(regex).matcher(data);
				
				while (m.find()) {
					String fieldId = m.group(0).substring(2,m.group(0).length()-2);
					allNames.add(fieldId);
				}
			}
		}
		if(allNames!=null && allNames.size()>0 )
		{
		List<Long> tagInfo = emailVariableAppService.findByNameIn(allNames);
		return tagInfo;
		}
		return null;
			
		
	}

}