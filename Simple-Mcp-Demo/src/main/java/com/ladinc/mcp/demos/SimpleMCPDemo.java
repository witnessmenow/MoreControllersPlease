package com.ladinc.mcp.demos;

import com.ladinc.mcp.MCP;
import com.ladinc.mcp.RedirectOption;

public class SimpleMCPDemo {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception 
	{
		MCP mcp = MCP.tryCreateAndStartMCPWithPort(8888);
		
		MCP.USE_IP_ADDRESS_AS_ID = false;
		
		//add custom redirect options to the landing page
		mcp.redirectOptions.add(new RedirectOption("testPage", "Custom Page Demo"));
		mcp.redirectOptions.add(new RedirectOption("playscape", "Playscape"));
		
		mcp.customLinks.add("testPage");
		mcp.customLinks.add("target.png");
		mcp.customLinks.add("playscape");
		
		System.out.println("Connect to: " + mcp.getAddressForClients());
		
		
		
		mcp.addMCPListener(new MyMCPListener());
		
		while(true)
		{
			Thread.sleep(500);
		}
	}

}
