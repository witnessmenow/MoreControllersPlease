package com.ladinc.mcp.webpage;

import java.io.InputStream;
import java.util.Scanner;

import fi.iki.elonen.NanoHTTPD.Response;

public class WebPageBuilder {
	
	public Response generateWebPage(String headerContent, String bodyContent)
	{
		StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<head>");
        sb.append(headerContent);
        sb.append("</head>");
        sb.append("<body onLoad=\"initMCP()\">");
        sb.append(bodyContent);
        sb.append("</body>");
        sb.append("</html>");
        return new Response(sb.toString());
	}
	
	
	public String readFile(String fileName) 
	{
		InputStream is = getClass().getResourceAsStream("/" + fileName);

	  
		Scanner filesScanner = null;
		try {
			filesScanner = new Scanner( is );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String fileContents = filesScanner.useDelimiter("\\A").next();
		filesScanner.close();
		return fileContents;
	}
	
	public String returnJSRedirect(String redirectUrl, int controllerId)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<script language=\"javascript\">");
		sb.append("window.location.href = \"" + redirectUrl + "?id=" + controllerId + "\"");
		sb.append("</script>");

		return sb.toString();
	}
	
	public String returnMetaRedirect(String redirectUrl, int controllerId)
	{
		return "";	
	}
	

}
