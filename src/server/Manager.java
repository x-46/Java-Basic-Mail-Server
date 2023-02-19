package server;

import server.pop.Pop3Server;
import server.smtp.SmtpServer;
import utils.userHandling.Authentication;

public class Manager {

	private String domain;
	private String bindIp;

	private String keystorePath;
	private String keystorePassword;

	private SmtpServer smtpServer;
	private Pop3Server pop3Server;
	
	private Authentication authentication;

	public Manager(String domain, String bindIp, String keystorePath, String keystorePassword, Authentication authentication) {
		this.domain = domain;
		this.bindIp = bindIp;

		this.keystorePath = keystorePath;
		this.keystorePassword = keystorePassword;

		this.authentication = authentication;
		
		smtpServer = new SmtpServer(-1, this);
		pop3Server = new Pop3Server(-1, this);
	}

	public void start() {
		smtpServer.buildServer();
		pop3Server.buildServer();
	}

	public String getDomain() {
		return domain;
	}

	public String getBindIp() {
		return bindIp;
	}

	public String getKeystorePath() {
		return keystorePath;
	}

	public String getKeystorePassword() {
		return keystorePassword;
	}

	public Authentication getAuthentication() {
		return authentication;
	}

}
