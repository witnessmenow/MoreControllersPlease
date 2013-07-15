package com.ladinc.mcp.socketServer;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class MCPSocketServer {

	private ServerSocket listener;
	
	private static int MAX_CONNECTIONS=0;
	
	public MCPSocketServer()
	{
		try 
		{
			listener = new ServerSocket(0);
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Listen for incoming connections and handle them
	public void StartServer() 
	{
		int i=0;
	
		try
		{
			Socket server;
			
			while((i++ < MAX_CONNECTIONS) || (MAX_CONNECTIONS == 0))
			{
				doComms connection;
				
				server = listener.accept();
				doComms conn_c= new doComms(server);
				Thread t = new Thread(conn_c);
				t.start();
			}
		} 
		catch (IOException ioe) 
		{
			System.out.println("IOException on socket listen: " + ioe);
			ioe.printStackTrace();
		}
	}
	  
	  public int getListeningPort()
	  {
		  if(listener != null)
		  {
			  return listener.getLocalPort();
		  }
		  else
		  {
			  return -1;
		  }
	  }
	  
	  public void StopServer()
		{
			try {
				listener.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	class doComms implements Runnable {
	    private Socket server;
	    private String line,input;

	    doComms(Socket server) {
	      this.server=server;
	    }

	    public void run () {

	      input="";

	      try {
	        // Get input from the client
	    	  Scanner sc = new Scanner(server.getInputStream());
	    	  //DataInputStream in = new DataInputStream (server.getInputStream());
	       // PrintStream out = new PrintStream(server.getOutputStream());
	    	  line = sc.nextLine();
	        while(input != null) 
	        {
	          input=input + line;
	          System.out.println("Overall message is:" + input);
	          line = sc.nextLine();
	        }

	        // Now write to the client

	        System.out.println("Overall message is:" + input);
	        //out.println("Overall message is:" + input);

	        server.close();
	      } catch (IOException ioe) {
	        System.out.println("IOException on socket listen: " + ioe);
	        ioe.printStackTrace();
	      }
	    }
	}

