package com.pearson.gb.util;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.net.URLEncoder;
import org.apache.log4j.Logger;
import com.google.common.base.Joiner;
import com.pearson.gb.xml.GradeBook;

public class LtiAuth {

    private static Logger logger = Logger.getLogger(LtiAuth.class);

    // UNDER NO CIRCUMSTANCES DEFINE OR SEND THE oauth_token HEADER OR
    // USE the oauth_token_secret AS PART OF THE HASHING KEY.
    // DOING SO WILL RESULT IN AN ACCESS DENIED RETURN PAYLOAD

    // ALSO, ENSURE YOUR OAUTH TIMESTAMP IS SMALLER THAN 32 BIT.
    // FAILURE TO DO SO WILL ALSO RESULT IN AN ACCESS DENIED PAYLOAD

    public static final String OAUTH_CONSUMER_KEY      = "oauth_consumer_key";
    public static final String OAUTH_NONCE             = "oauth_nonce";
    public static final String OAUTH_SIGNATURE_METHOD  = "oauth_signature_method";
    public static final String OAUTH_SIGNATURE         = "oauth_signature";
    public static final String OAUTH_TIMESTAMP         = "oauth_timestamp";
    public static final String OAUTH_VERSION           = "oauth_version";
 
    // Define the header that will go into the oauth signature
    // Note the lack of oauth_token or oauth_token_secret.
    private static final String[] signatureBaseHashOrder = {
	OAUTH_CONSUMER_KEY,
	OAUTH_NONCE,
	OAUTH_SIGNATURE_METHOD,
	OAUTH_TIMESTAMP,
	OAUTH_VERSION
    };


    // Define the order of the paramaters that make up the
    // Authorization header. As with the signature, exclude
    // oauth_token at all costs!
    private static final String[] authParamOrder = {
	OAUTH_CONSUMER_KEY,
	OAUTH_SIGNATURE_METHOD,
	OAUTH_SIGNATURE,
	OAUTH_TIMESTAMP,
	OAUTH_NONCE,
	OAUTH_VERSION
    };
    
    /**
     * This method will jump through all kinds of hoops to correctly
     * hash a url for LTI. It deviates from the OAuth spec. It is
     * important to realize this and respect it.
     */
    public static String generateSpecialOAuthHeader(String postBody, String url, String host, String consumerKey, String sharedSecret, String oAuthVersion) {
	logger.info("postBody: " + postBody);
	logger.info("url: " + url);
	logger.info("host: " + host);
	logger.info("consumerKey: " + consumerKey);
	logger.info("sharedSecret: " + sharedSecret);
	logger.info("oAuthVersion: " + oAuthVersion);
	
	// Grab a timestamp
	long now = System.currentTimeMillis();

	// Chop it down to the 32 bit range since this is all LTI can handle
	// WARNING! IF THIS VALUE IS GREATER THAN 32 BITS YOU WILL GET AN ACCESS DENIED
	long timeStamp = now / 1000;

	// Chope the nonce down more just for paranoias sake (rubber chicken has been waived)
	long nonce     = timeStamp / 1000;

	// Create a map to hold the values of the oauth parameters
	Map<String, String> map = new HashMap<String, String>();
	map.put(OAUTH_CONSUMER_KEY, consumerKey);
	map.put(OAUTH_NONCE, String.valueOf(nonce));
	map.put(OAUTH_SIGNATURE_METHOD, HmacSha1.OAUTH_HASH_SIGNATURE);
	map.put(OAUTH_TIMESTAMP, String.valueOf(timeStamp));
	map.put(OAUTH_VERSION, oAuthVersion);

	// Walk the signature Params list and assemble the values as
	// strings in order. This list will be the normalized
	// parameters
	List signatureParams = new ArrayList();
	for (String s : signatureBaseHashOrder) {
	    signatureParams.add(s + "=" + map.get(s));
	}

	// Join these params into a single string for later hashing
	String normalizedParams = Joiner.on("&").join(signatureParams);
	logger.info("normalizedParams: " + normalizedParams);

	// Now generate the signatureBase and hash it
	String signatureHash = "";
	String bodyHash = "";
	try {
	    // The signature base must be just like this or bad things will happen
	    String signatureBase = "" +
		"POST&" + 
		URLEncoder.encode(url,"UTF-8") + 
		"&" + 
		URLEncoder.encode(normalizedParams,"UTF-8");
	    logger.info("signatrueBase: " + signatureBase);

	    // THE KEY CANNOT HAVE THE TOKEN SECRET APPENDED! LTI
	    // BREAKS THE OAUTH SPEC IN THIS REGARD. KEEP IT EXACTLY
	    // LIKE THIS OR SUFFFER.
	    String key = sharedSecret + "&";
	    logger.info("key is: " + key);

	    // Hash the signature base with they non standard key
	    signatureHash = HmacSha1.hmacDigest(signatureBase, key);
	    map.put("oauth_signature", URLEncoder.encode(signatureHash,"UTF-8"));
	    logger.info("signatureHash: " + signatureHash);

	    // Hash the body as well. Not sure why. Not sure is this is needed.
	    bodyHash      = HmacSha1.hmacDigest(postBody, key);
	    map.put("oauth_body_hash", URLEncoder.encode(bodyHash,"UTF-8"));
	    logger.info("bodyHash: " + bodyHash);
	    
	} catch (Exception e) {
	    logger.info("OAuth header generation failed for grade exchange");
	}

	// Now we need to generate the Authorization header.  Walk the
	// list of authorization paramaeters in order and map them key
	// value pairs. Note the values must be wrapped in double
	// quotes.
	List authParamsList = new ArrayList();
	for (String s : authParamOrder) {
	    authParamsList.add(s + "=\"" + map.get(s) + "\"");
	};

	// Convert the key value paird to a comma delimited string
	String authParams = Joiner.on(",").join(authParamsList);

	// Return the OAuth header value
	return "OAuth realm=\"" + host + "\"," + authParams;
    }
}
