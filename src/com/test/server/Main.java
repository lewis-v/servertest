package com.test.server;


import
java.io.IOException;



import
java.net.ServerSocket;

import
java.net.Socket;
import java.util.ArrayList;
import java.util.List;



public class Main {
	static List<ServerThread> threads = new ArrayList<ServerThread>();
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		try
		{
			
			@SuppressWarnings("resource")
			ServerSocket ss =new ServerSocket(47423);


			while (true){
				Socket socket =ss.accept();
				ServerThread thread = new ServerThread(socket);
				threads.add(thread);
				thread.start();
			}
		} 
		catch
		(IOException e) {
			System.out.println(e.getMessage());
		}
		for(ServerThread t : threads) {
			if(t != null) {
				t.interrupted();
			}
		}
	}
}
