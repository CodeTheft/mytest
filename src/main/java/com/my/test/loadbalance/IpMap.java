package com.my.test.loadbalance;

import java.util.HashMap;
import java.util.Map;

/**
 * 负载均衡， 自定义ip库模拟多请求
 * @author chufei
 * @date 2017年7月20日
 */
public class IpMap {

	/**
	 * key表示ip地址，value表示该ip所占权重
	 */
	public static final Map<String, Integer> SERVER_WEIGHT_MAP = new HashMap<String, Integer>();
	
	static {
		SERVER_WEIGHT_MAP.put("192.168.1.100", 1);
		SERVER_WEIGHT_MAP.put("192.168.1.101", 1);
		SERVER_WEIGHT_MAP.put("192.168.1.102", 1);
		SERVER_WEIGHT_MAP.put("192.168.1.103", 4);
		SERVER_WEIGHT_MAP.put("192.168.1.104", 1);
		SERVER_WEIGHT_MAP.put("192.168.1.105", 2);
		SERVER_WEIGHT_MAP.put("192.168.1.106", 1);
		SERVER_WEIGHT_MAP.put("192.168.1.107", 3);
		SERVER_WEIGHT_MAP.put("192.168.1.108", 2);
		SERVER_WEIGHT_MAP.put("192.168.1.109", 1);
		SERVER_WEIGHT_MAP.put("192.168.1.110", 1);
	}
	
}
