package com.ctfo.main;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.ctfo.statement.SystemStatement;
import com.ctfo.util.SocketUtil;

/**
 * 模拟器服务，客户端类
 * 
 * @author James 2014-3-14 15:21:57
 */
public class SaveCenterClient {

	private static Logger logger = Logger.getLogger(SaveCenterClient.class);

	public Socket socket;
	public PrintWriter out;
	public BufferedReader in;

	public static void main(String[] args) {
		SaveCenterClient status = new SaveCenterClient();
		status.reveice();
	}
	
	public void reveice(){
		long a = 0l;
		try {
			connect();
			wirteData("NOOP\r\n");
			while (true) {
				String str = in.readLine();
				a++;
				System.out.println(str);
//				Thread.sleep(50l);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Client reveice count:"+a);
		System.exit(0);
	}

	/**
	 * 建立Socket连接
	 */
	private void connect() throws Exception {
		socket = new Socket("127.0.0.1", SystemStatement.LOCALHOST_PORT);
		socket.setReceiveBufferSize(10 * 1024 * 1024);
		in = SocketUtil.createReader(socket);
		out = SocketUtil.createWriter(socket);
	}

	/**
	 * 发送数据
	 */
	private void wirteData(String data) {
		out.write(data);
		out.flush();
	}

}
