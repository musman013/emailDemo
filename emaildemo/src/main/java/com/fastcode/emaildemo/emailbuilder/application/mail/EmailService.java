package com.fastcode.emaildemo.emailbuilder.application.mail;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fastcode.emaildemo.domain.irepository.FileContentStore;
import com.fastcode.emaildemo.domain.irepository.FileRepository;
import com.fastcode.emaildemo.domain.model.File;

@Service
public class EmailService implements IEmailService {

	@Autowired
	public JavaMailSender emailSender;

	@Autowired
	private Environment env;

	@Autowired
	private RestTemplateBuilder restTemplate;

	@Autowired
	private FileContentStore contentStore;

	@Autowired
	private FileRepository filesRepo;

	@Transactional(propagation = Propagation.REQUIRED)
	public void sendMessage(String to, String cc, String bcc, String subject, String htmlContent, List<File> inlineImages, List<File> attachments) {

		MimeMessage message = emailSender.createMimeMessage();

		try {

			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

			String[] toArray = to.split(",", -1);
			String[] ccArray = cc != null ? cc.split(",", -1) : new String[0];
			String[] bccArray = bcc != null ? bcc.split(",", -1) : new String[0];

			helper.setTo(toArray);
			helper.setCc(ccArray);
			helper.setBcc(bccArray);
			helper.setSubject(subject);

			// Use the true flag to indicate the text included is HTML
			helper.setText(htmlContent, true);

			for (File file : inlineImages) {
				helper.addInline(file.getContentId(), getFileStreamResource(file.getId()));
			}

			// Now add the real attachments
			for (File file : attachments) {
				helper.addAttachment(file.getName(), getFileStreamResource(file.getId()));
			}

		} catch (MessagingException ex) {
			ex.printStackTrace();
		}

		emailSender.send(message);
	}

	private ByteArrayResource getFileStreamResource(Long fileId) { // This method will download file using RestTemplate
		try {
			Optional<File> f = filesRepo.findById(fileId);
			// InputStreamResource inputStreamResource = new
			// InputStreamResource(contentStore.getContent(f.get()));
			return new ByteArrayResource(IOUtils.toByteArray(contentStore.getContent(f.get())));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
