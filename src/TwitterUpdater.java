// To add a new app to Twitter account go to https://apps.twitter.com/
// Create the app, copy the consumer key, generate the access token

//import java.util.List;
import java.util.Date;
import java.io.*;
//import java.sql.Time;
//import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.TimeUnit;

//import twitter4j.GeoLocation;
//import twitter4j.Query;
//import twitter4j.QueryResult;
//import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
//import twitter4j.auth.RequestToken;
import twitter4j.conf.*;

public class TwitterUpdater {
	
	static long num_tweets = 0;
	static long lastTweetTime = 0;
	static long latestFileTime = 0;

	public static void main(String[] args) {
		TwitterUpdater TU = new TwitterUpdater();
		TU.seekTweets();
	}
	
	// Only return files older than millisecondsOld, and newer than millisecondsNew
	public String FindFile(long millisecondsOld, long millisecondsNew) {
		   		      
		      File file = null;
		      String[] paths;
		      		            
		      try{      
		         // create new file object
		         file = new File("./tmp/");
		                                 
		         // array of files and directory
		         paths = file.list();
		            
		         // for each name in the path array
		         for(String path : paths)
		         {
		            if (path.startsWith("tweet.")) {
			            Path file2 = Paths.get("./tmp/"+path);
			            BasicFileAttributes attr = Files.readAttributes(file2, BasicFileAttributes.class);
			            
			            if ((System.currentTimeMillis() - attr.creationTime().to(TimeUnit.MILLISECONDS)) > millisecondsOld && // it is older than this
			            	(System.currentTimeMillis() - attr.creationTime().to(TimeUnit.MILLISECONDS)) < millisecondsNew && // but newer than this
			            		attr.creationTime().to(TimeUnit.MILLISECONDS) > latestFileTime) { // And newer than last file tweeted
			            	
			            	latestFileTime = attr.creationTime().to(TimeUnit.MILLISECONDS);
			            	
			                // display time and date using toString()
			                System.out.println(new Date().toString() + ": '"+file2+"' is older than "+millisecondsOld/1000+
			                		" seconds, newer than "+millisecondsNew/1000+" seconds, and newer than last tweeted file"
			                		);
			                
			            	return("./tmp/"+path);
			            }
		            }
		         }
		      }catch(Exception e){
		    	  System.out.println(new Date().toString() + ": Exception trying to open directory list");
		         // if any error occurs
		         e.printStackTrace();
		      }
		      
			return null;
	}
	
	public void seekTweets() {
		// Keep an eye on the mud /tmp directory for any file named tweet.*
		
		// The name of the file to open.
        String fileName = null;
        String lastTweet = null;
                                   
        while( true ) {
        	
        fileName = FindFile(10*60*1000, 40*60*1000); // Look for files that are at least 10 minutes old, but not older than 40 minutes
        if (fileName != null && fileName.length() >= 0) {
                    
        	// This will reference one line at a time
        	String line = null;

        	try {
        		// FileReader reads text files in the default encoding.
        		FileReader fileReader = new FileReader(fileName);

        		// Always wrap FileReader in BufferedReader.
        		BufferedReader bufferedReader = new BufferedReader(fileReader);

        		while((line = bufferedReader.readLine()) != null) {
        			if (lastTweet == null || !lastTweet.equals(line + " #wotmud"))  { // Don't tweet the same text twice in a row
        				lastTweet = line + " #wotmud";
        				doTweet(lastTweet, false);
        			}
        			else if (lastTweet != null)
        				System.out.println(new Date().toString() + ": NOT Tweeting duplicate of '" + lastTweet + "'");
        		}   

        		// Always close files.
        		bufferedReader.close();
            
        		// Delete the tweet file now that we've processed it
        		Files.delete(Paths.get(fileName));
        	}
        	catch(FileNotFoundException ex) {
        		System.out.println(new Date().toString() + ": Unable to open file '" + fileName + "'");                
        	}
        	catch(IOException ex) {
        		System.out.println(new Date().toString() + ": Error reading file '" + fileName + "'");                  
        		// Or we could just do this: 
        		ex.printStackTrace();
        	}
        }
        
        try {
        	
        	long sleepTime;
        		
        	if ( (System.currentTimeMillis() - lastTweetTime) < (1000 * 60 * 180) ) // 180 minutes since last tweet?
        		sleepTime = (180 * 60 * 1000) - (System.currentTimeMillis() - lastTweetTime);  // No? Sleep until then.
        	else
        		sleepTime = 60 * 1000; // Sleep only 60 seconds waiting for new tweet
        		
        	// Don't print that we're sleeping every 60 seconds, just that we're sleeping for 180 minutes
        	if (sleepTime != 60 * 1000) System.out.println(new Date().toString() + ": Sleeping " + sleepTime/1000 + " seconds");
       		Thread.sleep(sleepTime);
    	}
        catch (InterruptedException e) {
        		e.printStackTrace();
        }
      } // end while
        
    } // end method
        	
        	
        	
        	
    public boolean doTweet(String theTweet, boolean unitTest) {

    	// For wot_imms
    	String CONSUMER_KEY = ""; //enter your consumer key
    	String CONSUMER_KEY_SECRET = ""; //enter your consumer key secret
    	
    	ConfigurationBuilder builder = new ConfigurationBuilder();
    	builder.setOAuthConsumerKey(CONSUMER_KEY);
    	builder.setOAuthConsumerSecret(CONSUMER_KEY_SECRET);
    	Configuration configuration = builder.build();
    	TwitterFactory factory = new TwitterFactory(configuration);
    	Twitter twitter = factory.getInstance();
        
        // For wot_imms
        String accessToken = ""; //enter your twitter access token
        String accessTokenSecret = ""; //enter your twitter secret token

        AccessToken oathAccessToken = new AccessToken(accessToken, accessTokenSecret);
        twitter.setOAuthAccessToken(oathAccessToken);

        try {
        	if (num_tweets == 0 && !unitTest) twitter.verifyCredentials();
        
        	System.out.println(new Date().toString() + ": Tweet #"+ num_tweets + ": "+theTweet);
        
        	if (!unitTest) twitter.updateStatus(theTweet);
        	num_tweets++;
        	lastTweetTime = System.currentTimeMillis();
        	return true;
       }
  	   catch (TwitterException e) {
  		 System.out.println(new Date().toString() + ": Got a problem " + e.getStatusCode());
      	         e.printStackTrace(); 
      	 return false;
  	   }
        
    } // End set method
        
} // End class




