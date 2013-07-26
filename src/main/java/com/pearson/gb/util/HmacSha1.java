package com.pearson.gb.util;

import java.util.Arrays;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

public class HmacSha1 {
    private static Logger logger = Logger.getLogger(HmacSha1.class);
    public static final String HASH_SIGNATURE       = "HmacSHA1";
    public static final String OAUTH_HASH_SIGNATURE = "HMAC-SHA1";

    public static String hmacDigest(String msg, String keyString) {
	String digest = null;
	try {
	    SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), HASH_SIGNATURE);
	    Mac mac = Mac.getInstance(HASH_SIGNATURE);
	    mac.init(key);
	    byte[] bytes = mac.doFinal(msg.getBytes("UTF-8"));
	    digest = Base64.encodeBase64String(bytes);
	} catch (Exception e) {
	    logger.info(e);
	}
	return digest;
    }
}
