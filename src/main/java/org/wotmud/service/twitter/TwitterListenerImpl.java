package org.wotmud.service.twitter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Date;

public class TwitterListenerImpl implements TwitterListener{

	private long lastTweetTime = 0;
	private long latestFileTime = 0;
	private String lastTweet = "";
	
	public void setLastTweetTime(long time) {
		lastTweetTime = time;
	}
	
	public long getLastTweetTime() {
		return lastTweetTime;
	}
	
	public File[] getTweetFileList(String path) {
		if (path == null || path.length() == 0) {
			return null;
		}
		// create new file object
		File file = new File(path);

		// array of files and directory
		File[] allFiles = file.listFiles(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				if (name.startsWith("tweet."))
					return true;
				return false;
			}
		});
		return allFiles;

	}
	
	// Only return files older than start, and newer than end
	public File getMostRecentFileInTimeFrame(File[] files, long start, long end) {
		if (files == null || files.length == 0 || start > end) {
			return null;
		}
		try {
			long mostRecentTweet = latestFileTime;
			File myFile = files[0];
			for (File f : files) {
				if (f.lastModified() < latestFileTime) {
					f.deleteOnExit();
					continue;
				}
				long fileAge = System.currentTimeMillis() - f.lastModified();
				if (fileAge > start && fileAge < end) {
					mostRecentTweet = Math.max(mostRecentTweet, f.lastModified());
					myFile = f;
				}
			}
			
			if (mostRecentTweet >= lastTweetTime)
				return myFile;
		    
	      } catch(Exception e){
	    	  System.out.println(new Date().toString() + ": Exception trying to open directory list");
	         // if any error occurs
	         e.printStackTrace();
	      }
	      
		return null;
	}

	public String getTweetFromFile(File file) {
		if (file == null || file.getTotalSpace() == 0) {
			return null;
		}
		// This will reference one line at a time
		String result = null;
    	try {
    		// FileReader reads text files in the default encoding.
    		FileReader fileReader = new FileReader(file);

    		// Always wrap FileReader in BufferedReader.
    		BufferedReader bufferedReader = new BufferedReader(fileReader);
    		String line = bufferedReader.readLine();
    		
    		if (line == null || line.length() == 0)
    			//Empty first line in the file 
    			System.out.println(new Date().toString() + ": Nothing to tweet.");
    		
    		else if (lastTweet.equals(line + " #wotmud")) 
    			// Don't tweet the same text twice in a row
				System.out.println(new Date().toString() + ": NOT Tweeting duplicate of '" + lastTweet + "'");
			
    		else 
				lastTweet = line + " #wotmud";
			
    		result = lastTweet;
    		lastTweetTime = System.currentTimeMillis();
    		// Always close files.
    		bufferedReader.close();
        
    		// Delete the tweet file now that we've processed it
    		file.deleteOnExit();

    	}
    	catch(FileNotFoundException ex) {
    		System.out.println(new Date().toString() + ": Unable to open file '" + file.getName() + "'");                
    	}
    	catch(IOException ex) {
    		System.out.println(new Date().toString() + ": Error reading file '" + file.getName() + "'");                  
    		// Or we could just do this: 
    		ex.printStackTrace();
    	} 
    	return result;
	}
	
	public void sleep() {
		try {
			long sleepTime;
			
			// 180 minutes since last tweet?
			if ( (System.currentTimeMillis() - lastTweetTime) < (1000 * 60 * 180) ) {
				sleepTime = (180 * 60 * 1000) - (System.currentTimeMillis() - lastTweetTime);  // No? Sleep until then.
				// Don't print that we're sleeping every 60 seconds, just that we're sleeping for 180 minutes
				System.out.println(new Date().toString() + ": Sleeping " + sleepTime/1000 + " seconds");
			} else
				sleepTime = 60 * 1000; // Sleep only 60 seconds waiting for new tweet
			
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public String getMeTweet(String path, long start, long end) {
		File[] files = getTweetFileList(path);
		File file = getMostRecentFileInTimeFrame(files, start, end);
		return getTweetFromFile(file);
	}


}
