package com.my.test.loadbalance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 负载均衡-加权轮询
 * 
 * <p>权重值越大，该服务器每轮所获得的请求数量越多</p>
 * 
 * @author chufei
 * @date 2017年7月20日
 */
public class WeightRoundRobin {

	private static Integer pos = 0;

	public static String getServer() {

		// SERVER_WEIGHT_MAP是动态列表，服务上下线可能导致列表数据改变
		// 方法内部要新建局部变量serverMap，现将serverMap中的内容复制到线程本地，以避免被多个线程修改
		// 但是可能导致新问题，如果在期间新增或下线服务器，负载均衡算法将无法获知
		Map<String, Integer> serverMap = new HashMap<String, Integer>();
		serverMap.putAll(IpMap.SERVER_WEIGHT_MAP);

		// 获取ip列表
		Set<String> keySet = serverMap.keySet();
		Iterator<String> iterator = keySet.iterator();
		
		ArrayList<String> ipList = new ArrayList<String>();
		while (iterator.hasNext()) {
			String server = iterator.next();
			// 根据服务器获取该权重
			int weight = serverMap.get(server);
			// 权重越大的将会承载更多请求
			for (int i = 0; i < weight; i++) {
				ipList.add(server);
			}
		}
		
		String server = null;
		// 对服务器列表下标加锁同步，避免并发下数组越界
		synchronized (pos) {
			if (pos > ipList.size()) {
				pos = 0;
			}
			server = ipList.get(pos);
			pos++;
		}
		return server;
	}

}
