package com.fastcode.emaildemo.emailbuilder.restcontrollers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fastcode.emaildemo.commons.application.OffsetBasedPageRequest;
import com.fastcode.emaildemo.commons.search.SearchCriteria;
import com.fastcode.emaildemo.commons.search.SearchUtils;
import com.fastcode.emaildemo.domain.irepository.FileRepository;
import com.fastcode.emaildemo.domain.model.File;
import com.fastcode.emaildemo.emailbuilder.application.emailtemplate.EmailTemplateAppService;
import com.fastcode.emaildemo.emailbuilder.application.emailtemplate.dto.CreateEmailInput;
import com.fastcode.emaildemo.emailbuilder.application.emailvariable.EmailVariableAppService;
import com.fastcode.emaildemo.emailbuilder.application.emailvariable.dto.FindEmailVariableByIdOutput;
import com.fastcode.emaildemo.emailbuilder.application.mail.EmailService;

@RestController
@RequestMapping("/mail")
public class MailController {

	@Autowired
	private EmailService emailService;

	@Autowired
	private EmailTemplateAppService emailTemplateAppService;

	@Autowired
	private EmailVariableAppService emailVariableAppService;

	@Autowired
	private Environment env;
	
	@Autowired
	private FileRepository filesRepo;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity sendEmail(@RequestBody @Valid CreateEmailInput email) throws IOException {
		

		List<File> lImages = new ArrayList<File>();
		lImages.addAll(email.getInlineImages());

		List<File> lAttachments = new ArrayList<File>();
		lAttachments.addAll(filesRepo.getFileByEmailTemplateId(email.getId()));
		email.setEmailBody(emailTemplateAppService.convertJsonToHtml(replaceVariable(email.getContentJson())));

		emailService.sendMessage(email.getTo(), replaceVariable(email.getCc()), replaceVariable(email.getBcc()), replaceVariable(email.getSubject()), email.getEmailBody(), lImages, lAttachments);
		return new ResponseEntity(HttpStatus.OK);
	}

	private String replaceVariable(String input) {

		if (input == null || input.length() == 0)
			return input;

		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(env.getProperty("fastCode.offset.default")), Integer.parseInt(env.getProperty("fastCode.limit.default")));

		HashMap<String, String> map = new HashMap<>();

		List<FindEmailVariableByIdOutput> tags;
		try {

			SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject("");
			tags = emailVariableAppService.find(searchCriteria, pageable);
			System.out.println(" tags " + tags);
			for (FindEmailVariableByIdOutput tag : tags) {
				map.put("{{" + tag.getPropertyName() + "}}", tag.getDefaultValue());
			}
		} catch (Exception ex) {
			map.put("tag1", "tag one");
			map.put("{{tag2}}", "tag two");
		}

		final String regex = "\\{\\{\\w+\\}\\}";// "\\{\\{\\w+}}"; //"{{\\w+}}";

		final Matcher m = Pattern.compile(regex).matcher(input);

		final List<String> matches = new ArrayList<>();
		while (m.find()) {
			matches.add(m.group(0));
			if (map.get(m.group(0)) != null)
				input = input.replaceAll(Pattern.quote(m.group(0)), map.get(m.group(0)));
		}
		return input;
	}
}
