package com.ladinc.mcp.interfaces;

import java.util.Map;

public interface MCPContorllersListener 
{
	void buttonDown(int controllerId, String buttonCode);
	void buttonUp(int controllerId, String buttonCode);
	void analogMove(int controllerId, String analog, float x, float y);
	void orientation(int controllerId, float gamma, float beta, float alpha);
	
	void pass(Map<String, String> header, Map<String, String> parms, Map<String, String> files);
}
