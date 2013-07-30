package com.ladinc.mcp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ladinc.mcp.interfaces.MCPContorllersListener;
import com.ladinc.mcp.utils.NetworkUtils;
import com.ladinc.mcp.utils.ResponseUtils;
import com.ladinc.mcp.webpage.WebPageBuilder;

import fi.iki.elonen.NanoHTTPD;

public class MCP extends NanoHTTPD {
	
	private Boolean debugLogging = true;
	
	private int uniqueId = 0;
	
	private List<MCPContorllersListener> listenerList;
	
	public MCP(int portNumber)
	{
		super(portNumber);
        listenerList = new ArrayList<MCPContorllersListener>();
	}
	
	public MCP(){
        
		super(0);
		
		//this.debugLogging = true;
        listenerList = new ArrayList<MCPContorllersListener>();
    }
    
    protected void fireButtonDown(int controllerId, String buttonCode)
    {
    	
    	for (MCPContorllersListener l : listenerList) 
    	{
    		l.buttonDown(controllerId, buttonCode);
    	}
    }
    
    protected void fireButtonUp(int controllerId, String buttonCode)
    {
    	
    	for (MCPContorllersListener l : listenerList) 
    	{
    		l.buttonUp(controllerId, buttonCode);
    	}
    }
    
    protected void fireAnalogEvent(int controllerId, String analog, float x, float y)
    {
    	for (MCPContorllersListener l : listenerList) 
    	{
    		l.analogMove(controllerId, analog, x, y);
    	}
    }
    
    protected void fireOrientationEvent(int controllerId, float gamma, float beta, float alpha)
    {
    	for (MCPContorllersListener l : listenerList) 
    	{
    		l.orientation(controllerId, gamma, beta, alpha);
    	}
    }
    
    public void addMCPListener(MCPContorllersListener l) {
    	listenerList.add(l);
      }

     public void removeClickListener(MCPContorllersListener l) {
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
    
    private void handleOrientationEventFromClient(String controllerId, String gamma, String beta, String alpha)
    {
		if(debugLogging)
			System.out.println("Recieved Orientation Event for controller " + controllerId + " - gamma: " + gamma + ", beta: " +  beta + ", alpha:" + alpha );
		
		float gammaf = Float.parseFloat(gamma);
		float betaf = Float.parseFloat(beta);
		float alphaf = Float.parseFloat(alpha);
		
		fireOrientationEvent(Integer.parseInt(controllerId), gammaf, betaf, alphaf);
    }
    
    private Response serveCustom(String uri, Method method, Map<String, String> header, Map<String, String> parms, Map<String, String> files) 
    {
    	return WebPageBuilder.generateWebPage("",  WebPageBuilder.readFile("testBody"));
    }
      
    @Override
    public Response serve(String uri, Method method, Map<String, String> header, Map<String, String> parms, Map<String, String> files) 
    {
    	
    	if(debugLogging)
			System.out.println("Recieved Request for uri " + uri );
    	
    	//If the user is returning a custom webpage
    	if(uri.contains("custom/"))
    	{
    		return serveCustom(uri, method, header, parms, files);
    	}
    	
    	//Handle Events
    	if(uri.contains("buttonEvent"))
    	{	
    		handleButtonEventFromClient(parms.get("id"), parms.get("button"), parms.get("event"));
    		
    		return WebPageBuilder.generateWebPage("", parms.get("button"));
    		
    	}
    	else if(uri.contains("analogEvent"))
    	{
    		handleAnalogEventFromClient(parms.get("id"), parms.get("analog"), parms.get("x"), parms.get("y"));
    		
    		return WebPageBuilder.generateWebPage("", "OK");
    	}
    	else if(uri.contains("orientationEvent"))
    	{
    		handleOrientationEventFromClient(parms.get("id"), parms.get("gamma"), parms.get("beta"), parms.get("alpha"));
    		
    		return WebPageBuilder.generateWebPage("", "OK");
    	}
    	
    	
    	if(uri.contains("/images/"))
    	{    		
    		if(uri.contains(".png"))
        	{
        		return ResponseUtils.handlePNGRequest("/jQueryImages/" + uri.substring(1));
        	}
    		else if(uri.contains(".gif"))
    		{
    			return ResponseUtils.handleGIFRequest("/jQueryImages/" + uri.substring(1));
    		}
    		else
    		{
    			return WebPageBuilder.generateWebPage("", "?");
    		}
    	}
    	
    	
    	//Handle Request for Files
    	if(uri.contains(".css"))
    	{
    		if(debugLogging)
    			System.out.println("Handling CSS Request " + uri );
    		return ResponseUtils.handleCSSRequest("/CSS/" + uri.substring(1));
    	}
    	else if(uri.contains(".js"))
    	{
    		if(debugLogging)
    			System.out.println("Handling JS Request " + uri );
            return ResponseUtils.handleJSRequests("/Js/" + uri.substring(1));
    	}
    	else if(uri.contains(".png"))
    	{
    		if(debugLogging)
    			System.out.println("Handling Image Request " + uri );
    		return ResponseUtils.handlePNGRequest("/Images/" + uri.substring(1));
    	}
    	else if (uri.contains("favicon"))
    	{
    		return WebPageBuilder.generateWebPage("", "");
    	}
    	
    	
    	
    	if (uri.contains("canvas"))
    	{
    		return WebPageBuilder.generateWebPage(WebPageBuilder.readFile("Headers/canvasHeader"), WebPageBuilder.readFile("Bodys/canvasBody"));
    	}
    	else if(uri.contains("keyboard"))
    	{
    		return WebPageBuilder.generateWebPage(WebPageBuilder.readFile("Headers/keyboardHeader"), WebPageBuilder.readFile("Bodys/keyboardBody"));
    	}
    	else if(uri.contains("tilt"))
    	{
    		return WebPageBuilder.generateWebPage(WebPageBuilder.readFile("Headers/tiltHeader"), WebPageBuilder.readFile("Bodys/tiltBody"));
    	}
    	else if (uri.contains("redirect"))
    	{
    		uniqueId ++;
    		
    		if(parms.containsKey("choice"))
    		{
    			return WebPageBuilder.generateWebPage("", WebPageBuilder.returnJSRedirect("http://" + this.getAddressForClients() + "/" + parms.get("choice"), uniqueId));
    		}
    		else
    		{
    			return WebPageBuilder.generateWebPage("", WebPageBuilder.returnJSRedirect("http://" + this.getAddressForClients() + "/canvas", uniqueId));
    		}
    	}
    	else
    	{
    		return WebPageBuilder.generateWebPage(WebPageBuilder.readFile("Headers/landingPageHeader"), WebPageBuilder.readFile("Bodys/landingPageBody"));
    		//return webpageBuilder.generateWebPage(webpageBuilder.readFile("Headers/defaultHeader"), webpageBuilder.readFile("Bodys/defaultBody"));
    	}
    }
    
    
}
