package server.pop;

import me.x46.base.server.ClientConnection;
import me.x46.base.server.ClientLogic;
import server.Manager;

public class Pop3Authentication implements ClientLogic {
	
	private Manager manager;

	public Pop3Authentication(Manager manager) {
		this.manager = manager;
	}
		
	@Override
	public void input(ClientConnection cc, String message) {

		if (message.startsWith("AUTH")) {
			cc.sendMessage("+OK");
			cc.sendMessage("PLAIN");
			cc.sendMessage(".");
		}

		if (message.startsWith("USER")) {
			cc.setRegister("username", message.split(" ")[1]);
			cc.sendMessage("+OK User name accepted, password please");
		} else if (message.startsWith("PASS")) {
			cc.setRegister("password", message.split(" ")[1]);
			
			if (manager.getAuthentication().user(cc.getRegister("username"), cc.getRegister("password"))) {
				cc.sendMessage("+OK " + cc.getRegister("username") + " has xx messages");
			} else {
				cc.sendMessage("-ERR Authentication failure: unknown user name or bad password.");
			}
		}

	}

}
