package server_client_advanced;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Hashtable;
import java.util.Scanner;

public class Server implements Runnable{
	int port;
	boolean exit;
	Hashtable<String, Socket> userMap;
	
	Server(int port){
		exit = false;
		this.port = port;
		userMap = new Hashtable<String, Socket>();
	}
	
	void start(){
		try{
			ServerSocket serverSocket = new ServerSocket(port);
			
			while(!exit){
				(new userThread(serverSocket.accept())).run();
			}
			
			serverSocket.close();
		}catch(Exception e){
			System.out.println("Server: An exception has been thrown: " + e.getMessage());
		}
	}

	@Override
	public void run() {
		System.out.println("Server running");
		start();
	}
	
	private class userThread implements Runnable{
		private Socket userSocket;
		
		public userThread(Socket userSocket) {
			this.userSocket = userSocket;
		}
		
		@Override
		public void run() {
			try{
				BufferedReader clientInput = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));
				String username = clientInput.readLine();
				System.out.println("Server: username recieved: " + username);
				userMap.put(username, userSocket);
				
				while(!exit){
					String recievedMessage = clientInput.readLine();
					System.out.println("Server: message recieved: " + recievedMessage);
					
					if(recievedMessage.equals("exit")){
						exit = true;
					}else{
						Scanner messageScanner = new Scanner(recievedMessage);
						String toUser = messageScanner.next();
						DataOutputStream clientOutput = new DataOutputStream(userMap.get(toUser).getOutputStream());
						clientOutput.writeBytes(messageScanner.next());
						System.out.println("Server: message sent to: " + toUser);
					}
				}
				
				clientInput.close();
				userSocket.close();
			}catch(Exception e){
				System.out.println("Serverside exception: " + e.getMessage());
			}
		}
		
	}
}

