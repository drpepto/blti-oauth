package com.pearson.gb;

import com.pearson.gb.client.PostGrade;
import org.apache.log4j.Logger;

public class Main {
    private static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
	// These come from the maven pom
	String url           = args[0];
	String host          = args[1];
	String consumerKey   = args[2];
	String sharedSecret  = args[3];
	String oAuthVersion  = args[4];
	String xmlVersion    = args[5];
	String msgIdentifier = args[6];
	String sourceId      = args[7];
	String language      = args[8];
	String grade         = args[9];
	try {
	    logger.info("Main class called with: " + args);
	    PostGrade.postGrade(url, host, consumerKey, sharedSecret, oAuthVersion, xmlVersion, msgIdentifier, sourceId, language, grade);
	} catch (Exception e) {
	    logger.info("Main: Exception: " + e);
	}
    }
}
