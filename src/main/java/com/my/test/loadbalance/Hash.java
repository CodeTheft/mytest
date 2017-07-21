package com.my.test.loadbalance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 负载均衡-源地址哈希
 * 
 * <p>源地址哈希法的优点在于：保证了相同客户端IP地址将会被哈希到同一台后端服务器，直到后端服务器列表变更。
 * 					  根据此特性可以在服务消费者与服务提供者之间建立有状态的session会话</p>
 * 
 * <p>源地址哈希算法的缺点在于：除非集群中服务器的非常稳定，基本不会上下线，否则一旦有服务器上线、下线，
 * 					   那么通过源地址哈希算法路由到的服务器是服务器上线、下线前路由到的服务器的概率非常低，
 * 					   如果是session则取不到session，如果是缓存则可能引发"雪崩"</p>
 * 
 * @author chufei
 * @date 2017年7月20日
 */
public class Hash {

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

		// 客户端请求ip，可通过HttpServlet的getRemoteIp获取
		String remoteIp = "127.0.0.1";
		int hashCode = remoteIp.hashCode();
		int pos = hashCode % keySet.size();
		
		return ipList.get(pos);
	}
	
}
