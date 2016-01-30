package org.wotmud.service.twitter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TestBase {

	protected void createFile(String path, String text, String fileName) {
		BufferedWriter output;

		try {
			File file = new File(path + fileName);
			output = new BufferedWriter(new FileWriter(file));
			output.write(text);
			if (output != null)
				output.close();
			file.deleteOnExit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void createDirectoryIfNotExist(String path) {
		File theDir = new File(path);

		// if the directory does not exist, create it
		if (!theDir.exists()) {
			System.out.println("creating directory: " + path);
			boolean result = false;

			try {
				theDir.mkdir();
				result = true;
			} catch (SecurityException se) {
				se.printStackTrace();
			}
			if (result) {
				System.out.println(path + "directory created");
			}
		}
		theDir.deleteOnExit();
	}
}
