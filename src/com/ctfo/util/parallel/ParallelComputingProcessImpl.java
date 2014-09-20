package com.ctfo.util.parallel;

import java.util.List;

/***
 * 小例子-应用业务类
 * 
 * @author James 2014-8-6 10:02:13
 * 
 */
public class ParallelComputingProcessImpl implements ParallelComputingProcess<String, String> {

	@Override
	public void process(int threadId, List<String> list, String obj) {
		System.out.println(Thread.currentThread().getThreadGroup().getName() + "-" + Thread.currentThread().getName() + "-threadId[" + threadId + "],obj>>>" + obj + "----test>>" + list);
	}

}
