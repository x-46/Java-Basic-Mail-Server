package test;

import server.Manager;
import test.utils.MyAuthentication;

public class TestServer {

	public static void main(String[] args) {

		Manager manager = new Manager("test.com", "1.1.1.1", "certificate.p12", "123",
				new MyAuthentication());
		manager.start();

	}

}
