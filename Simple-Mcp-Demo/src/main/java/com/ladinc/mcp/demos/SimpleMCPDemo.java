package com.ladinc.mcp.demos;

import com.ladinc.mcp.MCP;

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
		
		System.out.println("Connect to: " + mcp.getAddressForClients());
		
		
		
		mcp.addMCPListener(new MyMCPListener());
		
		while(true)
		{
			Thread.sleep(500);
		}
	}

}
