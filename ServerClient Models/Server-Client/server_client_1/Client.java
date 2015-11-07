package server_client_1;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Client implements Runnable {

	String hostname;
	String clientName;
	int port;
	
	public Client(String name, int port) {
		//hostname = "localhost";
		//hostname = "145.108.69.105";
		hostname = "192.168.31.102";
		
		this.port = port;
		clientName = name;
	}
	
	void start(){
		try{
			Socket connSocket = new Socket(hostname, port);
			
			BufferedReader serverInput = new BufferedReader(new InputStreamReader(connSocket.getInputStream()));
			DataOutputStream serverOutput = new DataOutputStream(connSocket.getOutputStream());
			
			serverOutput.writeBytes(clientName + "\n");
			
			String reply = serverInput.readLine();
			System.out.println("Server says: " + reply);
			
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
