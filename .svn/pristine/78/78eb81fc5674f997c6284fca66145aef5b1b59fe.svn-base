package com.ctfo.util.parallel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ctfo.statement.SystemStatement;

public class ParallelComputingProcessImpl implements ParallelComputingProcess<String, String> {

	private Logger logger = Logger.getLogger(ParallelComputingProcessImpl.class);

	@Override
	public void process(int threadId, List<String> list, String obj) {
		// 一定要捕获异常。
		try {
			Map<String, String> speedRulesMap = speedRules(list.size(), 30000L);
			String flag = speedRulesMap.get("flag");
			String[] before = speedRulesMap.get("before").split("_");
			String[] after = speedRulesMap.get("after") == null ? null : speedRulesMap.get("after").split("_");
			long before0 = Long.parseLong(before[0]);
			long before1 = before.length == 2 ? Long.parseLong(before[1]) : 0L;
			long after1 = after != null ? Long.parseLong(after[1]) : 0L;
			logger.debug("threadId:" + threadId + ",list.size():" + list.size() + ",speedRulesMap:" + speedRulesMap);

			long time = System.currentTimeMillis();
			long i = 0;
			for (String str : list) {
				try {
					if (SystemStatement.SEND_AVERAGE_SLEEP) {
						// 匀速控制
						if ("1".equals(flag)) {
							if (before.length == 1) {
								// 每毫秒?条
								if (i % before0 == 0) {
									Thread.sleep(1L);
								}
							} else {
								if (i <= before0) {
									// 前?条，每毫秒?条;
									if (i % before1 == 0) {
										Thread.sleep(1L);
									}
								} else {
									// 后?条，每毫秒?条
									if (i % after1 == 0) {
										Thread.sleep(1L);
									}
								}
							}
						} else {
							if (before.length == 1) {
								// 每?毫秒1条
								Thread.sleep(before0);
							} else {
								if (i <= before0) {
									// 前?条，每?毫秒1条;
									Thread.sleep(before1);
								} else {
									// 后?条，每?毫秒1条
									Thread.sleep(after1);
								}
							}
						}
					}
				} catch (Exception e) {
					logger.error("threadId:,process forEach inside ERROR! ", e);
				}
				i++;
			}
			logger.debug(threadId + "---" + (System.currentTimeMillis() - time) + "ms");
		} catch (Exception e) {
			e.printStackTrace();
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
