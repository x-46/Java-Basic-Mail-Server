package server.smtp;

import java.util.HashMap;

import me.x46.base.server.ClientConnection;
import me.x46.base.server.ClientLogic;
import server.Manager;
import utils.Mail;

public class SmtpMailTransfer implements ClientLogic {

	private Manager manager;

	private HashMap<ClientConnection, StringBuilder> mailBuilder;
	
	public SmtpMailTransfer(Manager manager) {
		this.manager = manager;
		
		mailBuilder = new HashMap<ClientConnection, StringBuilder>();
	}

	@Override
	public void input(ClientConnection cc, String message) {
		System.out.println(message);
		
		if (message.startsWith("MAIL FROM")) {
			commandMAIL_FROM(cc, message);
		} else if (message.startsWith("RCPT TO")) {
			commandRCPT_TO(cc, message);
		} else if (message.startsWith("DATA")) {
			commandDATA(cc, message);
			mailBuilder.put(cc, new StringBuilder());
		} else {
			if (message.equals(".")) {
				cc.setRegister("msg", mailBuilder.get(cc).toString());
				mailTransmitDone(cc);
				mailBuilder.remove(cc);
			} else {
				if(!mailBuilder.containsKey(cc)) return;
				mailBuilder.get(cc).append(message); // to speed up
				mailBuilder.get(cc).append("\n");
			//	cc.setRegister("msg", cc.getRegister("msg") + message + "\n");
			}
		}
	}

	private void commandMAIL_FROM(ClientConnection cc, String message) {
		if (message.substring(message.lastIndexOf('@') + 1).equals(manager.getDomain())
				&& !cc.registerContainsKey("authenticated")) {
			cc.sendMessage("530 5.7.0 Authentication Required");
			System.out.println("weg");
		//	return; TODO
		}
		// TODO mail format
		cc.sendMessage("250 ok");
		cc.setRegister("from", message.substring(message.indexOf(":") + 1));
	}

	private void commandRCPT_TO(ClientConnection cc, String message) {
		if (!cc.registerContainsKey("from")) {
			cc.sendMessage("530 TODO");
			return;
		}

		cc.sendMessage("250 ok");
		cc.setRegister("to", message.substring(message.indexOf(":") + 1));
	}

	private void commandDATA(ClientConnection cc, String mesString) {
		if (!cc.registerContainsKey("to") || !cc.registerContainsKey("from")) {
			cc.sendMessage("530 TODO");
			return;
		}
		System.out.println("DATA2");
		cc.sendMessage("354 ok");
		cc.setRegister("msg", "");
	}

	private void mailTransmitDone(ClientConnection cc) {
		if (!cc.registerContainsKey("to") || !cc.registerContainsKey("from")) {
			cc.sendMessage("530 TODO");
			return;
		}

		cc.sendMessage("250 ok");

		Mail mail = new Mail(cc.getRegister("from"), cc.getRegister("to"), cc.getRegister("msg"), cc.getRegister("ip"),
				manager.getDomain());

		mail.save();
		mail.sendMail();
	}
}
