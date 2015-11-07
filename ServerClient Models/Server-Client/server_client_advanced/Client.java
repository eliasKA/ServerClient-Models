package server_client_advanced;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable {

	String hostname;
	String clientName;
	BufferedReader serverInput;
	DataOutputStream serverOutput;
	
	int port;
	boolean exit;
	
	public Client(String name, int port) {
		//hostname = "localhost";
		hostname = "145.108.69.105";
		
		this.port = port;
		clientName = name;
		exit = false;
	}
	
	void start(){
		try{
			Socket connectionSocket = new Socket(hostname, port);
			
			serverInput = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			serverOutput = new DataOutputStream(connectionSocket.getOutputStream());
			
			serverOutput.writeBytes(clientName + "\n");
			System.out.println("Client: username sent");
			
			(new Thread(new Send())).run();
			(new Thread(new Listen())).run();
		}catch(Exception e){
			System.out.println("Client: An exception has been thrown: "  + e.getMessage());
		}
		
	}
	
	private class Listen implements Runnable{

		@Override
		public void run() {
			try {
				while(!exit){
					System.out.println("Client: waiting for message");
					String message = serverInput.readLine();
					System.out.println("Client: recieved " + message);
				}
			} catch (IOException e) {
				System.out.println("Clientside exception: " + e.getMessage());
			}
		}
		
	}

	private class Send implements Runnable{

		@Override
		public void run() {
			try{
				String message;
				Scanner in = new Scanner(System.in);
				
				while(!exit){
					System.out.println("Client: waiting for input");
					message = in.nextLine();
					if(message.equals("exit")){
						exit = true;
					}
					serverOutput.writeBytes(message + "\n");
				}
			}catch(Exception e){
				System.out.println("Clientside exception: " + e.getMessage());
			}
		}
		
	}
	
	@Override
	public void run() {
		System.out.println("Client-side running");
		start();
	}
}
