package com.ctfo.util.parallel;

import java.util.List;

/**
 * 并行处理-业务执行接口
 * 
 * @param ListType
 *            需并行处理的List中的泛型
 * @param ParameterType
 *            业务处理时传入的参数类型
 * @author James 2013-11-20 14:05:22
 */
public interface ParallelComputingProcess<ListType, ParameterType> {

	/***
	 * 具体的业务实现在该方法中编写
	 * @param threadId 线程ID
	 * @param list 切分后的小List集合
	 * @param obj 外部传入数据，为业务实现提供数据支持
	 */
	public void process(int threadId, List<ListType> list, ParameterType obj);
}
