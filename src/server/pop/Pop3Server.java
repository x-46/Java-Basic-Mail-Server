package server.pop;

import me.x46.base.server.BaseServer;
import me.x46.base.server.ClientConnected;
import me.x46.base.server.ClientConnection;
import server.Manager;

public class Pop3Server {
	
	private BaseServer server;
	private Manager manager;
	
	
	public Pop3Server(int port, Manager manager) {
		if(port == -1) port = 110;
		
		server = new BaseServer(port);
		this.manager = manager;
	}
	
	public void buildServer() {
		
		server.setKeystoreFile("certificate.p12");
		server.setKeystoreFilePassword("123");
		
		server.bindIp(manager.getBindIp());

		server.setSendLineEnding("\r\n");

		server.addClientConnectedEvent(ClientConnected.getDefaultEvent());

		
		server.addClientConnectedEvent(new ClientConnected() {

			@Override
			public void clientConnected(ClientConnection cc) {
				cc.sendMessage("+OK POP3 server ready <1896.697170952@x46.me>");

			}
		});
		
		
		server.addClientLogic(new Pop3BaseInstructionSet());
		server.addClientLogic(new Pop3Authentication(manager));
		server.addClientLogic(new Pop3DataProcessing());

		server.start();
	}
}
