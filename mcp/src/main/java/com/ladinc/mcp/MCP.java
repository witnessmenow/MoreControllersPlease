package com.ladinc.mcp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;
import java.util.Map;

import javax.swing.event.EventListenerList;

import com.ladinc.mcp.utils.NetworkUtils;
import com.ladinc.mcp.webpage.WebPageBuilder;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import fi.iki.elonen.ServerRunner;

public class MCP extends NanoHTTPD {
	
	private Boolean debugLogging = true;
	
	public class ButtonDownEvent extends EventObject 
	{
		public int controllerId;
		public String buttonCode;
		
		public ButtonDownEvent(Object source, int controllerId, String buttonCode) 
		{
			super(source);
			this.controllerId = controllerId;
			this.buttonCode = buttonCode;
  		}
	}
	
	public class ButtonUpEvent extends EventObject 
	{
		public int controllerId;
		public String buttonCode;
		
		public ButtonUpEvent(Object source, int controllerId, String buttonCode) 
		{
			super(source);
			this.controllerId = controllerId;
			this.buttonCode = buttonCode;
  		}
	}
	
	public interface MCPListener extends EventListener
	{
		void buttonDown(ButtonDownEvent e);
		void buttonUp(ButtonUpEvent e);
	}
	
	
	
	
	public WebPageBuilder webpageBuilder;
	
	public static final String
    MIME_PLAINTEXT = "text/plain",
    MIME_HTML = "text/html",
    MIME_JS = "application/javascript",
    MIME_CSS = "text/css",
    MIME_PNG = "image/png",
    MIME_DEFAULT_BINARY = "application/octet-stream",
    MIME_XML = "text/xml";
	
	private List<MCPListener> listenerList;
	
	public MCP(boolean debugLogging)
	{
		super(8082);
        listenerList = new ArrayList<MCPListener>();
        webpageBuilder = new WebPageBuilder();
        
        this.debugLogging = debugLogging;
	}
	
	public MCP(){
        
		super(8082);

        listenerList = new ArrayList<MCPListener>();
        webpageBuilder = new WebPageBuilder();
    }

    public static void main(String[] args) {
        ServerRunner.run(MCP.class);
        
    }
    
    protected void fireButtonDown(ButtonDownEvent e)
    {
    	
    	for (MCPListener l : listenerList) 
    	{
    		l.buttonDown(e);
    	}
    }
    
    protected void fireButtonUp(ButtonUpEvent e)
    {
    	
    	for (MCPListener l : listenerList) 
    	{
    		l.buttonUp(e);
    	}
    }
    
    public void addMCPListener(MCPListener l) {
    	listenerList.add(l);
      }

     public void removeClickListener(MCPListener l) {
    	  listenerList.remove(l);
      }
     
     public String getAddressForClients()
     {
    	 return NetworkUtils.getIPAddress(true) + ":" + this.getListeningPort();
     }

    
    private void handleButtonEventFromClient(int controllerId, String buttonCode, String eventType)
    {
		if(debugLogging)
			System.out.println("Recieved Button Event for controller " + controllerId + " - Button: " + buttonCode + ", Event: " +  eventType  );
    	
    	if (eventType.contains("down"))
    	{
    		fireButtonDown(new ButtonDownEvent(this, 1, buttonCode));
    	}
    	else
    	{
    		fireButtonUp(new ButtonUpEvent(this, 1, buttonCode));
    	}
    }
      
      
    @Override
    public Response serve(String uri, Method method, Map<String, String> header, Map<String, String> parms, Map<String, String> files) {
        
    	InputStream mbuffer = null;	
    	
    	if(uri.contains("buttonEvent"))
    	{	
    		handleButtonEventFromClient(1, parms.get("button"), parms.get("event"));
    		
    		return webpageBuilder.generateWebPage("", parms.get("button"));
    		
    	}
    	else if(uri.contains(".js"))
    	{
            
            return handleJSRequests(uri);
    	}
    	else if(uri.contains(".png"))
    	{
    		return handlePNGRequest(uri);
    	}
    	else if (uri.contains("canvas"))
    	{
    		return webpageBuilder.generateWebPage(webpageBuilder.readFile("Headers/canvasHeader"), webpageBuilder.readFile("Bodys/canvasBody"));
    	}
    	else
    	{
    		return webpageBuilder.generateWebPage(webpageBuilder.readFile("Headers/defaultHeader"), webpageBuilder.readFile("Bodys/defaultBody"));
    	}
    }
    	
    private Response handleJSRequests(String uriInput)
    {
    	//InputStream is = getClass().getResourceAsStream("/com/ladinc/mcp/files/js/"+ uriInput.substring(1));	
    	InputStream is = getClass().getResourceAsStream("/Js/" + uriInput.substring(1));
    	return new NanoHTTPD.Response(Status.OK, MIME_JS, is);
    }
    
    private Response handlePNGRequest(String uriInput)
    {
    	InputStream is = getClass().getResourceAsStream("/Images/" + uriInput.substring(1));
    	return new NanoHTTPD.Response(Status.OK, MIME_PNG, is);
    }
}
