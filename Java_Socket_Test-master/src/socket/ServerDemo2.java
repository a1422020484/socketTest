package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerDemo2 {

	public static void main(String[] args) throws IOException {
		ServerSocket server = new ServerSocket(8899);
		while(true){
			Socket client = server.accept();
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			String str = in.readLine();
			System.out.println(str);
			client.close();
//			server.close();
		}
	}
}
