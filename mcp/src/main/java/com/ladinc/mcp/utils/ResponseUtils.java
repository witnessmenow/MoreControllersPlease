package com.ladinc.mcp.utils;

import java.io.InputStream;

import com.ladinc.mcp.webpage.WebPageBuilder;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.Status;

public class ResponseUtils 
{
	public static final String
    MIME_PLAINTEXT = "text/plain",
    MIME_HTML = "text/html",
    MIME_JS = "application/javascript",
    MIME_CSS = "text/css",
    MIME_PNG = "image/png",
    MIME_DEFAULT_BINARY = "application/octet-stream",
    MIME_XML = "text/xml",
    MIME_GIF = "image/gif";
	
    public static Response handleJSRequests(String uriInput)
    {
    	InputStream is = ResponseUtils.class.getResourceAsStream(uriInput);
    	
    	return new NanoHTTPD.Response(Status.OK, MIME_JS, is);
    }
    
    public static Response handlePNGRequest(String uriInput)
    {
    	InputStream is = ResponseUtils.class.getResourceAsStream(uriInput);
    	return new NanoHTTPD.Response(Status.OK, MIME_PNG, is);
    }
    
    public static Response handleCSSRequest(String uriInput)
    {
    	InputStream is = ResponseUtils.class.getResourceAsStream(uriInput);
    	return new NanoHTTPD.Response(Status.OK, MIME_CSS, is);
    }
    
    public static Response handleGIFRequest(String uriInput)
    {
    	System.out.println("gif: " + uriInput);
    	
    	InputStream is = ResponseUtils.class.getResourceAsStream(uriInput);
    	return new NanoHTTPD.Response(Status.OK, MIME_GIF, is);
    }
    
    

}
