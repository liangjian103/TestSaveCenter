package com.ctfo.util.parallel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

/**
 * 并行计算 请运行当前类中的Main方法来学习该类的用法及作用。
 * 
 * @param ListType
 *            需并行处理的List泛型
 * @param ParameterType
 *            业务处理时传入的参数类型
 * @author James 2014-5-15 17:46:28
 */
public class ParallelComputing<ListType, ParameterType> {

	private Logger logger = Logger.getLogger(ParallelComputing.class);

	private ExecutorService pool;

	/** 业务名称 */
	private String serviceName = "DefaultService";

	/** 默认构造,默认的ServiceName,默认使用 newCachedThreadPool连接池 */
	public ParallelComputing() {
		pool = newCachedThreadPool(serviceName);
	}

	/** 默认指定serviceName,使用newCachedThreadPool连接池来管理 */
	public ParallelComputing(String serviceName) {
		this.serviceName = serviceName;
		pool = newCachedThreadPool(serviceName);
	}

	/** 指定serviceName,newFixedThreadPool线程池池 ，指定固定线程池数量 */
	public ParallelComputing(String serviceName, int nThreads) {
		this.serviceName = serviceName;
		pool = newFixedThreadPool(nThreads, serviceName);
	}

	/** 创建一个可重用固定线程数的线程池 */
	private ExecutorService newFixedThreadPool(int num, final String serviceName) {
		ExecutorService pool = Executors.newFixedThreadPool(num, new ThreadFactory() {
			AtomicInteger a = new AtomicInteger();// 原子记数

			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setName(serviceName + "-" + a.getAndIncrement());
				return t;
			}
		});
		return pool;
	}

	/** 创建一个可重用并在60秒内自动伸缩的线程池 */
	private ExecutorService newCachedThreadPool(final String serviceName) {
		ExecutorService pool = Executors.newCachedThreadPool(new ThreadFactory() {
			AtomicInteger a = new AtomicInteger();// 原子记数

			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setName(serviceName + "-" + a.getAndIncrement());
				return t;
			}
		});
		return pool;
	}

	/** 关闭线程池，释放资源 */
	public void colse() {
		pool.shutdownNow();
		// 关闭线程池
		pool.shutdown();
	}

	/**
	 * 并行执行的任务-按数据长度切分任务数
	 * 
	 * @param list
	 *            需并行处理的数据
	 * @param process
	 *            业务实现类
	 * @param dataShardSize
	 *            每个线程处理条数,小于2000按2000处理
	 */
	public boolean processForDataShard(List<ListType> list, ParallelComputingProcess<ListType, ParameterType> process, int dataShardSize, ParameterType obj) {
		if (list == null || list.size() < 1 || process == null) {
			return false;
		}
		int minShardSize = 2000;// 每个线程最小处理条数
		dataShardSize = dataShardSize < minShardSize ? minShardSize : dataShardSize;

		long startTime = System.currentTimeMillis();
		// 将list分块后丢给多个线程，并行处理
		int listSize = list.size();// 总数
		int threadNum = listSize / dataShardSize;// 启动线程数
		int dataMon = listSize % dataShardSize;// 数据余数
		int threadFullNum = threadNum + (dataMon > 0 ? 1 : 0);// 总共需要启动线程数
		final CountDownLatch latch = new CountDownLatch(threadFullNum);// 同步辅助类
		final ParallelComputingProcess<ListType, ParameterType> processShard = process;
		final ParameterType object = obj;

		if (threadNum > 0) {
			for (int i = 0; i < threadNum; i++) {
				int fromIndex = i * dataShardSize;
				int toIndex = ((i + 1) * dataShardSize);
				final List<ListType> listShard = list.subList(fromIndex, toIndex);
				final int threadId = i;
				Thread t = new Thread() {
					@Override
					public void run() {
						job(threadId, listShard, processShard, object);
						latch.countDown();// 计数减一
					}
				};
				pool.execute(t);
			}
		}

		if (dataMon > 0) {
			int fromIndex = listSize - dataMon;
			int toIndex = listSize;
			final List<ListType> listShard = list.subList(fromIndex, toIndex);
			final int threadId = threadNum;
			Thread t = new Thread() {
				@Override
				public void run() {
					job(threadId, listShard, processShard, object);
					latch.countDown();// 计数减一
				}
			};
			pool.execute(t);
		}

		logger.info(serviceName + "-processForDataShard START (thread count:" + threadFullNum + ",Sharding size:" + dataShardSize + ",total count：" + listSize + ")");
		try {
			latch.await();// 等待子线程结束
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		logger.info(serviceName + "-processForDataShard FINISH (thread count:" + threadFullNum + ",Sharding size:" + dataShardSize + ",total count：" + listSize + ") use time " + (System.currentTimeMillis() - startTime) + "ms");
		return true;
	}

	/**
	 * 并行执行的任务-按设置任务数,如list长度%任务数>0,则会启动任务数+1个任务去处理
	 * 
	 * @param list
	 *            需并行处理的数据
	 * @param process
	 *            业务实现类
	 * @param threadNum
	 *            启动任务处理数
	 *            （如果任务数小于等于0,则单线程处理,如果任务数大于20,则只启动20个线程处理,如果数据小于2000条，则单线程处理）
	 */
	public boolean processForThread(List<ListType> list, ParallelComputingProcess<ListType, ParameterType> process, int threadNum, ParameterType obj) {
		if (list == null || list.size() < 1 || process == null) {
			return false;
		}
		long startTime = System.currentTimeMillis();
		int listSize = list.size();// 总数

		threadNum = threadNum <= 0 ? 1 : threadNum;// 如果设置的启动线程数小于等于0，则单线程处理
		threadNum = threadNum > 20 ? 20 : threadNum;// 限制线程最大启动数为20或21(余数线程)
		threadNum = listSize < 2000 ? 1 : threadNum;// 如果数据小于2000条，则单线程处理。

		int everyListNum = listSize / threadNum;// 每个线程处理数
		int datadMon = listSize % threadNum;// 数据余数
		int threadFullNum = threadNum + (datadMon > 0 ? 1 : 0);// 总共需要启动线程数
		final CountDownLatch latch = new CountDownLatch(threadFullNum);// 同步辅助类
		final ParallelComputingProcess<ListType, ParameterType> processShard = process;
		final ParameterType object = obj;

		for (int i = 0; i < threadNum; i++) {
			int fromIndex = i * everyListNum;
			int toIndex = ((i + 1) * everyListNum);
			final List<ListType> listShard = list.subList(fromIndex, toIndex);
			final int threadId = i;
			Thread t = new Thread() {
				@Override
				public void run() {
					job(threadId, listShard, processShard, object);
					latch.countDown();// 计数减一
				}
			};
			pool.execute(t);
		}

		if (datadMon > 0) {
			int fromIndex = listSize - datadMon;
			int toIndex = listSize;
			final List<ListType> listShard = list.subList(fromIndex, toIndex);
			final int threadId = threadNum;
			Thread t = new Thread() {
				@Override
				public void run() {
					job(threadId, listShard, processShard, object);
					latch.countDown();// 计数减一
				}
			};
			pool.execute(t);
		}
		logger.info(serviceName + "-processForThread START (thread count:" + threadFullNum + ",Sharding size:" + everyListNum + ",total count：" + listSize + ")");
		try {
			latch.await();// 等待子线程结束
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		logger.info(serviceName + "-processForThread FINISH (thread count:" + threadFullNum + ",Sharding size:" + everyListNum + ",total count：" + listSize + ") use time " + (System.currentTimeMillis() - startTime) + "ms");
		return true;
	}

	/** 并行执行任务 */
	private void job(int threadId, List<ListType> list, ParallelComputingProcess<ListType, ParameterType> process, ParameterType obj) {
		process.process(threadId, list, obj);
	}

	public static void main(String[] args) {
		ParallelComputing<String, String> p = new ParallelComputing<String, String>("Test");
		// ParallelComputing<String, String> p = new ParallelComputing<String,
		// String>();
		List<String> list = new ArrayList<String>();
		for (int i = 0; i <= 5001; i++) {
			list.add("_" + i);
		}
		long time = System.currentTimeMillis();
		for (int i = 0; i < 50; i++) {
			boolean boo1 = p.processForDataShard(list, new ParallelComputingProcessImpl(), 3000, "TTTT");
			System.out.println("按数据长度切分任务数-并行处理完毕:" + boo1);
			boolean boo2 = p.processForThread(list, new ParallelComputingProcessImpl(), 3, "TEST");
			System.out.println("按设置任务数-并行处理完毕:" + boo2);
		}
		p.colse();

		System.out.println((System.currentTimeMillis() - time) + "ms");
	}
}
