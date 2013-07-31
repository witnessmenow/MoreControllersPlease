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
		MCP mcp = new MCP(8888);
		
		System.out.println("Starting Server");
		
		mcp.start();
		
		//add custom redirect options to the landing page
		mcp.redirectOptions.add(new RedirectOption("custom/testPage", "Custom Page Demo"));
		
		System.out.println("Connect to: " + mcp.getAddressForClients());
		
		
		
		mcp.addMCPListener(new MyMCPListener());
		
		while(true)
		{
			Thread.sleep(500);
		}
	}

}
