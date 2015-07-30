package com.ecom.mail;

import java.io.IOException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

public class SendMail {

	private final static Logger logger = Logger.getLogger(SendMail.class);

	/**
	 * Send simle messages
	 * @param subject
	 * @param messageBody
	 * @param toAddresses
	 * @return
	 */
	public boolean sendMessage(String subject, String messageBody,
			Address[] toAddresses) {

		logger.info("Start of sendMessage");

		try {
			Message message = buildMessage();
			message.setRecipients(Message.RecipientType.TO, toAddresses);
			message.setSubject(subject);
			message.setText(messageBody);
			// send message
			Transport.send(message);

			logger.info("message sent successfully");

		} catch (MessagingException e) {
			logger.error("Error occured while sending message", e);
			throw new RuntimeException(e);
		}

		logger.info("End of sendMessage");

		return true;

	}

	/**
	 * Send message with Attachment
	 * 
	 * @param subject
	 * @param messageBody
	 * @param toAddresses
	 * @param attachedfileName
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	public boolean sendMessage(String subject, String messageBody,
			Address[] toAddresses, String attachedfileName) {

		try {
			Message message = buildMessage();
			message.setRecipients(Message.RecipientType.TO, toAddresses);
			message.setSubject(subject);

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Now set the actual message
			messageBodyPart.setText(messageBody);

			// Create a multipar message
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			BodyPart messageBodyPart2 = new MimeBodyPart();
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			String filepath = loader.getResource(attachedfileName).getPath();//"src/main/resources/myfile.txt";
			DataSource source = new FileDataSource(filepath);
			messageBodyPart2.setDataHandler(new DataHandler(source));
			multipart.addBodyPart(messageBodyPart2);

			// Send the complete message parts
			message.setContent(multipart);

			Transport.send(message);

		} catch (MessagingException me) {
			logger.error("Error occured while sending message", me);
			throw new RuntimeException(me);
		}
		logger.info("message sent successfully");

		return true;
	}

	private Message buildMessage() throws AddressException, MessagingException {
		Properties props = PropertyUtil.loadProperties("config.properties");
		String username = props.getProperty("mail.username");

		// Create session
		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, props
								.getProperty("mail.password"));
					}
				});
		// form message
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(username));

		return message;
	}

}
