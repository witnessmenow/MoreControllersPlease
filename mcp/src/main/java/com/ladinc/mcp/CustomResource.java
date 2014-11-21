package com.ladinc.mcp;

import java.io.InputStream;

public class CustomResource 
{
	public CustomResource(String fileName, InputStream stream)
	{
		this.fileName = fileName;
		this.stream = stream;
	}
	
	public String fileName;
	public InputStream stream;
}
