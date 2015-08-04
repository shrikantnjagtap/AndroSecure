package org.opencv.samples.facedetect;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import android.util.Log;

public class GMail {

	final String emailPort = "587";// gmail's smtp port
	final String smtpAuth = "true";
	final String starttls = "true";
	final String emailHost = "smtp.gmail.com";

	String fromEmail;
	String fromPassword;
	List<String> toEmailList;
	String emailSubject;
	String emailBody;
	String attachpath;

	Properties emailProperties;
	Session mailSession;
	MimeMessage emailMessage;
	Multipart multipart = new MimeMultipart();

	public GMail() {

	}

	public GMail(String fromEmail, String fromPassword,
			List<String> toEmailList, String emailSubject, String emailBody,
			String attachment) {
		this.fromEmail = fromEmail;
		this.fromPassword = fromPassword;
		this.toEmailList = toEmailList;
		this.emailSubject = emailSubject;
		this.emailBody = emailBody;
		this.attachpath = attachment;

		emailProperties = System.getProperties();
		emailProperties.put("mail.smtp.port", emailPort);
		emailProperties.put("mail.smtp.auth", smtpAuth);
		emailProperties.put("mail.smtp.starttls.enable", starttls);
		Log.i("GMail", "Mail server properties set.");
	}

	public MimeMessage createEmailMessage() throws AddressException,
			MessagingException, UnsupportedEncodingException {

		mailSession = Session.getDefaultInstance(emailProperties, null);
		emailMessage = new MimeMessage(mailSession);

		emailMessage.setFrom(new InternetAddress(fromEmail, fromEmail));

		for (String toEmail : toEmailList) {
			Log.i("GMail", "toEmail: " + toEmail);
			emailMessage.addRecipient(Message.RecipientType.TO,
					new InternetAddress(toEmail));
		}

		BodyPart messageBodyPart1 = new MimeBodyPart();
		BodyPart messageBodyPart2 = new MimeBodyPart();

		emailMessage.setSubject(emailSubject);
		messageBodyPart1.setText(emailBody);

		String filename = attachpath;
		DataSource source = new FileDataSource(filename);
		messageBodyPart2.setDataHandler(new DataHandler(source));
		messageBodyPart2.setFileName(filename);

		Log.i("Gmail", "filename=" + filename.toString());

		multipart.addBodyPart(messageBodyPart1);
		multipart.addBodyPart(messageBodyPart2);

		// emailMessage.setContent(emailBody, "text/html");// for a html email

		emailMessage.setContent(multipart);
		// emailMessage.setText(emailBody);// for a text email
		Log.i("GMail", "Email Message created.");

		return emailMessage;
	}

	public void sendEmail() throws AddressException, MessagingException {

		Transport transport = mailSession.getTransport("smtp");
		Log.i("GMail", "allrecipients: before  connect " + this.fromEmail
				+ "  " + this.fromPassword);
		transport.connect(emailHost, fromEmail, fromPassword);
		Log.i("GMail",
				"allrecipients: after connect    "
						+ emailMessage.getAllRecipients());
		transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
		transport.close();
		Log.i("GMail", "Email sent successfully.");

	}

	/*
	 * public void attachment() throws MessagingException{ //Multipart
	 * multipart=new MimeMultipart();
	 * 
	 * 
	 * MimeBodyPart messageBodyPart = new MimeBodyPart(); String filename =
	 * "C:/Users/f/workspace/autoemails1/src/com/example/autoemails1/hello.txt";
	 * DataSource source = new FileDataSource(filename);
	 * messageBodyPart.setDataHandler(new DataHandler(source));
	 * messageBodyPart.setFileName(filename);
	 * //multipart.addBodyPart(messageBodyPart); }
	 */

}
