package utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.storage.MailStorage;

public class Mail implements Serializable {

	private static final long serialVersionUID = 8792740883809307435L;

	private String domain;

	// received in the SMTP message
	private MailAddress mailTo;
	private MailAddress mailFrom;

	private String mailBody;

	private String clientIp;
	
	public Mail(String mailFrom, String mailTo, String mailBody, String clientIp, String domain) {
		
		
		this.mailFrom = new MailAddress(mailFrom);
		this.mailTo = new MailAddress(mailTo);

		this.mailBody = mailBody;

		this.domain = domain;
		
		this.clientIp = clientIp;
		
	}

	/**
	 * @return if and only if the email address, pass by the smtp handshake und the
	 *         mail address in the body are equals the function will return true
	 * 
	 */
	private boolean checkMailEquality() {
		String regex = "^To:.*" + mailTo + ".*$";

		Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(mailBody);

		boolean findTo = matcher.find();
		

		regex = "^From:.*" + mailFrom.rawMailAddress + ".*$";

		pattern = Pattern.compile(regex, Pattern.MULTILINE);
		matcher = pattern.matcher(mailBody);

		boolean findFrom = matcher.find();

		return findTo && findFrom;
	}
	
	private boolean forwardMail() {
		System.out.println(" forwardMail");
		return mailFrom.rawMailAddress.toLowerCase().endsWith(domain.toLowerCase());
	}
	
	private boolean checkDomainIntegrity() {
		if(forwardMail()) {
			return true;
		}
		
		String rdns = DNS.rdnsIPcheck(clientIp);
		if (rdns != null) {
			String[] tmp = rdns.split("\\.");
			rdns = tmp[tmp.length - 2] + "." + tmp[tmp.length - 1];
			if (rdns.equals(mailFrom.domain) 
					|| (mailFrom.domain.equals("gmail.com") && rdns.equals("google.com"))) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean mailIntegrity() {
		return checkDomainIntegrity() && checkMailEquality();
	}
	
	private ArrayList<MailAddress> findAllToMailAddress() {
		ArrayList<MailAddress> mailAddressList = new ArrayList<>();
		String to = mailBody.substring(mailBody.indexOf("To:") + 3);
		to = to.substring(0,to.indexOf("\n"));
		to = to.trim();
		
		String[] toArry = to.split(",");
		for(String toMail : toArry) {
			System.out.println("found to mail: " + toMail);
			mailAddressList.add(new MailAddress(toMail.trim()));
			System.out.println("add");
		}
		
		System.out.println(mailAddressList.size());
		return mailAddressList;
	}
	
	private ArrayList<MailAddress> findAllCcMailAddress() {
		if(mailBody.indexOf("Cc:") == -1) return new ArrayList<>();
		
		ArrayList<MailAddress> mailAddressList = new ArrayList<>();
		String cc = mailBody.substring(mailBody.indexOf("Cc:") + 3);
		cc = cc.substring(0,cc.indexOf("\n"));
		cc = cc.trim();
		
		String[] ccArry = cc.split(",");
		for(String ccMail : ccArry) {
			System.out.println("found cc mail: " + ccMail);
			mailAddressList.add(new MailAddress(ccMail.trim()));
		}
		
		return mailAddressList;
	}
	
	public void save() {
		MailStorage storage;
		if(forwardMail()) {
			storage = new MailStorage(mailFrom.rawMailAddress, this);
			storage.save("send");
		}else {
			storage = new MailStorage(mailTo.rawMailAddress, this);
			storage.save("received");
		}
	}
	
	public void sendMail() {
		System.out.println("send Mail " + forwardMail());
		if(!forwardMail()) return;
		
		ArrayList<MailAddress> to = findAllToMailAddress();
		ArrayList<MailAddress> cc = findAllCcMailAddress();
		
		
		MailTransmitter transmitter = new MailTransmitter(this);
		
		for(MailAddress mail : to) {
			transmitter.send(mail.rawMailAddress, mail.domain);
		}
		
		for(MailAddress mail : cc) {
			transmitter.send(mail.rawMailAddress, mail.domain);
		}
	}
	
	
	private class MailAddress implements Serializable {

		private static final long serialVersionUID = -83520307324463787L;

		public String mail;
		public String rawMailAddress;
		@SuppressWarnings("unused")
		public String name;
		public String domain;

		public MailAddress(String baseMail) {
			mail = baseMail.trim();

			if (mail.indexOf('<') != -1) {
				name = mail.substring(0, mail.indexOf('<'));
				rawMailAddress = mail.substring(mail.indexOf('<') + 1, mail.indexOf('>'));
			}else {
				rawMailAddress = mail;
			}

			domain = rawMailAddress.substring(rawMailAddress.indexOf('@') + 1);
			
		}

	}



	public String getFromDomain() {
		return mailFrom.domain;
	}

	public String getFromMail() {
		return mailFrom.rawMailAddress;
	}

	public String getMailBody() {
		return mailBody;
	}
	
}
