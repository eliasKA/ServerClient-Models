package server_client_echo;

import java.util.Scanner;

public class Main {

	static final int PORT = 20010;

	void start() {
		Server server = new Server(PORT);
		Client a = new Client("a",PORT);
		
		Thread serverThread = new Thread(server);
		Thread thrA = new Thread(a);
		
		thrA.start();
		serverThread.start();
		
	}

	public static void main(String[] args) {
		new Main().start();
	}

}

