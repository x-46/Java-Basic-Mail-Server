package server.smtp;

import java.nio.charset.Charset;
import java.util.Base64;

import me.x46.base.server.ClientConnection;
import me.x46.base.server.ClientLogic;
import server.Manager;

public class SmtpAuthentication implements ClientLogic {
	// https://knowledge.broadcom.com/external/article/164907/test-smtp-authentication-with-messaging.html
	
	private static final Charset ascii = Charset.forName("ASCII");
	
	private Manager manager;
	
	public SmtpAuthentication(Manager manager) {
		this.manager = manager;
	}
	
	@Override
	public void input(ClientConnection cc, String message) {
		if (message.toUpperCase().startsWith("AUTH LOGIN")) {
			commandAUTH_LOGIN(cc, message);
		} else if (cc.getRegister("username").equals("#")) {
			 setUsername(cc, message);
		} else if (cc.getRegister("password").equals("#")) {
			 setPassword(cc, message);
		}
		
	}
	
	private void setUsername(ClientConnection cc, String message) {
		cc.setRegister("username", decodeBase64(message));

			cc.sendMessage("334 " + encodeBase64("Password:"));
			cc.setRegister("password", "#");
	}
	
	private void setPassword(ClientConnection cc, String message) {
		cc.setRegister("password", decodeBase64(message));
		
		if (manager.getAuthentication().user(cc.getRegister("username"), cc.getRegister("password"))) {
			cc.sendMessage("235 2.7.0 Authentication successful");
			cc.setRegister("authenticated", "");
		} else {
			cc.sendMessage("535 5.7.8 Username and Password not accepted.");
			cc.setRegister("authenticated", null);
		}

	}
	
	private void commandAUTH_LOGIN(ClientConnection cc, String message) {
		if (cc.getRegister("EHLO") == null) {
			cc.sendMessage("503 5.5.1 EHLO first.");
			return;
		}

		// if the first message already contains the username
		if (!message.toUpperCase().equals("AUTH LOGIN")) {
			String[] tmp = message.split(" ");
			cc.setRegister("username", decodeBase64(tmp[2]));

			cc.sendMessage("334 " + encodeBase64("Password:"));
			cc.setRegister("password", "#");

			return;
		}

		cc.sendMessage("334 " + encodeBase64("Username:"));
		cc.setRegister("username", "#");
	}
	
	
	private static String decodeBase64(String str) {
		try {
			return new String(Base64.getDecoder().decode(str), ascii);
		} catch (Exception e) {
			return new String(Base64.getDecoder().decode(str + "="), ascii);
		}
	}

	private static String encodeBase64(String str) {
		return Base64.getEncoder().encodeToString(str.getBytes(ascii));
	}

}
