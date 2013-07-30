package com.ladinc.mcp.demos;

import com.ladinc.mcp.interfaces.MCPContorllersListener;

public class MyMCPListener implements MCPContorllersListener{

	public void buttonDown(int controllerId, String buttonCode) 
	{
		System.out.println("Button Down: Contorller: " + controllerId + " buttonCode: " + buttonCode);
	}

	public void buttonUp(int controllerId, String buttonCode) 
	{
		System.out.println("Button Up: Contorller: " + controllerId + " buttonCode: " + buttonCode);
	}

	public void analogMove(int controllerId, String analog, float x, float y) 
	{
		System.out.println("Analog Move: Contorller: " + controllerId + " Analog: " + analog + " X: " + x + " Y: " + y );
	}

	public void orientation(int controllerId, float gamma, float beta,
			float alpha) {
		System.out.println("orientation: Contorller: " + controllerId + " gamma: " + gamma + " beta: " + beta + " alpha: " + alpha );
		
	}


}
