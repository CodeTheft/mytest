package com.my.test.redis;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisClient {

	private static final Logger LOG = Logger.getLogger(RedisClient.class);
	
	private Jedis jedis;
	
	private JedisPool jedisPool;
	
	public RedisClient() {
		initJedisPool();
		jedis = jedisPool.getResource();
	}
	
	public void initJedisPool() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(5);
		jedisPoolConfig.setMaxWait(1000);
		jedisPoolConfig.setMaxActive(20);
		jedisPoolConfig.setTestOnBorrow(false);
		
		jedisPool = new JedisPool(jedisPoolConfig, "127.0.0.1", 6379, 2000);
	}
	
	public void close() {
		jedisPool.destroy();
	}
	
	public void operator() {
		LOG.debug("获取redis连接，开始操作");
		jedis.set("test_key", "test1");
		String value1 = jedis.get("test_key");
		LOG.debug("获取test_key的值：" + value1);
		
		jedis.del("test_key");
		LOG.debug("删除缓存test_key");
		value1 = jedis.get("test_key");
		LOG.debug("获取test_key的值：" + value1);
		
		new Thread(new Runnable() {
			public void run() {
				RedisSubscribe subscribe = new RedisSubscribe();
				jedis.psubscribe(subscribe, "channel1");
			}
		}).start();
		
		close();
	}
	
	public static void main(String[] args) {
		new RedisClient().operator();
	}
	
}
