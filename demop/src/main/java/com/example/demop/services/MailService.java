package com.example.demop.services;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import com.example.demop.model.Magazine;
import com.example.demop.security.TokenUtils;
import com.example.demop.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.demop.model.User;

@Service
public class MailService {
	@Autowired
	private JavaMailSender javaMailSender;

	/*
	 * Koriscenje klase za ocitavanje vrednosti iz application.properties fajla
	 */
	@Autowired
	private Environment env;

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private MagazineService magazineService;
	/*
	 * Anotacija za oznacavanje asinhronog zadatka
	 * Vise informacija na: https://docs.spring.io/spring/docs/current/spring-framework-reference/integration.html#scheduling
	 */
	@Async
	public void sendNotificaitionAsync(User user, String processId) throws MailException, InterruptedException, MessagingException {

		System.out.println("Slanje emaila...");
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
 		String htmlMsg = "<h3>Hi "+user.getName()+"</h3><br> <p>In order to activate your account visit next link  <a href=\"http://localhost:4200/activate/"+processId+"/"+user.getUsername()+"\">link</a></p>";
		mimeMessage.setContent(htmlMsg, "text/html");
		helper.setTo(user.getMail());
		helper.setSubject("Verification of account");
		helper.setFrom(env.getProperty("spring.mail.username"));
		javaMailSender.send(mimeMessage);

		System.out.println("Email poslat!");
	}
	@Async
	public void sendNotificationAuthor(User user) throws MailException, InterruptedException, MessagingException {

		System.out.println("Slanje emaila...");
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
		String htmlMsg = "<h3>Hi "+user.getName()+"</h3><br> <p>Congratulations, you have been successfully uploaded new text!</p>";
		mimeMessage.setContent(htmlMsg, "text/html");
		helper.setTo(user.getMail());
		helper.setSubject("Congratulations");
		helper.setFrom(env.getProperty("spring.mail.username"));
		javaMailSender.send(mimeMessage);

		System.out.println("Email poslat!");
	}
	@Async
	public void sendNotificationEditor(String idMag) throws MailException, InterruptedException, MessagingException {

		System.out.println("Slanje emaila...");
		Magazine magazine = magazineService.findMagazineById(Long.parseLong(idMag));
		User user = magazine.getMainEditor();
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
		String htmlMsg = "<h3>Hi "+user.getName()+"</h3><br> <p>We want to inform you that there is one new text added to your magazine!</p>";
		mimeMessage.setContent(htmlMsg, "text/html");
		helper.setTo(user.getMail());
		helper.setSubject("New Text");
		helper.setFrom(env.getProperty("spring.mail.username"));
		javaMailSender.send(mimeMessage);

		System.out.println("Email poslat!");
	}
	@Async
	public void sendDeclineNotificationAuthor(User user, String title) throws MailException, InterruptedException, MessagingException {

		System.out.println("Slanje emaila...");
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
		String htmlMsg = "<h3>Hi "+user.getName()+"</h3><br> <p>We want to inform you that your work under name"+title+" unfortunately the theme of your text is not acceptable.</p>";
		mimeMessage.setContent(htmlMsg, "text/html");
		helper.setTo(user.getMail());
		helper.setSubject("Notification - "+title);
		helper.setFrom(env.getProperty("spring.mail.username"));
		javaMailSender.send(mimeMessage);
		System.out.println("Email poslat!");
	}
	@Async
	public void sendDeclineNotificationFormat(User user, String deadline) throws MailException, InterruptedException, MessagingException {

		System.out.println("Slanje emaila...");
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
		String htmlMsg = "<h3>Hi "+user.getName()+"</h3><br> <p>We want to inform you that format of pdf is not correct. You can replace it, deadline is"+deadline+" </p>";

		mimeMessage.setContent(htmlMsg, "text/html");
		helper.setTo(user.getMail());
		helper.setSubject("Notification - Format of text");
		helper.setFrom(env.getProperty("spring.mail.username"));
		javaMailSender.send(mimeMessage);
		System.out.println("Email poslat!");
	}


	@Async
	public void sendNotificationScience(User user) throws MailException, InterruptedException, MessagingException {

		System.out.println("Slanje emaila...");
		/*MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
		String htmlMsg = "<h3>Hi "+user.getName()+"</h3><br> <p>We want to inform you that there is text where you are editor.</p>";

		mimeMessage.setContent(htmlMsg, "text/html");
		helper.setTo(user.getMail());
		helper.setSubject("Notification");
		helper.setFrom(env.getProperty("spring.mail.username"));
		javaMailSender.send(mimeMessage);*/
		System.out.println("Email poslat!");
	}
	@Async
	public void sendOutTimeNotification(User user) throws MailException, InterruptedException, MessagingException {

		System.out.println("Slanje emaila...");
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
		String htmlMsg = "<h3>Hi "+user.getName()+"</h3><br> <p>We want to inform you that time is up for your text.</p>";

		mimeMessage.setContent(htmlMsg, "text/html");
		helper.setTo(user.getMail());
		helper.setSubject("Notification");
		helper.setFrom(env.getProperty("spring.mail.username"));
		javaMailSender.send(mimeMessage);
		System.out.println("Email poslat!");
	}
}
