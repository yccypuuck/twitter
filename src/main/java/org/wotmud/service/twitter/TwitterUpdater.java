package org.wotmud.service.twitter;
// To add a new app to Twitter account go to https://apps.twitter.com/
// Create the app, copy the consumer key, generate the access token

import java.util.Date;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterUpdater {

	static long num_tweets = 0;
	static long lastTweetTime = 0;
	static long latestFileTime = 0;
	
    public boolean doTweet(String theTweet, boolean unitTest) {
    	if (theTweet == null) {
    		return false;
    	}
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




