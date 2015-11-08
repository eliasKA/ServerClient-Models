package server_client_chat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

public class Server implements Runnable {
	int port;
	boolean exit;
	Hashtable<String, ClientCommunicator> userSockets;
	Hashtable<ClientCommunicator, String> socketUsers;

	public Server(int port) {
		exit = false;
		userSockets = new Hashtable<String, ClientCommunicator>();
		socketUsers = new Hashtable<ClientCommunicator,String>();
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
			printLine("Server exception: " + e.getMessage());
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
				
				ClientCommunicator recieverIO; 
				ClientCommunicator clientIO = new ClientCommunicator(clientSocket);
				
				username = clientIO.readLine();
				clientIO.writeLine(line("Username accepted!"));
				
				userSockets.put(username, clientIO );
				socketUsers.put(clientIO, username);
				
				while (true) {
					message = clientIO.readLine();
					
					if(message.equals("exit")){
						clientIO.writeLine(line("Server exit"));
						break;
					}
					
					reciever = message.substring(0, message.indexOf(' '));
					message = message.substring(message.indexOf(' ')+1);
					
					recieverIO = userSockets.get(reciever);
					recieverIO.writeLine(line(socketUsers.get(clientIO) + "\t| " + message));
				}

				clientIO.closeConnection();
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

	private class ClientCommunicator{
		private Socket theSocket;
		private BufferedReader inComing;
		private DataOutputStream outGoing;
		
		public ClientCommunicator(Socket theSocket) {
			try{
				this.theSocket =  theSocket;
				inComing = new BufferedReader(new InputStreamReader(theSocket.getInputStream()));
				outGoing = new DataOutputStream(theSocket.getOutputStream());
			}catch(Exception e){
				printLine("Client construction exception: " + e.getMessage());
			}
		}
		
		public String readLine() throws IOException{
			return inComing.readLine();
		}
		
		public void writeLine(String line) throws IOException{
			outGoing.writeBytes(line);
		}
		
		public void closeConnection() throws IOException{
			inComing.close();
			outGoing.close();
			theSocket.close();
		}
	}
	
	@Override
	public void run() {
		printLine("Server running");
		start();
	}

	
	
}
