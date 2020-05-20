package com.fastcode.emaildemo.emailbuilder.application.mail;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
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

import com.fastcode.emaildemo.domain.model.File;

@Service
public class EmailService implements IEmailService {

	@Autowired
	public JavaMailSender emailSender;

	@Autowired
	private Environment env;

	@Autowired
	private RestTemplateBuilder restTemplate;

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
			for (File file : inlineImages) {
				helper.addAttachment(file.getName(), getFileStreamResource(file.getId()));
			}

		} catch (MessagingException ex) {
			ex.printStackTrace();
		}

		emailSender.send(message);
	}

	private InputStreamResource getFileStreamResource(Long fileId) { // This method will download file using RestTemplate
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
			HttpEntity<String> entity = new HttpEntity<>(headers);
			ResponseEntity<byte[]> response = restTemplate.build().exchange(env.getProperty("fastCode.docmgmt.URL") + "/" + fileId.toString(), HttpMethod.GET, entity, byte[].class);
			return new InputStreamResource(new ByteArrayInputStream(response.getBody()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
}
