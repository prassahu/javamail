package com.ecom.mail;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.junit.Before;
import org.junit.Test;

public class SendMailTest {

	private SendMail sendMail;

	@Before
	public void setUp() {
		sendMail = new SendMail();
	}

	@Test
	public void testSendMail() throws MessagingException {
		Address[] toAddresses = { new InternetAddress(
				"prashantsahu034@gmail.com") };
		assertTrue(sendMail.sendMessage("Subject Testing", "Body content",
				toAddresses));
	}

	@Test(expected = AddressException.class)
	public void testSendMailFail() throws MessagingException {
		Address[] toAddresses = { new InternetAddress("@gmail.com") };
		sendMail.sendMessage("Subject Testing", "Body content", toAddresses);
	}

	@Test
	public void testMultiAddressSendMail() throws MessagingException {
		Address[] toAddresses = {
				new InternetAddress("prashantsahu034@gmail.com"),
				new InternetAddress("prashantsahu312@gmail.com"),
				new InternetAddress("prashant.sahu@globallogic.com") };
		assertTrue(sendMail.sendMessage("Subject Testing", "Body content",
				toAddresses));
	}
	
	@Test
	public void testSendMailAttachment() throws MessagingException, IOException {
		Address[] toAddresses = { new InternetAddress(
				"prashantsahu034@gmail.com") };
		assertTrue(sendMail.sendMessage("Attachched sub", "Hello", toAddresses, "myfile1.txt"));
	}

}
