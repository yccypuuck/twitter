package org.wotmud.service.twitter;

import java.io.File;

public interface TwitterListener {
	
	File[] getTweetFileList(String path);

	File getMostRecentFileInTimeFrame(File[] files, long start, long end);

	String getTweetFromFile(File file);
	
	void sleep();
	
	void setLastTweetTime(long time);
	
	long getLastTweetTime();
	
	String getMeTweet(String path, long start, long end);

}
