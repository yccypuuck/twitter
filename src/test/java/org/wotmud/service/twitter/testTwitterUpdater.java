package org.wotmud.service.twitter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

/**
 * @author flash
 *
 */
public class testTwitterUpdater extends TestCase {

	public final String TWEET_MSG = "unit testing";
	public final String PATH = "./tmp/";
	TwitterUpdater twitterUpdater;
	TwitterListener twitterListener;

	@Before
	public void setUp() {
		twitterUpdater = new TwitterUpdater();
		twitterListener = new TwitterListenerImpl();

		createDirectoryIfNotExist(PATH);

		createFile(TWEET_MSG, "tweet.test");
	}

	@After
	public void cleanUp() {
		File file = new File(PATH);
		file.delete();

		file = new File("./tmp_empty/");
		file.delete();
		
	}

	private void createFile(String text, String fileName) {
		BufferedWriter output;

		try {
			File file = new File(PATH + fileName);
			output = new BufferedWriter(new FileWriter(file));
			output.write(text);
			if (output != null)
				output.close();
			file.deleteOnExit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createDirectoryIfNotExist(String path) {
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

	@Test
	public void testSetUp() {
		// intentionally left empty
	}

	@Test
	public void testGetTweetFileList_empty() {
		String tmpEmpty = "./tmp_empty/";
		createDirectoryIfNotExist(tmpEmpty);

		File[] files = twitterListener.getTweetFileList(tmpEmpty);

		assertTrue("No files should be found", files == null || files.length == 0);

		assertEquals("last tweet time should not be updated when no files found", 0,
				twitterListener.getLastTweetTime());
	}

	@Test
	public void testGetTweetFileList_positive() {

		File[] files = twitterListener.getTweetFileList(PATH);
		assertEquals("There should be only one file found", 1, files.length);

	}

	@Test
	public void testGetMostRecentFileInTimeFrame_too_recent() {
		File[] files = twitterListener.getTweetFileList(PATH);
		assertEquals("There should be only one file found", 1, files.length);

		File file = twitterListener.getMostRecentFileInTimeFrame(files, 10 * 60 * 1000, 5000 * 1000);
		assertNull("Should not show anything. File was just created! It's not old enough", file);
	}

	@Test
	public void testGetMostRecentFileInTimeFrame_positive() {
		File[] files = twitterListener.getTweetFileList("./tmp/");
		assertEquals("There should be only one file found", 1, files.length);

		// Look for a file of (almost) any age
		File file = twitterListener.getMostRecentFileInTimeFrame(files, 0, 99999);
		assertNotNull("One file should be found", file);

	}

	@Test
	public void testGetTweetFromFile() {
		String tweet = twitterListener.getMeTweet(PATH, 0, 999999);
		assertNotNull("Tweet should be found", tweet);
		assertTrue("The tweet should be '" + TWEET_MSG + "', but was '" + tweet + "'", tweet.startsWith(TWEET_MSG));

	}

	@Test
	public void testDoTweet() {
		boolean isUnitTest = true;
		assertTrue(twitterUpdater.doTweet("Hello there", isUnitTest));

	}

}