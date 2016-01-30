package org.wotmud.service.twitter;

import java.io.File;

public class Main {

	public static void main(String[] args) {

		TwitterListener twitterListener = new TwitterListenerImpl();
		TwitterUpdater twitterUpdater = new TwitterUpdater();
		
		while (true) {

			File[] files = twitterListener.getTweetFileList("./tmp/");

			// Look for the most recent file that is at least 10 minutes old,
			// but not older than 40 minutes
			File tweetFile = twitterListener.getMostRecentFileInTimeFrame(files, 10 * 60 * 1000, 40 * 60 * 1000);

			String tweet = twitterListener.getTweetFromFile(tweetFile);

			if (twitterUpdater.doTweet(tweet)) {
				twitterListener.setLastTweetTime(System.currentTimeMillis());
			}
			twitterListener.sleep();
		}
		
	}

}
