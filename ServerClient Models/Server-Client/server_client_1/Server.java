package server_client_1;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server implements Runnable{
	int port;
	
	Server(int port){
		this.port = port;
	}
	
	void start(){
		try{
			ServerSocket serverSocket = new ServerSocket(port);
			Socket connSocket = serverSocket.accept();
			
			BufferedReader clientInput = new BufferedReader(new InputStreamReader(connSocket.getInputStream()));
			DataOutputStream clientOutput = new DataOutputStream(connSocket.getOutputStream());
		
			String clientName = clientInput.readLine();			
			String reply = "Hello " + clientName + "\n";
			
			clientOutput.writeBytes(reply);
			
			clientOutput.close();
			clientInput.close();
			connSocket.close();
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
}

