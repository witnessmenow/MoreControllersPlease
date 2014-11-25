package com.ladinc.mcp.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.ladinc.mcp.MCP;
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
    
    public static Response handlePNGRequest(String uriInput, String fileContents)
    {
    	InputStream is = null;
    	
    	if(fileContents == null)
		{
			is = ResponseUtils.class.getResourceAsStream(uriInput);
		}
		else
		{
			return new NanoHTTPD.Response(Status.OK, MIME_PNG, fileContents);
//			try {
//				is = new FileInputStream(file);
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
    	return new NanoHTTPD.Response(Status.OK, MIME_PNG, is);
    }
    
    public static Response handleCSSRequest(String uriInput)
    {
    	InputStream is = ResponseUtils.class.getResourceAsStream(uriInput);
    	return new NanoHTTPD.Response(Status.OK, MIME_CSS, is);
    }
    
    public static Response handleGIFRequest(String uriInput, String fileContents)
    {
    	if(MCP.SHOW_DEBUG_LOGGING)
    		System.out.println("gif: " + uriInput);
    	
    	InputStream is = null;
    	
    	if(fileContents == null)
		{
			is = ResponseUtils.class.getResourceAsStream(uriInput);
		}
    	else
		{
    		return new NanoHTTPD.Response(Status.OK, MIME_GIF, fileContents);
//			try {
//				is = new FileInputStream(file);
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
    	
    	return new NanoHTTPD.Response(Status.OK, MIME_GIF, is);
    }
    
    

}
