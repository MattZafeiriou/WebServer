package com.MattZafeiriou.WebServer;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;

public class FilesManager
{

	private Thread thread;
	private Socket socket;

	public FilesManager( Socket socket ) throws IOException
	{
		thread = new Thread();
		this.socket = socket;
		try
		{
			thread.join();
		} catch( InterruptedException e )
		{
			e.printStackTrace();
		}
		work();
	}

	public void work() throws IOException
	{
		String request = Server.readRequests( socket, true );
		if( ! request.equals( "" ) ) // check if there is a request
		{
			System.out.println( request ); // print the name of the file client requested
			/*
			 * =========================== SEND DATA ==========================
			 */
			if( request.equals( "/" ) )
				request = "oof.html"; // this is just the index file in the folder.
			String MIME_type = Files.probeContentType( new File( request.replaceFirst( "/", "" ) ).toPath() );
			Server.sendFileData( Server.readFile( request.replaceFirst( "/", "" ) ), socket,
					"Content-Type: " + MIME_type );
		}
	}

}
