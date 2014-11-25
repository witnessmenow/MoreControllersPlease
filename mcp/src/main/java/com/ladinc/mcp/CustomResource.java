package com.ladinc.mcp;

public class CustomResource 
{
	public CustomResource(String fileName, String fileContents)
	{
		this.fileName = fileName;
		this.fileContents = fileContents;
	}
	
	public String fileName;
	public String fileContents;
}
