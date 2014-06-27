package com.ctfo.util.parallel;

import java.util.List;

/**
 * 并行处理-业务执行接口
 * 
 * @param ListType
 *            需并行处理的List泛型
 * @param ParameterType
 *            业务处理时传入的参数类型
 * @author James 2013-11-20 14:05:22
 */
public interface ParallelComputingProcess<ListType, ParameterType> {

	public void process(int threadId, List<ListType> list, ParameterType obj);
}
