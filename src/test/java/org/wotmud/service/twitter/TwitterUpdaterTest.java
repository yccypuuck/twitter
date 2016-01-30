package org.wotmud.service.twitter;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author flash
 *
 */
public class TwitterUpdaterTest extends TestBase {

	TwitterUpdater twitterUpdater;

	@Before
	public void setUp() {
		twitterUpdater = new TwitterUpdaterConsole();
	}

	@After
	public void cleanUp() {
		twitterUpdater = null;
	}

	@Test
	public void testSetUp() {
		// intentionally left empty
	}

	@Test
	public void testDoTweet() {
		assertTrue(twitterUpdater.doTweet("Hello there"));
		assertTrue(twitterUpdater.doTweet("Hello there"));
	}

}