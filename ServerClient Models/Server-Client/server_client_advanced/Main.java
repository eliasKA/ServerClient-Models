package server_client_advanced;

public class Main {
	
	static final int PORT = 20019;
	
	void start() {
		
		Server server = new Server(PORT);
		Client client = new Client("a",PORT);
		
		Thread serverThread = new Thread(server);
		Thread clientThread = new Thread(client);
		
		
		serverThread.start();
		clientThread.start();
	}

	public static void main(String[] args) {
		new Main().start();
	}

}

