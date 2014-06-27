package com.ctfo.queue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.log4j.Logger;

import com.ctfo.statement.SystemStatement;

/**
 * 队列集合，为多个存储服务提供数据
 * 
 * @author James 2014-3-15 1:06:25
 */
public enum ServiceQueue {

	// 枚举方式单例模式
	INSTANCE;

	private Logger logger = Logger.getLogger(ServiceQueue.class);

	/** 统一管理每个队列 */
	private Map<String, ArrayBlockingQueue<String>> mapQueue = new HashMap<String, ArrayBlockingQueue<String>>();
	/** 维护每个队列的名称 */
	private List<String> listQueueName = new ArrayList<String>();

	/** 返回队列个数 */
	public int getQueueListSize() {
		return listQueueName.size();
	}

	/** 添加新队列 */
	public void addQueue(String queueId) {
		ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(SystemStatement.QUEUE_SIZE);
		mapQueue.put(queueId, queue);
		listQueueName.add(queueId);
	}

	/** 移除队列 */
	public void removeQueue(String queueId) {
		listQueueName.remove(queueId);
		mapQueue.remove(queueId);
	}

	/** 获取队列名称 */
	public String getQueueName(int index) {
		return listQueueName.get(index);
	}

	/**
	 * 添加队列数据
	 * 
	 * @param queueId
	 *            队列ID
	 * @param messageStr
	 *            内部协议字符串
	 */
	public void addQueueByData(String queueId, String messageStr) {
		try {
			boolean boo = mapQueue.get(queueId).offer(messageStr);
			if (!boo) {
				logger.warn("SaveCenter Receive very slow !! QueueNumber:" + queueId);
			}
		} catch (Exception e) {
			logger.error("addQueueByData ERROR listQueue subscript:" + queueId + mapQueue.get(queueId).size(), e);
		}
	}

	/**
	 * 取出队列数据
	 * 
	 * @param queueId
	 *            队列ID
	 * @return 内部协议字符串
	 */
	public String getQueueByData(String queueId) {
		try {
			String messageStr = mapQueue.get(queueId).poll();// 非阻塞队列
			if (messageStr == null) {
				// logger.info("SaveCenter to receive quickly!!");
				messageStr = mapQueue.get(queueId).take();// 阻塞队列
			}
			return messageStr;
		} catch (Exception e) {
			logger.error("getQueueByData ERROR listQueue subscript:" + queueId + mapQueue.get(queueId).size(), e);
			return null;
		}
	}

	/**
	 * 返回队列当前状态清单
	 * 
	 * @return
	 */
	public String getQueueStatus() {
		StringBuffer sb = new StringBuffer();
		sb.append("==============================================================================\r\n");
		sb.append("CLIENT COUNT[" + mapQueue.size() + "] \r\n");
		Set<Map.Entry<String, ArrayBlockingQueue<String>>> set = mapQueue.entrySet();
		for (Iterator<Map.Entry<String, ArrayBlockingQueue<String>>> it = set.iterator(); it.hasNext();) {
			Map.Entry<String, ArrayBlockingQueue<String>> entry = (Map.Entry<String, ArrayBlockingQueue<String>>) it.next();
			sb.append("\t CLIENT NUM:[" + entry.getKey() + "],QUEUE SIZE:" + SystemStatement.QUEUE_SIZE + ",USE:" + entry.getValue().size() + "\r\n");
		}
		sb.append("==============================================================================\r\n");
		return sb.toString();
	}

	public static void main(String[] args) {
		ServiceQueue serviceQueue = ServiceQueue.INSTANCE;
		serviceQueue.addQueue("192.168.101.49");
		serviceQueue.addQueue("192.168.101.50");
		serviceQueue.addQueueByData("192.168.101.49", "TTTT");
		System.out.println(serviceQueue.getQueueStatus());
		List<String> list01 = new ArrayList<String>();
		list01.add("AAA");
		list01.add("BBB");
		list01.add("CCC");
		list01.remove(new String("BBB"));
		for (String string : list01) {
			System.out.println(string);
		}

	}
}
