package org.wotmud.service.twitter;

import java.util.Date;

import twitter4j.Twitter;

public class TwitterUpdaterConsole extends TwitterUpdater {

	Twitter twitter;

	public TwitterUpdaterConsole() {
	}
	
	@Override
	protected void setup() {
		System.out.println("Console output is used for tweeting");
	}

	@Override
	public boolean doTweet(String theTweet) {
		if (theTweet == null) {
			return false;
		}

		System.out.println(new Date().toString() + ": Tweet #" + num_tweets + ": " + theTweet);

		num_tweets++;
		lastTweetTime = System.currentTimeMillis();
		return true;

	} // End set method

} // End class
