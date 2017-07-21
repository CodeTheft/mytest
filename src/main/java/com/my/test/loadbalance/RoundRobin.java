package com.my.test.loadbalance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 负载均衡-轮询
 * 
 * <p>轮询法的优点在于：试图做到请求转移的绝对均衡</p>
 * <p>轮询法的缺点在于：为了做到请求转移的绝对均衡，必须付出相当大的代价，因为为了保证pos变量修改的互斥性，
 * 				 需要引入重量级的悲观锁synchronized，这将会导致该段轮询代码的并发吞吐量发生明显的下降</p>
 * 
 * @author chufei
 * @date 2017年7月20日
 */
public class RoundRobin {

	private static Integer pos = 0;

	public static String getServer() {

		// SERVER_WEIGHT_MAP是动态列表，服务上下线可能导致列表数据改变
		// 方法内部要新建局部变量serverMap，现将serverMap中的内容复制到线程本地，以避免被多个线程修改
		// 但是可能导致新问题，如果在期间新增或下线服务器，负载均衡算法将无法获知
		Map<String, Integer> serverMap = new HashMap<String, Integer>();
		serverMap.putAll(IpMap.SERVER_WEIGHT_MAP);

		// 获取ip列表
		Set<String> keySet = serverMap.keySet();
		ArrayList<String> ipList = new ArrayList<String>();
		ipList.addAll(keySet);

		String server = null;
		// 对服务器列表下标加锁同步，避免并发下数组越界
		synchronized (pos) {
			if (pos > keySet.size()) {
				pos = 0;
			}
			server = ipList.get(pos);
			pos++;
		}
		return server;
	}

}
