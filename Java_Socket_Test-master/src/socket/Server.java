package socket;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Socket服务端简单示例代码
public class Server {

	// 结束标记
	public static String END_MARK = "\r\n";
	// 线程池
	private static ExecutorService threadPool = null;

	static {
		// 线程池
		threadPool = Executors.newCachedThreadPool();
	}

	public static void main(String args[]) throws IOException {
		int port = 8899;
		// 创建一个ServerSocket并监听8899端口
		ServerSocket server = new ServerSocket(port);
		boolean flag = true;
		while (flag) {
			// 等待客户端的连接，accept方法是阻塞的
			final Socket socket = server.accept();

			// 在子线程中处理socket的请求，然后继续等待下一个客户端的连接
			threadPool.submit(new Runnable() {
				@Override
				public void run() {
					try {
						/**************************************************************************************/
						/*
						 * 这里存在一个问题：客户端断开连接后，但调用socket.isCloseed()返回是还是false。
						 * 具体原因还不清楚。 测试环境：系统（windows7）， 服务端开发工具（Eclipse），
						 * 客户端开发工具（vs2012）， 服务端实现（java）
						 * 客户端实现（c++，Socket实现为winsock）
						 */
						/**************************************************************************************/
						// 如果Socket未关闭&&可读&&可写
						while (!socket.isClosed() && !socket.isInputShutdown() && !socket.isOutputShutdown()) {

							// 连接建立后，从客户端读取数据
							Reader reader = new InputStreamReader(socket.getInputStream());
							char chars[] = new char[64];
							int len;
							StringBuilder sb = new StringBuilder();
							String temp;
							int idx = -1;

							// 读数据
							while ((len = reader.read(chars)) != -1) {
								temp = new String(chars, 0, len);
								idx = temp.indexOf(END_MARK); // 如果遇到结束标记则跳出循环
								if (idx != -1) {
									sb.append(temp, 0, idx); // 添加结束标记前的字符串
									break;
								}
								sb.append(temp);
								break;
							}
							System.out.println("from client: " + sb);

							if (sb.length() > 0) {
								// 往客户端写数据
								Writer writer = new OutputStreamWriter(socket.getOutputStream());
								// 写数据
								writer.append("Hello Client");
								writer.append(END_MARK); // 发送结束标记
								// 数据是存放在缓冲区，默认只有当缓冲区满时或在close时，数据才会真正发送出去（出于性能考虑），调用flush方法可以立刻把数据发送出去
								writer.flush();// 调用flush方法把缓冲区的数据写出去
							} else {
								// 如果没有接收到客户端指令则跳出循环
								break;
							}
						}
						socket.close();
					} catch (Exception e) {
						// 这里为了方便，只是简单的把错误信息打印到控制台
						e.printStackTrace();
					}
				}
			});
		}
		server.close();
	}
}