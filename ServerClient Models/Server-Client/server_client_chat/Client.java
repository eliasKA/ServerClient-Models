package server_client_chat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable {

	private String hostname;
	private int port;
	private boolean exit;
	Socket connectionSocket;
	
	public Client(String hostname, int port) {
		exit = false;
		this.port = port;
		this.hostname = hostname;
	}
	
	private void printLine(String line) {
		System.out.println(line);
	}

	private String line(String s) {
		return (s + "\n");
	}
	
	void start(){
		try {
			connectionSocket = new Socket(hostname, port);
			
			Send send = new Send(connectionSocket);
			Listen listen = new Listen(connectionSocket);
			
			Thread sendingThread = new Thread(send);
			Thread listeningThread = new Thread(listen);
			
			sendingThread.start();
			listeningThread.start();
			
			while(!exit){}
			Thread.sleep(1000);
			
			connectionSocket.close();
		} catch (Exception e) {
			printLine("Client exception: " + e.getMessage());
		}
		
	}
	
	private class Listen implements Runnable{
		private Socket listeningSocket;
		
		public Listen(Socket listeningSocket) {
			this.listeningSocket = listeningSocket;
		}
		
		private void startListening(){
			try {
				BufferedReader fromServer = new BufferedReader(new InputStreamReader(listeningSocket.getInputStream()));		
				String message;
				
				while(true){
					message = fromServer.readLine();
					
					if(message.equals("Server exit")){
						break;
					}
					
					printLine(message);
				}
				
				fromServer.close();
			} catch (Exception e) {
				printLine("Client listening exception: " + e.getMessage() + e.getLocalizedMessage());
			}
		
		}
		
		@Override
		public void run() {
			printLine("Client listening");
			startListening();
		}
		
	}

	private class Send implements Runnable{
		private Socket sendingSocket;
		Scanner in;
		
		public Send(Socket sendingSocket) {
			this.sendingSocket = sendingSocket;
			in = new Scanner(System.in);
		}
		
		private void startSending(){
			try {
				DataOutputStream toServer = new DataOutputStream(sendingSocket.getOutputStream());
				String message;
				
				while(true){
					message = in.nextLine();
					
					toServer.writeBytes(line(message));
				}
				
			} catch (Exception e) {
				printLine("Client sending exception: " + e.getMessage());
			}
			
		}
		
		@Override
		public void run() {
			printLine("Client sending");
			startSending();
		}
		
	}

	@Override
	public void run() {
		printLine("Client running");
		start();
	}
}
