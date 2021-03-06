package com.jobposter.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.jobposter.entity.Mail;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service("emailService")
public class EmailService {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	@Qualifier("emailConfigBean")
	private Configuration emailConfig;
	
	@Autowired
	public EmailService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
		
	}
	
	public void sendReject(Mail mailModel) throws MessagingException, IOException, TemplateException {
		Map model = new HashMap();
		model.put("name", mailModel.getName());
        model.put("location", "Jakarta");
        model.put("signature", "https://jobposterlinov.com");
        model.put("position", mailModel.getPosition());
        model.put("reason", mailModel.getReasonRejected());
     
        /**
         * Add below line if you need to create a token to verification emails and uncomment line:32 in "email.ftl"
         * model.put("token",UUID.randomUUID().toString());
         */

        mailModel.setModel(model);


        //log.info("Sending Email to: " + mailModel.getTo());


        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        mimeMessageHelper.addInline("logo.png", new ClassPathResource("classpath:/lwcn-logo.jpeg"));

        Template template = emailConfig.getTemplate("reject.ftl");
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, mailModel.getModel());

        mimeMessageHelper.setTo(mailModel.getTo());
        mimeMessageHelper.setText(html, true);
        mimeMessageHelper.setSubject(mailModel.getSubject());
        mimeMessageHelper.setFrom("no-reply@gmail.com");


        javaMailSender.send(message);
		
	}
	
	public void sendRejectByApplicant(Mail mailModel) throws MessagingException, IOException, TemplateException {
		Map model = new HashMap();
		model.put("name", mailModel.getName());
        model.put("location", "Jakarta");
        model.put("signature", "https://jobposterlinov.com");
        model.put("position", mailModel.getPosition());
        model.put("reason", mailModel.getReasonRejected());
        model.put("aplname", mailModel.getAddress());
        /**
         * Add below line if you need to create a token to verification emails and uncomment line:32 in "email.ftl"
         * model.put("token",UUID.randomUUID().toString());
         */

        mailModel.setModel(model);


        //log.info("Sending Email to: " + mailModel.getTo());


        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        mimeMessageHelper.addInline("logo.png", new ClassPathResource("classpath:/lwcn-logo.jpeg"));

        Template template = emailConfig.getTemplate("rejectbyapplicant.ftl");
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, mailModel.getModel());

        mimeMessageHelper.setTo(mailModel.getTo());
        mimeMessageHelper.setText(html, true);
        mimeMessageHelper.setSubject(mailModel.getSubject());
        mimeMessageHelper.setFrom("no-reply@gmail.com");


        javaMailSender.send(message);
		
	}

	public void sendAttend(Mail mailModel) throws MessagingException, IOException, TemplateException {
		Map model = new HashMap();
		model.put("name", mailModel.getName());
        model.put("location", "Jakarta");
        model.put("signature", "https://jobposterlinov.com");
        model.put("position", mailModel.getPosition());
        model.put("aplname", mailModel.getReasonRejected());
        /**
         * Add below line if you need to create a token to verification emails and uncomment line:32 in "email.ftl"
         * model.put("token",UUID.randomUUID().toString());
         */

        mailModel.setModel(model);


        //log.info("Sending Email to: " + mailModel.getTo());


        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        mimeMessageHelper.addInline("logo.png", new ClassPathResource("classpath:/lwcn-logo.jpeg"));

        Template template = emailConfig.getTemplate("attend.ftl");
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, mailModel.getModel());

        mimeMessageHelper.setTo(mailModel.getTo());
        mimeMessageHelper.setText(html, true);
        mimeMessageHelper.setSubject(mailModel.getSubject());
        mimeMessageHelper.setFrom("no-reply@gmail.com");


        javaMailSender.send(message);
		
	}
	
	public void sendReschedule(Mail mailModel) throws MessagingException, IOException, TemplateException {
		Map model = new HashMap();
		model.put("name", mailModel.getName());
        model.put("location", "Jakarta");
        model.put("signature", "https://jobposterlinov.com");
        model.put("position", mailModel.getPosition());
        model.put("reason", mailModel.getReasonReschedule());
        model.put("address", mailModel.getAddress());
        model.put("aplname", mailModel.getReasonRejected());
        model.put("date", mailModel.getDate());
        model.put("time", mailModel.getTime());
        /**
         * Add below line if you need to create a token to verification emails and uncomment line:32 in "email.ftl"
         * model.put("token",UUID.randomUUID().toString());
         */

        mailModel.setModel(model);


        //log.info("Sending Email to: " + mailModel.getTo());


        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        mimeMessageHelper.addInline("logo.png", new ClassPathResource("classpath:/lwcn-logo.jpeg"));

        Template template = emailConfig.getTemplate("reschedule.ftl");
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, mailModel.getModel());

        mimeMessageHelper.setTo(mailModel.getTo());
        mimeMessageHelper.setText(html, true);
        mimeMessageHelper.setSubject(mailModel.getSubject());
        mimeMessageHelper.setFrom("no-reply@gmail.com");


        javaMailSender.send(message);
		
	}
	
	public void sendHire(Mail mailModel) throws MessagingException, IOException, TemplateException {
		Map model = new HashMap();
		model.put("name", mailModel.getName());
        model.put("location", "Jakarta");
        model.put("signature", "https://jobposterlinov.com");
        model.put("content", mailModel.getContent());
        model.put("position", mailModel.getPosition());
        model.put("date", mailModel.getDate());
        model.put("time", mailModel.getTime());
        model.put("address", mailModel.getAddress());
        /**
         * Add below line if you need to create a token to verification emails and uncomment line:32 in "email.ftl"
         * model.put("token",UUID.randomUUID().toString());
         */

        mailModel.setModel(model);


        //log.info("Sending Email to: " + mailModel.getTo());


        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        mimeMessageHelper.addInline("logo.png", new ClassPathResource("classpath:/lwcn-logo.jpeg"));

        Template template = emailConfig.getTemplate("hire.ftl");
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, mailModel.getModel());

        mimeMessageHelper.setTo(mailModel.getTo());
        mimeMessageHelper.setText(html, true);
        mimeMessageHelper.setSubject(mailModel.getSubject());
        mimeMessageHelper.setFrom("no-reply@gmail.com");


        javaMailSender.send(message);
		
	}
	
	public void sendInterview(Mail mailModel) throws MessagingException, IOException, TemplateException {

        Map model = new HashMap();
        model.put("name", mailModel.getName());
        model.put("location", "Jakarta");
        model.put("signature", "https://jobposterlinov.com");
        model.put("content", mailModel.getContent());
        model.put("position", mailModel.getPosition());
        model.put("date", mailModel.getDate());
        model.put("time", mailModel.getTime());
        model.put("address", mailModel.getAddress());
        /**
         * Add below line if you need to create a token to verification emails and uncomment line:32 in "email.ftl"
         * model.put("token",UUID.randomUUID().toString());
         */

        mailModel.setModel(model);


        //log.info("Sending Email to: " + mailModel.getTo());


        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        mimeMessageHelper.addInline("logo.png", new ClassPathResource("classpath:/lwcn-logo.jpeg"));

        Template template = emailConfig.getTemplate("interview.ftl");
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, mailModel.getModel());

        mimeMessageHelper.setTo(mailModel.getTo());
        mimeMessageHelper.setText(html, true);
        mimeMessageHelper.setSubject(mailModel.getSubject());
        mimeMessageHelper.setFrom("no-reply@gmail.com");


        javaMailSender.send(message);

    }
	
	public void sendEmail(Mail mailModel) throws MessagingException, IOException, TemplateException {

        Map model = new HashMap();
        model.put("name", mailModel.getName());
        model.put("location", "Jakarta");
        model.put("signature", "https://jobposterlinov.com");
        model.put("content", mailModel.getContent());
        /**
         * Add below line if you need to create a token to verification emails and uncomment line:32 in "email.ftl"
         * model.put("token",UUID.randomUUID().toString());
         */

        mailModel.setModel(model);


        //log.info("Sending Email to: " + mailModel.getTo());


        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        mimeMessageHelper.addInline("logo.png", new ClassPathResource("classpath:/lwcn-logo.jpeg"));

        Template template = emailConfig.getTemplate("passwordaccount.ftl");
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, mailModel.getModel());

        mimeMessageHelper.setTo(mailModel.getTo());
        mimeMessageHelper.setText(html, true);
        mimeMessageHelper.setSubject(mailModel.getSubject());
        mimeMessageHelper.setFrom("no-reply@gmail.com");


        javaMailSender.send(message);

    }

}
