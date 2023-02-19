package utils;

import java.util.HashMap;

import me.x46.base.client.BaseClient;
import me.x46.base.client.Connected;
import me.x46.base.client.HandshakeDone;
import me.x46.base.client.InBox;

public class MailTransmitter {

	private Mail mail;

	private HashMap<String, Boolean> req;
	private HashMap<String, Integer> step;

	public MailTransmitter(Mail mail) {
		this.mail = mail;
		
		req = new HashMap<String, Boolean>();
		step = new HashMap<String, Integer>();

	}


	public void send(String to, String toDomain) {
		System.out.println(to + "-####------------------------###-" + toDomain);	
		
		req.put(to, false);
		step.put(to, 1);

		String smtpServer = DNS.lookupMailHosts(toDomain)[0];
		System.out.println(smtpServer);
		BaseClient bs = new BaseClient(smtpServer, 25, true);
		bs.setSendLineEnding("\r\n");

		bs.addHandshakeDoneEvent(new HandshakeDone() {

			@Override
			public void handshake() {
				bs.sendMessage("HELO " + mail.getFromDomain());

			}
		});

		bs.addConnectedEvent(new Connected() {

			@Override
			public void connected() {
				bs.sendMessage("EHLO " + mail.getFromDomain());
			}
		});
		
	

		bs.addInBox(new InBox() {

			@Override
			public void in(String message) {
				
				System.out.println(message);
			}
			
		});

		bs.addInBox(new InBox() {

			@Override
			public void in(String message) {
				if (message.indexOf("STARTTLS") != -1) {
					step.put(to, step.get(to).intValue()-1);
				
				}
				if (message.startsWith("220 ") && req.get(to)) {
					bs.startTLS();
				}
				if (message.startsWith("250 ")) {
					if (step.get(to) == 0) {
						bs.sendMessage("STARTTLS");
						req.put(to, true);
					} else if (step.get(to) == 1) {
						bs.sendMessage("MAIL FROM:<" + mail.getFromMail() + ">");
					} else if (step.get(to) == 2) {
						bs.sendMessage("RCPT TO:<" + to + ">");
					} else if (step.get(to) == 3) {
						bs.sendMessage("DATA");
					} else if (step.get(to) == 4) {
						bs.sendMessage("QUIT");
						bs.close();
					}

					step.put(to, step.get(to).intValue()+1);
				} else {
					if (message.startsWith("354")) {

						String[] lines = mail.getMailBody().split("\n");

						for (String line : lines) {
							bs.sendMessage(line);
						}

						bs.sendMessage(".");
					}
				}

			}
		});

		bs.openConnection();

	}

}
