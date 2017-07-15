package socket;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;

// Socket客户端简单示例代码
public class Client {

	// 结束标记
	public static String END_MARK = "\n";

	public static void main(String args[]) throws Exception {

		String host = "127.0.0.1"; // 服务端ip地址
		int port = 8899; // 服务端的端口号
		// 创建Socket与服务端建立连接
		Socket client = new Socket(host, port);

		// 建立连接后就可以往服务端写数据了
		Writer writer = new OutputStreamWriter(client.getOutputStream());
		writer.append("Hello Server");
		writer.append(END_MARK); // 结束标记
		writer.flush();// 调用flush方法把缓冲区中的数据写出去

		// 从服务端读数据
		Reader reader = new InputStreamReader(client.getInputStream());
		char chars[] = new char[64];
		int len;
		StringBuilder sb = new StringBuilder();
		String temp;

		int idx = -1;
		while ((len = reader.read(chars)) != -1) {
			temp = new String(chars, 0, len);
			idx = temp.indexOf(END_MARK); // 如果遇到结束标记则跳出循环
			if (idx != -1) {
				sb.append(temp, 0, idx); // 添加结束标记前的字符串
				break;
			}
			sb.append(temp);
		}
		System.out.println("from server: " + sb);

		writer.close();
		reader.close();
		client.close();
	}
}