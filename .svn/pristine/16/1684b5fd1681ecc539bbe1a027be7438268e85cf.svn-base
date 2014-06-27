package com.ctfo.manage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

import com.ctfo.dao.TestSaveCenterDao;
import com.ctfo.queue.ServiceQueue;
import com.ctfo.spring.factory.InitSpring;
import com.ctfo.statement.SystemStatement;
import com.ctfo.util.parallel.ParallelComputing;
import com.ctfo.util.parallel.ParallelComputingProcess;

/**
 * 车辆数据调度器(数据分配给多个队列)
 * 
 * @author James 2014-3-15 1:06:13
 * 
 */
public class Scheduler implements ParallelComputingProcess<Map<String, Object>, DataEnum.DataTypeSevice> {

	private static Logger logger = Logger.getLogger(Scheduler.class);

	public void loadDB() {
		long startTime = System.currentTimeMillis();
		logger.info("DB load START!");
		TestSaveCenterDao testSaveCenterDao = (TestSaveCenterDao) InitSpring.INSTANCE.getBean("testSaveCenterDao");
		SystemStatement.RS_VEHICLE = testSaveCenterDao.queryVehicle(SystemStatement.START_SIM, SystemStatement.END_SIM);
		logger.info("DB Data size:" + SystemStatement.RS_VEHICLE.size());
		logger.info("DB load OK! time:" + (System.currentTimeMillis() - startTime) + "ms");
	}

	/** 将数据配置的数量及时间间隔调度到各存储队列 */
	public void schedulerByData() {
		logger.info("schedulerByData START! TIME:" + System.currentTimeMillis());
		// 按照如下顺利推送数据
		this.executeService(DataEnum.DataTypeSevice.REG_DATA);// 注册
		this.executeService(DataEnum.DataTypeSevice.AUTH_DATA);// 鉴权
		this.executeService(DataEnum.DataTypeSevice.ISONLINE_DATA);// 上线
		this.executeServiceByParallel(DataEnum.DataTypeSevice.TRACK_DATA, DataEnum.DataTypeSevice.TRACK_ALARM_DATA);// 位置&&报警
		this.executeService(DataEnum.DataTypeSevice.ISOFFLINE_DATA);// 下线
		logger.info("schedulerByData FINISH! TIME:" + System.currentTimeMillis());
	}

	/** 并行执行的指令 */
	private void executeServiceByParallel(DataEnum.DataTypeSevice... dataTypeSevices) {
		final CountDownLatch latch = new CountDownLatch(dataTypeSevices.length);// 同步辅助类
		for (final DataEnum.DataTypeSevice dataTypeSevice : dataTypeSevices) {
			new Thread() {
				@Override
				public void run() {
					executeService(dataTypeSevice);
					latch.countDown();
				}
			}.start();
		}
		try {
			latch.await();// 等待子线程结束
		} catch (Exception e) {
			logger.error("CountDownLatch.await() ERROR!", e);
		}
	}

	/** 指令数据调度业务 */
	private void executeService(DataEnum.DataTypeSevice dataTypeSevice) {
		try {
			// 并行计算框架
			ParallelComputing<Map<String, Object>, DataEnum.DataTypeSevice> p = new ParallelComputing<Map<String, Object>, DataEnum.DataTypeSevice>(dataTypeSevice.toString());
			long sendDataSize = dataTypeSevice.getSendDataSize();
			for (int i = 0; i < sendDataSize; i++) {
				try {
					long startTime = System.currentTimeMillis();
					p.processForThread(SystemStatement.RS_VEHICLE, this, SystemStatement.SYS_CPU_COUNT, dataTypeSevice);
					long time = System.currentTimeMillis() - startTime;// 耗时
					if (!SystemStatement.SEND_AVERAGE_SLEEP) {
						dataTypeSevice.sendSleep(dataTypeSevice.getSendDataSleep() - time);
					}
				} catch (Exception e) {
					logger.error("excuteService " + dataTypeSevice.name() + " ERROR!", e);
				}
			}
		} catch (Exception e) {
			logger.error("excuteService ERROR!", e);
		}
	}

	@Override
	public void process(int threadId, List<Map<String, Object>> list, DataEnum.DataTypeSevice dataTypeSevice) {
		try {
			Map<String, String> speedRulesMap = speedRules(list.size(), dataTypeSevice.getSendDataSleep());
			String flag = speedRulesMap.get("flag");
			String[] before = speedRulesMap.get("before").split("_");
			String[] after = speedRulesMap.get("after") == null ? null : speedRulesMap.get("after").split("_");
			long before0 = Long.parseLong(before[0]);
			long before1 = before.length == 2 ? Long.parseLong(before[1]) : 0L;
			long after1 = after != null ? Long.parseLong(after[1]) : 0L;
			logger.debug("threadId:" + threadId + ",list.size():" + list.size() + ",speedRulesMap:" + speedRulesMap);

			long time = System.currentTimeMillis();
			long i = 0;
			for (Map<String, Object> map : list) {
				try {
					if (ServiceQueue.INSTANCE.getQueueListSize() <= 0) {
						break;
					}
					long vid = Long.parseLong(map.get("VID") + "");
					int index = (int) (vid % ServiceQueue.INSTANCE.getQueueListSize());
					ServiceQueue.INSTANCE.addQueueByData(ServiceQueue.INSTANCE.getQueueName(index), dataTypeSevice.buildCommand(map));
					if (SystemStatement.SEND_AVERAGE_SLEEP) {
						// 匀速控制
						if ("1".equals(flag)) {
							if (before.length == 1) {
								// 每毫秒?条
								if (i % before0 == 0) {
									dataTypeSevice.sendSleep(1L);
								}
							} else {
								if (i <= before0) {
									// 前?条，每毫秒?条;
									if (i % before1 == 0) {
										dataTypeSevice.sendSleep(1L);
									}
								} else {
									// 后?条，每毫秒?条
									if (i % after1 == 0) {
										dataTypeSevice.sendSleep(1L);
									}
								}
							}
						} else {
							if (before.length == 1) {
								// 每?毫秒1条
								dataTypeSevice.sendSleep(before0);
							} else {
								if (i <= before0) {
									// 前?条，每?毫秒1条;
									dataTypeSevice.sendSleep(before1);
								} else {
									// 后?条，每?毫秒1条
									dataTypeSevice.sendSleep(after1);
								}
							}
						}
					}
				} catch (Exception e) {
					logger.error("threadId:" + threadId + ",dataTypeSevice:" + dataTypeSevice + ",process forEach inside ERROR! ", e);
				}
				i++;
			}
			logger.debug(threadId + "---" + dataTypeSevice + "---" + (System.currentTimeMillis() - time) + "ms");
		} catch (Exception e) {
			logger.error("threadId:" + threadId + ",dataTypeSevice:" + dataTypeSevice + ",process ERROR! ", e);
		}
	}

	/**
	 * 抽取匀速规则
	 * 
	 * @param total
	 *            处理条数
	 * 
	 * @param timeLimit
	 *            时间限制
	 * 
	 * @return 匀速规则
	 */
	private static Map<String, String> speedRules(long total, long timeLimit) {
		Map<String, String> map = new HashMap<String, String>();
		if (total >= timeLimit) {
			map.put("flag", "1");// 每毫秒?条
			if (total % timeLimit == 0) {
				map.put("before", (total / timeLimit) + "");// 每毫秒?条
			} else {
				int x = (int) (total / timeLimit + 1);
				map.put("before", ((total - ((x * timeLimit - total) * (x - 1)))) + "_" + x);// 前?条，每毫秒?条
				map.put("after", ((x * timeLimit - total) * (x - 1)) + "_" + (x - 1));// 后?条，每毫秒?条
			}
		} else {
			map.put("flag", "2");// 每?毫秒1条
			if (timeLimit % total == 0) {
				map.put("before", (timeLimit / total) + "");// 每?毫秒1条
			} else {
				int x = (int) (timeLimit / total);
				map.put("before", (timeLimit - x * total) + "_" + (x + 1));// 前?条，每?毫秒1条
				map.put("after", (total - (timeLimit - x * total)) + "_" + x);// 后?条，每?毫秒1条
			}
		}
		return map;
	}
}
