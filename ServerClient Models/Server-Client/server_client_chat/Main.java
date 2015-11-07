package server_client_chat;

public class Main {

	static final int PORT = 20019;
	static final String HOSTNAME =  "192.168.31.102"; 
	//alternate = "localhost";
	//ipv6 = "2001:0:5ef5:79fb:405:6b6:a9a2:8d19"
	
	public Main() {
		
	}
	
	void start() {
		Server server = new Server(PORT);
		//Client client = new Client(HOSTNAME,PORT);
		
		Thread serverThread = new Thread(server);
		//Thread clientThread = new Thread(client);
		
		serverThread.start();
		//clientThread.start();
	}

	public static void main(String[] args) {
		new Main().start();
	}

}
