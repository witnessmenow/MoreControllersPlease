package com.ladinc.mcp.webpage;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import com.ladinc.mcp.MCP;
import com.ladinc.mcp.RedirectOption;

import fi.iki.elonen.NanoHTTPD.Response;

public class WebPageBuilder {
	
	public static Response generateWebPage(String headerContent, String bodyContent)
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
	
	
	public static String readFile(String fileName) 
	{
		InputStream is = WebPageBuilder.class.getResourceAsStream( fileName);

	  
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
	
	public static String returnJSRedirect(String redirectUrl, int controllerId)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<script language=\"javascript\">");
		
		if(MCP.USE_IP_ADDRESS_AS_ID)
		{
			sb.append("window.location.href = \"" + redirectUrl + "\"");
		}
		else
		{
			sb.append("window.location.href = \"" + redirectUrl + "?id=" + controllerId + "\"");
		}
		sb.append("</script>");

		return sb.toString();
	}
	
	public static String returnMetaRedirect(String redirectUrl, int controllerId)
	{
		return "";	
	}
	
	public static String convertRedirectOptionListToString(List<RedirectOption> redirectOptions)
	{
		if(redirectOptions == null || redirectOptions.size() == 0)
		{
			return "";	
		}
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("<select name=\"choice\">");
		
		for (RedirectOption r : redirectOptions)
		{
			sb.append("<option value=\"" + r.url + "\">" + r.displayText + "</option>");
		}
		
		sb.append("</select>");
		
		return sb.toString();
	}
	

}
