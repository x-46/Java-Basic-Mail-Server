package utils.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import utils.Mail;

import java.util.Set;

public class MailStorageAccess {


	private String mailAddress;
	private String folder;


	private HashMap<Integer, File> list;

	public MailStorageAccess(String mail, String folder) {
		this.mailAddress = mail.toLowerCase();
		this.folder = folder;

		list = new HashMap<Integer, File>();

		load();
	}

	private void load() {
		File userFolder = new File("./mails/" + mailAddress + "/" + folder);

		if (!userFolder.exists())
			return;

		File[] fileListArr = userFolder.listFiles();
		List<File> fileList = Arrays.asList(fileListArr);

		Collections.sort(fileList, new Comparator<File>() {
			public int compare(File o1, File o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		for (File f : fileList) {
			list.put(f.getName().hashCode(), f);
		}
	}

	public int getMailCount() {
		return list.size();
	}

	public long getMailSize() {
		long length = 0;
		for (Entry<Integer, File> map : list.entrySet()) {
			if (map.getValue().isFile())
				length += map.getValue().length();
		}

		return length;
	}

	public long[] getSizeList() {
		long[] l = new long[list.size()];

		int i = 0;

		for (int key : list.keySet()) {
			l[i] = list.get(key).length();
			i++;
		}

		return l;
	}

	public int[] getUIDList() {
		Set<Integer> set = list.keySet();
		Integer[] la = set.toArray(new Integer[0]);
		int[] l = new int[la.length];

		for (int i = 0; i < la.length; i++) {
			l[i] = la[i];
		}

		return l;
	}

	public Mail getMail(int id) {
		Mail mail = (Mail) readObjectFromFile(list.get(getUIDList()[id]).getAbsolutePath());
		return mail;
	}

	private Object readObjectFromFile(String filepath) {

		try {

			FileInputStream fileIn = new FileInputStream(filepath);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);

			Object obj = objectIn.readObject();

			System.out.println("The Object has been read from the file");
			objectIn.close();
			return obj;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
