package org.wotmud.service.twitter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

/**
 * @author flash
 *
 */
public class testTwitterUpdater extends TestCase {

	public static TwitterUpdater TU;
	public static String fileName;
	
  public void testFindFile() {

	  TU = new TwitterUpdater();
	  
	  fileName = TU.FindFile(10000*1000, 5000*1000); // Look for files that are ancient (to not get any hopefully)
	  
	  assertTrue(fileName == null || fileName.length() >= 0);
	  
	  // If there is no last tweet file time then we couldn't have gotten back a filename
	  if (TwitterUpdater.latestFileTime == 0)  
		  assertTrue(fileName == null);
	  
	  // If no filename, create one
	  if (fileName == null) {
		  BufferedWriter output;
		  
	        try {
	            File file = new File("./tmp/tweet.test");
	            output = new BufferedWriter(new FileWriter(file));
	            output.write("unit testing");
	            if ( output != null ) output.close();
	        } catch ( IOException e ) {
	            e.printStackTrace();
	        }
	    
	        // Ok now try again. It should still not show anything if we just created the file! It's not old enough
	        fileName = TU.FindFile(10*60*1000, 5000*1000);
	        assertTrue(fileName == null);
	  }
	  
	  // Now we've created a file, let's shorten the time needed
	  fileName = TU.FindFile(0, 99999); // Look for a file of (almost) any age
	  
	  assertTrue(fileName == null || fileName.length() >= 0);
	  
	  if (fileName != null)
	  {
		  Path path = Paths.get(fileName);
		
		  try {
			  BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
			  assertTrue( (System.currentTimeMillis() - attr.creationTime().to(TimeUnit.MILLISECONDS)) < 5000); // Should be less than 5 seconds old
			  Files.delete(Paths.get(fileName)); // Clean up
		  }
		  catch(Exception e){
			  System.out.println(new Date().toString() + ": Exception trying to get file attributes");
			  // if any error occurs
			  e.printStackTrace();
		  }
	  }
  }

 public void testDoTweet() {
	 
	 TU = new TwitterUpdater();
	 
	 assertTrue(TU.doTweet("Hello there", true));

	/*
    LinkedList<Integer> list = new LinkedList();
    list.add(1);
    assertTrue(list.size() == 1);
    assertTrue(list.getFirst().equals(1));
    assertTrue(list.size() == 2);
    */
 }

}