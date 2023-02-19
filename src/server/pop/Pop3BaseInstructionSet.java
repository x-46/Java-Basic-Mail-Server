package server.pop;

import me.x46.base.server.ClientConnection;
import me.x46.base.server.ClientLogic;

public class Pop3BaseInstructionSet implements ClientLogic{

	@Override
	public void input(ClientConnection cc, String message) {
		if (message.startsWith("STLS")) {
			cc.sendMessage("+OK Begin TLS negotiation");
			cc.startTLS();
		} 
		
		if(message.startsWith("CAPA")) {
			cc.sendMessage("+OK Capability list follows");
			cc.sendMessage("TOP");
			cc.sendMessage("UIDL");
			cc.sendMessage("STLS");
			cc.sendMessage("SASL PLAIN");
			cc.sendMessage("USER");
			cc.sendMessage(".");
		}
		
		if(message.startsWith("QUIT")) {
			cc.sendMessage("+OK Logging out");
			cc.close();
		}
		
	}

}
