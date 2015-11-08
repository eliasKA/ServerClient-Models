package server_client_chat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

public class Server implements Runnable {
	int port;
	boolean exit;
	Hashtable<String, Socket> userSockets;

	public Server(int port) {
		exit = false;
		userSockets = new Hashtable<String, Socket>();
		this.port = port;
	}

	private void printLine(String line) {
		System.out.println(line);
	}

	private String line(String s) {
		return (s + "\n");
	}

	private void start() {
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			Socket client;
			UserThread newUser;
			Thread userThread;
			
			while (!exit) {
				client = serverSocket.accept();
				
				newUser = new UserThread(client);
				userThread = new Thread(newUser);
				
				userThread.start();
			}

			serverSocket.close();
		} catch (Exception e) {
			printLine("Server exception: " + e.getMessage() + " " + e.getStackTrace().toString());
		}
	}

	private class UserThread implements Runnable {
		private Socket clientSocket;

		public UserThread(Socket client) {
			clientSocket = client;
		}

		void start() {
			try {
				String username, message, reciever;
				DataOutputStream toReciever;
				
				BufferedReader fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				DataOutputStream toClient = new DataOutputStream(clientSocket.getOutputStream());
				
				username = fromClient.readLine();
				userSockets.put(username, clientSocket);
				toClient.writeBytes(line("Username accepted!"));
				
				while (true) {
					message = fromClient.readLine();
					
					if(message.equals("exit")){
						exit = true;
						toClient.writeBytes(line("exit"));
						
						break;
					}
					
					reciever = message.substring(0, message.indexOf(' '));
					message = message.substring(message.indexOf(' ')+1);
					
					toReciever = new DataOutputStream(userSockets.get(reciever).getOutputStream());
					toReciever.writeBytes(line(message));
					toReciever.close();
				}

				fromClient.close();
				clientSocket.close();
			} catch (Exception e) {
				printLine("Server exception: " + e.getMessage());
			}
		}

		@Override
		public void run() {
			printLine("Server: a new client has connected");
			start();
		}

	}

	@Override
	public void run() {
		printLine("Server running");
		start();
	}

}
