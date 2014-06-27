package com.ctfo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.log4j.Logger;

/**
 * Socket工具类
 * 
 * @author James 2013-5-8 17:07:16
 */
public class SocketUtil {
	private static Logger logger = Logger.getLogger(SocketUtil.class);

	public SocketUtil() {
	}

	public static Socket createSocket(String ip, int port, int reConnectTime) {
		Socket socket = null;
		do {
			try {
				socket = new Socket(ip, port);
				socket.setReceiveBufferSize(10 * 1024 * 1024);
				logger.info(ip + ":" + String.valueOf(port) + "网络连接成功！");
			} catch (Exception e) {
				logger.error(ip + ":" + String.valueOf(port) + "连接失败", e);
				closeSocket(socket);
				try {
					Thread.sleep(reConnectTime);
				} catch (Exception e2) {
					logger.error("SocketUtil.createSocket(String ip, int port, int reConnectTime):Thread.sleep(reConnectTime);", e);
				}
			}
		} while (socket == null);
		return socket;
	}

	public static BufferedReader createReader(Socket socket) {
		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "GBK"));
			return in;
		} catch (IOException e) {
			logger.error("SocketUtil.createReader(Socket socket)", e);
			return null;
		}

	}

	public static PrintWriter createWriter(Socket socket) {

		PrintWriter out;
		try {
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "GBK"));
			return out;
		} catch (IOException e) {
			logger.error("SocketUtil.createWriter(Socket socket) ", e);
			return null;

		}

	}

	public static void closeSocket(Socket socket) {
		try {
			if (socket != null)
				socket.close();
		} catch (IOException e) {
			logger.error(e);
		}

	}

	public static void closeReader(BufferedReader in) {
		try {
			if (in != null) {
				in.close();
				in = null;
			}
		} catch (IOException e) {
			logger.error("SocketUtil.closeReader(BufferedReader in) ", e);
		}

	}

	public static void closeWriter(PrintWriter out) {
		try {
			if (out != null) {
				out.close();
				out = null;
			}
		} catch (Exception e) {
			logger.error("SocketUtil.closeWriter(PrintWriter out) ", e);
		}
	}

}
