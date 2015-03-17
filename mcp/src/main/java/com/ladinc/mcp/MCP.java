package com.ladinc.mcp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.BindException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.ladinc.mcp.interfaces.MCPContorllersListener;
import com.ladinc.mcp.utils.NetworkUtils;
import com.ladinc.mcp.utils.ResponseUtils;
import com.ladinc.mcp.webpage.WebPageBuilder;

import fi.iki.elonen.NanoHTTPD;

public class MCP extends NanoHTTPD {
	
	private int uniqueId = 0;
	
	private List<MCPContorllersListener> listenerList;
	
	public List<String> customLinks;
	
	public List<CustomResource> customLinkDirect;
	
	public Map<String, JSONObject> hearbeatResponses;
	
	public static boolean SHOW_DEBUG_LOGGING = false;
	
	public static boolean USE_IP_ADDRESS_AS_ID = false;
	
	public static String FILE_LOCATION_PREFIX = "";
	
	public String baseMCPRocksURL = "http://mcp.rocks";
	
	public String mcpRocksDataLabel = null;
	
	public boolean mcpRocksVerified = false;
	
	public String defaultStartPage = null;
	
	public Timer timer;
	
	
	//Tries to create a mcp instacnce with given port, if it fails it deafults to a random port
	public static MCP tryCreateAndStartMCPWithPort(int portNumber)
	{
		
		System.out.println("Trying to create server using port  " +  portNumber);
		MCP mcp = new MCP(portNumber);
		
		try {
			if(SHOW_DEBUG_LOGGING)
			{
				System.out.println("Trying to start server");
			}
			mcp.start();
		} 
		catch (Exception e) {
			
			System.out.println("Caught Exception");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(mcp.getListeningPort() <= 0)
		{	
			//port was in use, using a random one
			if(SHOW_DEBUG_LOGGING)
			{
				System.out.println("Port must have been unavailable  ");
				System.out.println("Trying to create server random port  ");
			}
			mcp = new MCP(0);
			
			try {
				if(SHOW_DEBUG_LOGGING)
				{
					System.out.println("Trying to start server");
				}
				mcp.start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return mcp;
		
	}
	
	public MCP(int portNumber)
	{
		super(portNumber);
		setDefaults();

	}
	
	public MCP(){
        
		super(0);
		setDefaults();
    }
	
	public void registerWithMCPRocks(String gameName) throws ParseException, IOException
	{
		URL request = new URL(baseMCPRocksURL + "/register.php?internalIp=" + getIpAddressForClients() + "&game=" + getGameName(gameName));
		Scanner scanner = new Scanner(request.openStream());
		String response = scanner.useDelimiter("\\Z").next();
		JSONObject json = (JSONObject)new JSONParser().parse(response);
		mcpRocksDataLabel = (String) json.get("name");
		scanner.close();
		
		mcpRocksVerified = verifyMCPRocks(gameName);
		
		if(mcpRocksVerified)
		{
			setMCPRocksHeartbeat();
			registerRemoveGameFromMCPRocksOnExit();
		}
	}
	
	
	
	private void setMCPRocksHeartbeat() throws MalformedURLException
	{
		if(this.timer != null)
		{
			timer.cancel();
			timer.purge();
		}
		
		this.timer = new Timer();
		keepAliveUrl =  new URL(baseMCPRocksURL + "/keepAlive.php?key=" + mcpRocksDataLabel);
		timer.scheduleAtFixedRate(new TimerTask() {
			  @Override
			  public void run() 
			  {
				  sendKeepAliveToMCPRocks();
			  }
			}, 60000, 60000);
	}
	
	public URL keepAliveUrl;
	public void sendKeepAliveToMCPRocks()
	{
		Scanner scanner;
		try
		{
			scanner = new Scanner(keepAliveUrl.openStream());
			String response = scanner.useDelimiter("\\Z").next();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private String getGameName(String gameName) throws UnsupportedEncodingException
	{
		return URLEncoder.encode(gameName, "UTF-8").replace("+", "%20"); 
	}
	
	
	private boolean verifyMCPRocks(String gameName) throws IOException
	{
		URL request = new URL(baseMCPRocksURL + "/verify.php?internalIp=" + getIpAddressForClients() + "&game=" + getGameName(gameName));
		Scanner scanner = new Scanner(request.openStream());
		String response = scanner.useDelimiter("\\Z").next();
		return (response.contains("true"));
	}
	
	public void registerRemoveGameFromMCPRocksOnExit()
	{

		  Runtime.getRuntime().addShutdownHook(new Thread() 
		  {
			  @Override
			  public void run() 
			  {
				  removeFromMCPRocks();
			  }
		  });
	}
	
	public void removeFromMCPRocks()
	{
		try 
		{
			URL request;
			request = new URL(baseMCPRocksURL + "/remove.php?key=" + mcpRocksDataLabel);
			Scanner scanner = new Scanner(request.openStream());
			String response = scanner.useDelimiter("\\Z").next();
		 }
		catch (IOException e) 
		{
		  e.printStackTrace();
		}
	}
	
	private void setDefaults()
	{
        listenerList = new ArrayList<MCPContorllersListener>();
        
        setDefaultRedirects();
        
        customLinks = new ArrayList<String>();
        
        hearbeatResponses = new HashMap<String, JSONObject>();
	}
	
	public void setDefaultRedirects()
	{
		redirectOptions = new ArrayList<RedirectOption>();
		
		redirectOptions.add(new RedirectOption(CANVAS_URL, "Canvas (Touch Screen)"));
		redirectOptions.add(new RedirectOption(TILT_URL, "Tilt (Accelerometer)"));
		redirectOptions.add(new RedirectOption(KEYBOARD_URL, "Keyboard (Physical Keyboard)"));
	}
	
	public List<RedirectOption> redirectOptions;
	
	public static String CANVAS_URL = "canvas";
	public static String TILT_URL = "tilt";
	public static String KEYBOARD_URL = "keyboard";
	
    
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
    
    protected void firePassEvent(Map<String, String> header, Map<String, String> parms, Map<String, String> files)
    {
    	for (MCPContorllersListener l : listenerList) 
    	{
    		l.pass(header, parms, files);
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
    	 if(!mcpRocksVerified)
    		 return getIpAddressForClients();
    	 else
    		 return baseMCPRocksURL.replace("http://", "");
     }
     
     public String getIpAddressForClients()
     {
    	 return NetworkUtils.getIPAddress(true) + ":" + this.getListeningPort();
     }

    
    private void handleButtonEventFromClient(String controllerId, String buttonCode, String eventType)
    {
		if(SHOW_DEBUG_LOGGING)
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
		if(SHOW_DEBUG_LOGGING)
			System.out.println("Recieved Analog Event for controller " + controllerId + " - Analog: " + analogCode + ", X: " +  x + ", Y:" + y );
		
		float xf = Float.parseFloat(x);
		float yf = Float.parseFloat(y);
		
		fireAnalogEvent(Integer.parseInt(controllerId), analogCode, xf, yf);
    }
    
    private void handleOrientationEventFromClient(String controllerId, String gamma, String beta, String alpha)
    {
		if(SHOW_DEBUG_LOGGING)
			System.out.println("Recieved Orientation Event for controller " + controllerId + " - gamma: " + gamma + ", beta: " +  beta + ", alpha:" + alpha );
		
		float gammaf = Float.parseFloat(gamma);
		float betaf = Float.parseFloat(beta);
		float alphaf = Float.parseFloat(alpha);
		
		fireOrientationEvent(Integer.parseInt(controllerId), gammaf, betaf, alphaf);
    }
    
    private void handlePassEventFromClient(Map<String, String> header, Map<String, String> parms, Map<String, String> files)
    {
    	if(SHOW_DEBUG_LOGGING)
			System.out.println("Recieved Pass Event");
    	
		firePassEvent(header, parms, files);
    }
    
    private Response serveCustom(String uri, Method method, Map<String, String> header, Map<String, String> parms, Map<String, String> files, String fileContents) 
    {
    	//return WebPageBuilder.generateWebPage("",  WebPageBuilder.readFile("testBody"));
    	
    	if(SHOW_DEBUG_LOGGING)
			System.out.println("Serving Custom Request for uri " + uri );
    	
    	try
    	{
    		if(uri.contains(".png"))
        	{
        		return ResponseUtils.handlePNGRequest(uri, fileContents);
        	}
    		else if(uri.contains(".gif"))
    		{
    			return ResponseUtils.handleGIFRequest(uri, fileContents);
    		}
    		
    		return new Response(WebPageBuilder.readFile(uri, fileContents));
    	}
    	catch (Exception e)
    	{
    		//unable to load custom
    	}
    	
    	return WebPageBuilder.generateWebPage("", "");
    }
      
    @Override
    public Response serve(String uri, Method method, Map<String, String> header, Map<String, String> parms, Map<String, String> files) 
    {
    	
    	if(SHOW_DEBUG_LOGGING)
			System.out.println("Recieved Request for uri " + uri );
    	
    	
    	//If the user is returning a custom webpage
    	for(String str : customLinks)
    	{
    		if(uri.contains(str))
    		{
    			return serveCustom(MCP.FILE_LOCATION_PREFIX + uri, method, header, parms, files, null);
    		}
    	}
    	
    	if(this.customLinkDirect != null)
    	{
    		for(CustomResource cr: this.customLinkDirect)
    		{
    			if(uri.contains(cr.fileName))
    			{
    				return serveCustom(uri, method, header, parms, files, cr.fileContents);
    			}
    		}
    	}
    	
    	if(uri.contains("heartbeat"))
    	{
    		String id = parms.get("id");
    		
    		if(this.hearbeatResponses.containsKey(id))
    		{
    			String jsonResp = this.hearbeatResponses.get(id).toJSONString();
    			//To ensure no repeats of the same message
    			this.hearbeatResponses.remove(id);
    			return new Response(jsonResp);
    		}
    		else
    		{
    			return new Response((new JSONObject()).toJSONString());
    		}
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
    	else if(uri.contains("passEvent"))
    	{
    		handlePassEventFromClient(header, parms, files);
    		
    		return WebPageBuilder.generateWebPage("", "OK");
    	}
    	
    	if(uri.contains("getIpAddress"))
    	{
    		String clientIP = header.get("remote-addr");
    		JSONObject json = new JSONObject();
    		json.put("ip", clientIP);
    		
    		return new Response(json.toJSONString());
    	}
    	
    	if(uri.contains("/images/"))
    	{    		
    		if(uri.contains(".png"))
        	{
        		return ResponseUtils.handlePNGRequest("/jQueryImages/" + uri.substring(1), null);
        	}
    		else if(uri.contains(".gif"))
    		{
    			return ResponseUtils.handleGIFRequest("/jQueryImages/" + uri.substring(1), null);
    		}
    		else
    		{
    			return WebPageBuilder.generateWebPage("", "?");
    		}
    	}
    	
    	
    	//Handle Request for Files
    	if(uri.contains(".css"))
    	{
    		if(SHOW_DEBUG_LOGGING)
    			System.out.println("Handling CSS Request " + uri );
    		return ResponseUtils.handleCSSRequest("/CSS/" + uri.substring(1));
    	}
    	else if(uri.contains(".js"))
    	{
    		if(SHOW_DEBUG_LOGGING)
    			System.out.println("Handling JS Request " + uri );
            return ResponseUtils.handleJSRequests("/Js/" + uri.substring(1));
    	}
    	else if(uri.contains(".png"))
    	{
    		if(SHOW_DEBUG_LOGGING)
    			System.out.println("Handling Image Request " + uri );
    		return ResponseUtils.handlePNGRequest("/Images/" + uri.substring(1), null);
    	}
    	else if (uri.contains("favicon"))
    	{
    		return WebPageBuilder.generateWebPage("", "");
    	}
    	
    	
    	
    	if (uri.contains("canvas"))
    	{
    		return WebPageBuilder.generateWebPage(WebPageBuilder.readFile("/Headers/canvasHeader", null), WebPageBuilder.readFile("/Bodys/canvasBody", null));
    	}
    	else if(uri.contains("keyboard"))
    	{
    		return WebPageBuilder.generateWebPage(WebPageBuilder.readFile("/Headers/keyboardHeader", null), WebPageBuilder.readFile("/Bodys/keyboardBody", null));
    	}
    	else if(uri.contains("tilt"))
    	{
    		return WebPageBuilder.generateWebPage(WebPageBuilder.readFile("/Headers/tiltHeader", null), WebPageBuilder.readFile("/Bodys/tiltBody", null));
    	}
    	else if(uri.contains("postman"))
    	{
    		return WebPageBuilder.generateWebPage(WebPageBuilder.readFile("/Headers/postmanHeader", null), WebPageBuilder.readFile("/Bodys/postmanBody", null));
    	}
    	else if (uri.contains("redirect"))
    	{
    		uniqueId ++;
    		
    		if(parms.containsKey("choice"))
    		{
    			return WebPageBuilder.generateWebPage("", WebPageBuilder.returnJSRedirect("http://" + this.getIpAddressForClients() + "/" + parms.get("choice"), uniqueId));
    		}
    		else
    		{
    			return WebPageBuilder.generateWebPage("", WebPageBuilder.returnJSRedirect("http://" + this.getIpAddressForClients() + "/canvas", uniqueId));
    		}
    	}
    	else
    	{
    		if(defaultStartPage != null && (customLinks != null || customLinkDirect != null))
    		{
    			if(customLinks != null)
    			{
    				if(customLinks.contains(defaultStartPage))
    				{
    					return serveCustom(MCP.FILE_LOCATION_PREFIX + "/" + defaultStartPage, method, header, parms, files, null);
    				}
    			}
    			
    			if(customLinkDirect != null)
    			{
    				for(CustomResource cr: this.customLinkDirect)
    	    		{
    	    			if(defaultStartPage == cr.fileName)
    	    			{
    	    				return serveCustom(uri, method, header, parms, files, cr.fileContents);
    	    			}
    	    		}
    			}
    			
    		}
    		
    		String bodyText = WebPageBuilder.readFile("/Bodys/landingPageBody", null);
    		String redirectOptionText = WebPageBuilder.convertRedirectOptionListToString(redirectOptions);
    		
    		return WebPageBuilder.generateWebPage(WebPageBuilder.readFile("/Headers/landingPageHeader", null), String.format(bodyText, redirectOptionText));
    		//return webpageBuilder.generateWebPage(webpageBuilder.readFile("Headers/defaultHeader"), webpageBuilder.readFile("Bodys/defaultBody"));
    	}
    }
    
    
}
