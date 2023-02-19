package server.pop;

import me.x46.base.server.ClientConnection;
import me.x46.base.server.ClientLogic;
import utils.storage.MailStorageAccess;

public class Pop3DataProcessing implements ClientLogic {

	@Override
	public void input(ClientConnection cc, String message) {

		if (message.startsWith("STAT")) {
			commandSTAT(cc);
		} else if (message.startsWith("LIST")) {
			commandLIST(cc);
		} else if (message.startsWith("UIDL")) {
			commandUIDL(cc);
		} else if (message.startsWith("RETR")) {
			commandRETR(cc, message);
		} else if (message.startsWith("DELE")) {
			commandDELE(cc);
		}

	}

	private void commandSTAT(ClientConnection cc) {
		MailStorageAccess msa = new MailStorageAccess(cc.getRegister("username"), "received");

		cc.sendMessage("+OK " + msa.getMailCount() + " " + msa.getMailSize());
	}

	private void commandLIST(ClientConnection cc) {
		MailStorageAccess msa = new MailStorageAccess(cc.getRegister("username"), "received");
		long[] list = msa.getSizeList();

		cc.sendMessage("+OK " + list.length + " messages:");
		for (int i = 0; i < list.length; i++) {
			cc.sendMessage(i + " " + list[i]);
		}
		cc.sendMessage(".");
	}

	private void commandUIDL(ClientConnection cc) {
		MailStorageAccess msa = new MailStorageAccess(cc.getRegister("username"), "received");
		int[] list = msa.getUIDList();

		cc.sendMessage("+OK " + list.length + " messages:");
		for (int i = 0; i < list.length; i++) {
			cc.sendMessage(i + " " + list[i]);
		}
		cc.sendMessage(".");
	}

	private void commandRETR(ClientConnection cc, String message) {
		int id = Integer.parseInt(message.split(" ")[1]);
		MailStorageAccess msa = new MailStorageAccess(cc.getRegister("username"), "received");

		String mail = msa.getMail(id).getMailBody();

		String[] mailArr = mail.split("\n");

		cc.sendMessage("+OK " + mail.length() + " octets");
		for (String line : mailArr) {
			cc.sendMessage(line);
		}

		cc.sendMessage(".");
	}

	private void commandDELE(ClientConnection cc) {
		cc.sendMessage("+OK");
	}

}
