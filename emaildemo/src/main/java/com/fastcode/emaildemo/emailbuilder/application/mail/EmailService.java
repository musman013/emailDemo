package com.fastcode.emaildemo.emailbuilder.application.mail;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
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
	public void sendMessage(String to, String cc, String bcc, String subject, String htmlContent,
			List<File> inlineImages, List<File> attachments) {

		MimeMessage message = emailSender.createMimeMessage();

		try {

			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());

			String[] toArray = to.split(",", -1);
			String[] ccArray = cc != null ? cc.split(",", -1) : new String[0];
			String[] bccArray = bcc != null ? bcc.split(",", -1) : new String[0];

			helper.setTo(toArray);
			helper.setCc(ccArray);
			helper.setBcc(bccArray);
			helper.setSubject(subject);
			helper.setText(htmlContent, true);

			// Use the true flag to indicate the text included is HTML

			for (File file : inlineImages) {
				try {

					ByteArrayResource fileStreamResource = getFileStreamResource(Long.valueOf(file.getName()));
					if (fileStreamResource != null)
						helper.addInline(file.getSummary(), fileStreamResource, "image/jpeg");
				} catch (Exception e) {
					// ignore
					e.printStackTrace();
				}
			}

			// Now add the real attachments
			for (File file : attachments) {
				ByteArrayResource fileStreamResource = getFileStreamResource(file.getId());
				if (fileStreamResource != null)
					helper.addAttachment(file.getName(), fileStreamResource);
			}

		} catch (MessagingException ex) {
			ex.printStackTrace();
		}

		emailSender.send(message);
	}

	public ByteArrayResource getFileStreamResource(Long fileId) { // This method will download file using RestTemplate
		try {
			Optional<File> f = filesRepo.findById(fileId);
			// InputStreamResource inputStreamResource = new
			// InputStreamResource(contentStore.getContent(f.get()));
			InputStream content = contentStore.getContent(f.get());
			return content != null ? new ByteArrayResource(IOUtils.toByteArray(content)) : null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String appendInlineImagePrifix(String name) {
		if (name.startsWith("cid:")) {
			return name;
		} else {
			return "cid:" + name;
		}
	}

}
