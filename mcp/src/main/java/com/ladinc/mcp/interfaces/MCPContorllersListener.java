package com.ladinc.mcp.interfaces;

public interface MCPContorllersListener 
{
	void buttonDown(int controllerId, String buttonCode);
	void buttonUp(int controllerId, String buttonCode);
	void analogMove(int controllerId, String analog, float x, float y);
	void orientation(int controllerId, float gamma, float beta, float alpha);
}
