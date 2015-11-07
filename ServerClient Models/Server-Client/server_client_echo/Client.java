package server_client_echo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable {

	String hostname;
	String clientName;
	Scanner in;
	int port;
	
	public Client(String name, int port) {
		//hostname = "localhost";
		hostname = "145.108.69.105";
		
		in = new Scanner(System.in);
		this.port = port;
		clientName = name;
	}
	
	void start(){
		try{
			Socket connSocket = new Socket(hostname, port);
			
			BufferedReader serverInput = new BufferedReader(new InputStreamReader(connSocket.getInputStream()));
			DataOutputStream serverOutput = new DataOutputStream(connSocket.getOutputStream());
			
			String message;
			boolean exit = false;
			while(!exit){
				message = in.nextLine();
				serverOutput.writeBytes(message + "\n");
				
				if(message.equals("exit")){
					exit = true;
				}
				
				System.out.println("response: " + serverInput.readLine());
			}
			
			serverOutput.close();
			serverInput.close();
			connSocket.close();
		}catch(Exception e){
			System.out.println("Client: An exception has been thrown: "  + e.getMessage());
		}
		
	}

	@Override
	public void run() {
		System.out.println("Client-side running");
		start();
	}
}
