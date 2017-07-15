package socketDemo2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	public static String END_MARK = "\n";
	public static void main(String[] args) throws IOException {
		ServerSocket server = new ServerSocket(8899);
		while(true){
			Socket client = server.accept();
			
			String tempString;
			BufferedReader in =new BufferedReader(new InputStreamReader(client.getInputStream()));
			
			PrintWriter os = new PrintWriter(client.getOutputStream());
			
			tempString = in.readLine();
			
			System.out.println("Client : " + tempString);
			
			if(tempString.length() > 0){
				Writer writer = new OutputStreamWriter(client.getOutputStream());
				writer.append("Hello client ");
				writer.append(END_MARK);
				writer.flush();
			}
			
			os.close();
			in.close();
//			client.close();
//			server.close();
		}
	}
}
