package com.MattZafeiriou.WebServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server
{
	@SuppressWarnings( "resource" )
	public static void main( String[] args )
	{
		int port = 8080;
		ServerSocket server = null;

		try
		{
			server = new ServerSocket( port );
		} catch( IOException e1 )
		{
			e1.printStackTrace();
		}
		server.setPerformancePreferences( 10000, 500, 100000 );
		System.out.println( "Server is ready" );
		while( true )
		{
			Socket clientSocket = null;
			try
			{
				clientSocket = server.accept();
				new FilesManager( clientSocket );
			} catch( IOException e )
			{
				e.printStackTrace();
			} finally
			{
				if( clientSocket != null )
					try
					{
						clientSocket.close();
					} catch( IOException e )
					{
						e.printStackTrace();
					}
			}

		}

	}

	public static void sendFileData( String data, Socket socket, String headers ) throws IOException
	{
		if( headers.equals( "" ) ) // if there are no server headers
		{
			String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + data;
			socket.getOutputStream().write( httpResponse.getBytes() );
		} else // there are at least one server header
		{
			String httpResponse = "HTTP/1.1 200 OK\r\n" + headers + "\r\n" + "\r\n" + data;
			socket.getOutputStream().write( httpResponse.getBytes() );
		}
	}

	/*
	 * ShowLines is like the debug mode
	 */
	public static String readRequests( Socket socket, boolean showLines ) throws IOException
	{
		String get = "";
		InputStreamReader isr = new InputStreamReader( socket.getInputStream() );

		BufferedReader reader = new BufferedReader( isr );

		boolean ready = reader.ready();
		if( ready )
		{
			/*
			 * =========================== READ DATA ==========================
			 */
			String line = reader.readLine();
			while( ! line.isEmpty() )
			{
				if( showLines )
					System.out.println( line );
				String[] elements = line.split( " " );
				if( elements[ 0 ].equals( "GET" ) )
				{
					get = elements[ 1 ];
				}
				line = reader.readLine();
			}
		}
		return get;
	}

	/*
	 * Reads only text files. I may make it for images l8r
	 */
	public static String readFile( String path )
	{
		String file = "";
		try
		{
			File myObj = new File( path );
			Scanner myReader = new Scanner( myObj );
			while( myReader.hasNextLine() )
			{
				String data = myReader.nextLine();
				file += data;
			}
			myReader.close();
		} catch( FileNotFoundException e )
		{
			System.err.println( "An error occurred." );
			e.printStackTrace();
		}
		return file;
	}

}
