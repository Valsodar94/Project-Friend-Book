package com.friendBook.model;

import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;
import org.simplejavamail.util.ConfigLoader;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {
	public void sendEmail(String receipantEmail, String subject, String content) {
		ConfigLoader.loadProperties("simplejavamail.properties",true);
		Email email = EmailBuilder.startingBlank()
		          .to(receipantEmail)
		          .from("friend.book.ittalents@gmail.com")
		          .withSubject(subject)
		          .withPlainText(content)
		          .withHeader("X-Priority", 5)
		          .buildEmail();

		Mailer mailer = MailerBuilder
		          .withSMTPServer("smtp.gmail.com", 587, "friend.book.ittalents", "FB1357924680")
		          .withTransportStrategy(TransportStrategy.SMTP_TLS)
		          .withSessionTimeout(10 * 1000)
		          .clearEmailAddressCriteria() // turns off email validation
		          .withDebugLogging(true)
		          
		          .buildMailer();

		mailer.sendMail(email);
	}
}
