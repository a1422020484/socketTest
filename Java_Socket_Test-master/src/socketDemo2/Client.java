package socketDemo2;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;

public class Client {

	public static String END_MARK = "\n";

	public static void main(String[] args) throws Exception {

		String host = "127.0.0.1";
		int port = 8899;

		Socket client = new Socket(host, port);

		Writer writer = new OutputStreamWriter(client.getOutputStream());
		writer.append("Hello server");
		writer.append(END_MARK);
		writer.flush();
		
		Reader reader = new InputStreamReader(client.getInputStream());
		char chars[] = new char[64];
		int len;
		StringBuilder sb = new StringBuilder();
		String temp;

		int idx = -1;

		while ((len = reader.read(chars)) != -1) {
			temp = new String(chars, 0, len);
			idx = temp.indexOf(END_MARK);
			if (idx != -1) {
				sb.append(temp, 0, idx);
				break;
			}
			sb.append(temp);
		}
		
		System.out.println("from server : " + sb);
		
		writer.close();
		reader.close();
//		client.close();
	}

}
