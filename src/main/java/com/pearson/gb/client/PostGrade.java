package com.pearson.gb.client;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.mortbay.jetty.HttpStatus;
import com.pearson.gb.util.LtiAuth;
import com.pearson.gb.xml.GradeBook;

public class PostGrade {

    private static Logger logger = Logger.getLogger(PostGrade.class);

    /**
     * Uses the LTI Grade exchange XML objects in the post call Also
     * uses OAuth1 for receiving server to use for validation
     *
     * From a high level:
     * 1 - Create a Post Object
     * 2 - Read in the xml payload and assign to the post object
     * 3 - Calculate the GNARLY oauth header for LTI and set the special sauce header
     * 4 - Perform the post and read the results
     */
    public static void postGrade(String url, String host, String consumerKey, String sharedSecret, String oAuthVersion, String xmlVersion, String msgIdentifier, String sourceId, String language, String grade) {
	logger.info("url: "           + url);
	logger.info("host: "          + host);
	logger.info("consumerKey: "   + consumerKey);
	logger.info("sharedSecret: "  + sharedSecret);
	logger.info("oAuthVersion: "  + oAuthVersion);
	logger.info("xmlVersion: "    + xmlVersion);
	logger.info("msgIdentifier: " + msgIdentifier);
	logger.info("sourceId: "      + sourceId);
	logger.info("language: "      + language);
	logger.info("grade: "         + grade);

	// Create an oauth consumer object with the key and secret
	OAuthConsumer consumer = new DefaultOAuthConsumer(consumerKey, sharedSecret);

	// Create an object to hold the post data
	PostMethod post = new PostMethod(url);

	// Grab a copy of the xml transformed by maven with the correct properties
	String postBody = GradeBook.getRequestXML(xmlVersion, msgIdentifier, sourceId, language, grade);
	logger.info("xml Payload: " + postBody);

	try {
	    // Generate a request entity from the xml and add it to the post object
	    RequestEntity entity = new StringRequestEntity(postBody, "application/xml", "UTF-8");
	    post.setRequestEntity(entity);

	    // Create an http client object to perform the post
	    int responseCode = 1;
	    HttpClient client = new HttpClient();

	    // Sign the url (why?)
	    consumer.sign(post.getURI().toString());

	    // Walk through the hoops to sign an oauth signature with all the special LTI sauce
	    String headerString = LtiAuth.generateSpecialOAuthHeader(
								     postBody,
								     url,
								     host,
								     consumerKey,
								     sharedSecret,
								     oAuthVersion
								     );
	    logger.info("headerString: " + headerString);

	    // Now set the Authorization header on the post object
	    post.setRequestHeader("Authorization", headerString);

	    // Dial the url, deliver the payload and check the return code
	    responseCode = client.executeMethod(post);
	    logger.info("responseCode: " + responseCode);
	    logger.info("xmlResponse: " + post.getResponseBodyAsString());

	} catch (Exception e) {
	    logger.info(e);
	}
    }

}
