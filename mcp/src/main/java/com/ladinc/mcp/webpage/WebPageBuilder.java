package com.ladinc.mcp.webpage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import fi.iki.elonen.NanoHTTPD.Response;

public class WebPageBuilder {
	
	private static final String NEW_LINE = "<br>";
	
	public static String generateBodyContent()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("More-Controllers-Please");
        
        sb.append(generateController());

        
        
        return sb.toString();
	}
	
	public static String generateController()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("<table>");
		sb.append("<tr>");
		
		sb.append("<td>");
		sb.append(generateDpad());
		sb.append("</td>");

		sb.append("<td>");
		//sb.append(generateDpad());
		sb.append("</td>");

		sb.append("<td>");
		//sb.append(generateDpad());
		sb.append("</td>");
		
		sb.append("</tr>");
		sb.append("</table>");
		
		return sb.toString();
	}
	
	public static String generateDpad()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<table>");
		
		sb.append("<tr>");
		sb.append("<td>");
		sb.append("</td>");
		sb.append("<td style=\"text-align:center;font-size: 50;\" colspan=\"1\">");
		sb.append("<input style=\"font-size: 30; width:75px;height:75px\" id=\"dpadUp\" type=\"button\" value=\"UP\"");
		sb.append("</td>");
		sb.append("<td>");
		sb.append("</td>");
		sb.append("</tr>");
		
		
		sb.append("<tr>");
		sb.append("<td style=\"text-align:center;font-size: 50;\">");
		sb.append("<input style=\"font-size: 30; width:75px;height:75px\" id=\"dpadLeft\" type=\"button\" value=\"LEFT\"");
		sb.append("</td>");
		
		sb.append("<td>");
		sb.append("</td>");
		
		sb.append("<td style=\"text-align:center;font-size: 50;\">");
		sb.append("<input style=\"font-size: 30; width:75px;height:75px\" id=\"dpadRight\" type=\"button\" value=\"RIGHT\"");
		sb.append("</td>");
		
		sb.append("</tr>");
		
		sb.append("<tr>");
		sb.append("<td>");
		sb.append("</td>");
		sb.append("<td style=\"text-align:center;font-size: 50;\" colspan=\"1\">");
		sb.append("<input style=\"font-size: 30; width:75px;height:75px\" id=\"dpadDown\" type=\"button\" value=\"DOWN\"");
		sb.append("</td>");
		sb.append("<td>");
		sb.append("</td>");
		sb.append("</tr>");
		
		sb.append("</table>");
		return sb.toString();
	}
	
	public String generateHeaderContent()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("<title>More-Controllers-Please</title>");
		sb.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0\"/>");
		sb.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0\"/>");
		sb.append("<script type=\"text/javascript\" src=\"defaultController.js\"></script>");
		//sb.append(generateJavaScript());
		
        return sb.toString();
	}
	
	public Response generateWebPage(String headerContent, String bodyContent)
	{
		StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<head>");
        sb.append(headerContent);
        sb.append("</head>");
        sb.append("<body onLoad=\"initTouch()\">");
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

}
