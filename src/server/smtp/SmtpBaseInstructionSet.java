package server.smtp;

import me.x46.base.server.ClientConnection;
import me.x46.base.server.ClientLogic;

public class SmtpBaseInstructionSet implements ClientLogic {

	@Override
	public void input(ClientConnection cc, String message) {
		
		if (message.startsWith("EHLO")) {
			cc.sendMessage("250-SIZE 52428800");
			cc.sendMessage("250-8BITMIME");
			cc.sendMessage("250-STARTTLS");
			cc.sendMessage("250-ENHANCEDSTATUSCODES");
			cc.sendMessage("250-AUTH LOGIN");
			cc.sendMessage("250 SMTPUTF8");

			cc.setRegister("EHLO", "");

			return;
		}

		if (!cc.registerContainsKey("EHLO")) {
			cc.sendMessage("503 5.5.1 EHLO first.");
			return;
		}
		
		
		if (message.startsWith("STARTTLS")) {
			cc.sendMessage("220 2.0.0 Ready to start TLS");
			cc.startTLS();
		} 
		
		if (message.equals("QUIT")) {
			cc.sendMessage("221");
			cc.close();
		}
	}

}
