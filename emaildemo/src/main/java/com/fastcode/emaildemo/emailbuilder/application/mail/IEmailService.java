package com.fastcode.emaildemo.emailbuilder.application.mail;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fastcode.emaildemo.domain.model.File;

@Service
public interface IEmailService {

	 void sendMessage(String to, String cc, String bcc, String subject, String htmlContent, List<File> inlineImages, List<File> attachments);

}
