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
	
	private int uniqueId = 0;
	
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
		void buttonDown(int controllerId, String buttonCode);
		void buttonUp(int controllerId, String buttonCode);
		void analogMove(int controllerId, String analog, float x, float y);
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
        
		super(0);
		
		this.debugLogging = true;
        listenerList = new ArrayList<MCPListener>();
        webpageBuilder = new WebPageBuilder();
    }

//    public static void main(String[] args) {
//        ServerRunner.run(MCP.class);
//        
//    }
    
    protected void fireButtonDown(int controllerId, String buttonCode)
    {
    	
    	for (MCPListener l : listenerList) 
    	{
    		l.buttonDown(controllerId, buttonCode);
    	}
    }
    
    protected void fireButtonUp(int controllerId, String buttonCode)
    {
    	
    	for (MCPListener l : listenerList) 
    	{
    		l.buttonUp(controllerId, buttonCode);
    	}
    }
    
    protected void fireAnalogEvent(int controllerId, String analog, float x, float y)
    {
    	for (MCPListener l : listenerList) 
    	{
    		l.analogMove(controllerId, analog, x, y);
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

    
    private void handleButtonEventFromClient(String controllerId, String buttonCode, String eventType)
    {
		if(debugLogging)
			System.out.println("Recieved Button Event for controller " + controllerId + " - Button: " + buttonCode + ", Event: " +  eventType  );
    	
    	if (eventType.contains("down"))
    	{
    		fireButtonDown( Integer.parseInt(controllerId), buttonCode);
    	}
    	else
    	{
    		fireButtonUp( Integer.parseInt(controllerId), buttonCode);
    	}
    }
    
    private void handleAnalogEventFromClient(String controllerId, String analogCode, String x, String y)
    {
		if(debugLogging)
			System.out.println("Recieved Analog Event for controller " + controllerId + " - Analog: " + analogCode + ", X: " +  x + ", Y:" + y );
		
		float xf = Float.parseFloat(x);
		float yf = Float.parseFloat(y);
		
		fireAnalogEvent(Integer.parseInt(controllerId), analogCode, xf, yf);
    }
      
      
    @Override
    public Response serve(String uri, Method method, Map<String, String> header, Map<String, String> parms, Map<String, String> files) {
        
    	InputStream mbuffer = null;	
    	
    	if(uri.contains("buttonEvent"))
    	{	
    		handleButtonEventFromClient(parms.get("id"), parms.get("button"), parms.get("event"));
    		
    		return webpageBuilder.generateWebPage("", parms.get("button"));
    		
    	}
    	else if(uri.contains("analogEvent"))
    	{
    		handleAnalogEventFromClient(parms.get("id"), parms.get("analog"), parms.get("x"), parms.get("y"));
    		
    		return webpageBuilder.generateWebPage("", "OK");
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
    	else if (uri.contains("favicon"))
    	{
    		return webpageBuilder.generateWebPage("", "");
    	}
    	else
    	{
    		uniqueId ++;
    		return webpageBuilder.generateWebPage("", webpageBuilder.returnJSRedirect("http://" + this.getAddressForClients() + "/canvas", uniqueId));
    		//return webpageBuilder.generateWebPage(webpageBuilder.readFile("Headers/defaultHeader"), webpageBuilder.readFile("Bodys/defaultBody"));
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
