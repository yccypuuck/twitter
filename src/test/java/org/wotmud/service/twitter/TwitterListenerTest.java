package org.wotmud.service.twitter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author flash
 *
 */

public class TwitterListenerTest extends TestBase {

	public final String TWEET_MSG = "unit testing";
	public final String PATH = "./tmp/";
	TwitterListener twitterListener;

	@Before
	public void setUp() {
		twitterListener = new TwitterListenerImpl();

		createDirectoryIfNotExist(PATH);

		createFile(PATH, TWEET_MSG, "tweet.test");
	}

	@After
	public void cleanUp() {
		File file = new File(PATH);
		file.delete();

		file = new File("./tmp_empty/");
		file.delete();

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

}