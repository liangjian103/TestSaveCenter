package com.ctfo.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.ctfo.manage.Scheduler;
import com.ctfo.queue.ServiceQueue;
import com.ctfo.statement.SystemStatement;
import com.ctfo.util.SocketUtil;

/**
 * 模拟器服务，启动类
 * 
 * @author James 2014-3-15 1:06:39
 */
public class TestSaveCenterMain {

	private static Logger logger = Logger.getLogger(TestSaveCenterMain.class);
	private static Logger sendData = Logger.getLogger("sendData");

	public static void main(String[] args) {
		TestSaveCenterMain server = new TestSaveCenterMain();
		TestSaveCenterManage serverManage = new TestSaveCenterManage();
		Scheduler scheduler = new Scheduler();
		// 1、加载DB数据到内存
		scheduler.loadDB();
		// 2、监听-服务、为客户端开辟队列
		server.serverListener();
		// 3、监听-服务,管理该服务的启动、停止、查看状态
		serverManage.serverListener();
		System.out.println("SERVER START OK! ^_^");
		// 3、准备启动
		server.ready();
		// 4、启动调度器,将数据拼装并插入到指定队列
		scheduler.schedulerByData();
	}

	private void ready() {
		try {
			SystemStatement.SERVER_RUN.take();
		} catch (InterruptedException e) {
			logger.error("SystemStatement.SERVER_RUN.take() ERROR!", e);
		}
	}

	// 监听客户端连接
	private void serverListener() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				// 监听管理端口
				ServerSocket listener = null;
				// 打开管理端口
				try {
					listener = new ServerSocket(SystemStatement.LOCALHOST_PORT);
				} catch (Exception e) {
					logger.error("打开数据推送端口时错误" + e.getMessage(), e);
					System.out.println("不能启动模拟器服务，服务可能已经启动了");
					System.exit(0);
					return;
				}
				while (true) {
					try {
						Socket sock = listener.accept();
						sock.setSoTimeout(1000 * 60 * 60 * 6);
						String threadId = sock.getInetAddress().getHostAddress() + ":" + sock.getPort();
						SerSocket serSocket = new SerSocket(threadId, sock);
						ServiceQueue.INSTANCE.addQueue(threadId);// 开辟队列
						serSocket.wirteData("LACK 0 0 0 106101");//存储服务需要一个MSGID
						serSocket.start();
					} catch (Exception e) {
						logger.error("serverListener ERROR!", e);
						e.printStackTrace();
					}
				}
			}
		};
		thread.start();
		logger.info("TestSaveCenterMain serverListener OK! ServerPORT:" + SystemStatement.LOCALHOST_PORT);
	}

	// 通信线程内部类
	private static class SerSocket extends Thread {
		public String threadId;
		public Socket socket;
		public PrintWriter out;
		public BufferedReader in;

		public SerSocket(String threadId, Socket socket) {
			this.threadId = threadId;
			this.socket = socket;
			out = SocketUtil.createWriter(socket);
			in = SocketUtil.createReader(socket);
		}

		public void run() {
			try {
				// 发数据
				new Thread() {
					@Override
					public void run() {
						while (true) {
							String data = ServiceQueue.INSTANCE.getQueueByData(threadId);
							sendData.info("threadId:" + threadId + " " + data);
							wirteData(data);
						}
					}
				}.start();
				// 接数据
				int count = 0;
				while (true) {
					count++;
					String s = in.readLine();
					if (s == null) {
						if (count >= 3) {
							if (SystemStatement.SYS_DYNAMIC) {
								// 移除该存储队列
								ServiceQueue.INSTANCE.removeQueue(threadId);
							}
							break;
						}
					}
				}
			} catch (Exception e) {
				logger.error("连接异常！threadId:"+threadId, e);
				// 移除该存储队列
				ServiceQueue.INSTANCE.removeQueue(threadId);
			} finally {
				try {
					socket.close();
					out.close();
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void wirteData(String data) {
			try {
				out.write(data + "\r\n");
				out.flush();
			} catch (Exception e) {
				logger.error("out.flush() ERROR!", e);
			}
		}
	}

}
