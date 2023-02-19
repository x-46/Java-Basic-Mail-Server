package server.smtp;

import me.x46.base.server.BaseServer;
import me.x46.base.server.ClientConnected;
import me.x46.base.server.ClientConnection;
import me.x46.base.server.ClientLogic;
import server.Manager;

public class SmtpServer {
	
	private BaseServer server;
	private Manager manager;
	
	
	public SmtpServer(int port, Manager manager) {
		if(port == -1) port = 25;
		
		server = new BaseServer(port);
		this.manager = manager;
	}
	
	public void buildServer() {
		
		server.setKeystoreFile(manager.getKeystorePath());
		server.setKeystoreFilePassword(manager.getKeystorePassword());
		
		server.bindIp(manager.getBindIp());

		server.setSendLineEnding("\r\n");

		server.addClientConnectedEvent(ClientConnected.getDefaultEvent());
		
		
		server.addClientConnectedEvent(new ClientConnected() {

			@Override
			public void clientConnected(ClientConnection cc) {
				cc.sendMessage("220 "+manager.getDomain()+" ESMTP");
				System.out.println("sendEy " + "220 "+manager.getDomain()+" ESMTP");
			}
		});
		
		server.addClientLogic(new ClientLogic() {
			
			@Override
			public void input(ClientConnection cc, String message) {
				System.out.println(">" + message);
				
			}
		});
		
		server.addClientLogic(new SmtpBaseInstructionSet());
		server.addClientLogic(new SmtpAuthentication(manager));
		server.addClientLogic(new SmtpMailTransfer(manager));

		server.start();
	}
	

}
