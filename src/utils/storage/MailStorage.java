package utils.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;


import utils.Mail;

public class MailStorage {
	
	private String mailAddress;
	private Mail mail;
	
	
	public MailStorage(String mailAddress, Mail mail) {
		this.mail = mail;
		this.mailAddress = mailAddress.toLowerCase();
	}
	
	
	public void save(String to) {
		File main = new File("./mails/" + mailAddress);
		if(!(main.exists() && main.isDirectory())) {
			main.mkdir();
		}

		
		File save = new File("./mails/" + mailAddress + "/" + to);
		if(!(save.exists() && save.isDirectory())) {
			save.mkdir();
		}
		
		writeObjectToFile(mail, "./mails/" + mailAddress + "/" + to + "/" + System.currentTimeMillis() + ".mail");
		
	}
	
	
	
	private void writeObjectToFile(Object serObj, String filepath) {
        try {
            FileOutputStream fileOut = new FileOutputStream(filepath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(serObj);
            objectOut.close();
            System.out.println("The Object  was succesfully written to a file");
 
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
