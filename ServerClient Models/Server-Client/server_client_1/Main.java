package server_client_1;

public class Main {
	
	static final int PORT = 9999;
	
	void start() {
		//Server server = new Server(PORT);
		Client client = new Client("Elias",PORT);

		//Thread serverThread = new Thread(server);
		Thread clientThread = new Thread(client);
		
		clientThread.start();
		//serverThread.start();
	}

	public static void main(String[] args) {
		new Main().start();
	}

}

