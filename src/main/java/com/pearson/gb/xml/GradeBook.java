package com.pearson.gb.xml;

public class GradeBook {
    public static final String getRequestXML(String version, String identifier, String sourceId, String language, String grade) {
	return "" +
	    "<imsx_POXEnvelopeRequest xmlns=\"http://www.imsglobal.org/services/ltiv1p1/xsd/imsoms_v1p0\">"+
	    "<imsx_POXHeader>" +
	    "<imsx_POXRequestHeaderInfo>" +
	    "<imsx_version>" + version + "</imsx_version>" +
	    "<imsx_messageIdentifier>" + identifier + "</imsx_messageIdentifier>" +
	    "</imsx_POXRequestHeaderInfo>" +
	    "</imsx_POXHeader>" +
	    "<imsx_POXBody>" +
	    "<replaceResultRequest>" +
	    "<resultRecord>" +
	    "<sourcedGUID>" +
	    "<sourcedId>" + sourceId + "</sourcedId>" +
	    "</sourcedGUID>" +
	    "<result>" +
	    "<resultScore>" +
            "<language>" + language + "</language>" +
            "<textString>" + grade + "</textString>" +
	    "</resultScore>" +
	    "</result>" +
	    "</resultRecord>" +
	    "</replaceResultRequest>" +
	    "</imsx_POXBody>" +
	    "</imsx_POXEnvelopeRequest>";
    }
}
