package com.my.test.kafka;

/**
 * kafka配置常量
 * @author chufei
 *
 */
public interface KafakaProperties {

	public static String zkConnect = "localhost:2181";
	
	public static String kafkaServer = "localhost:9092";
	
	public static int kafkaProducerBufferSize = 64 * 1024;
	
	public static int connectionTimeout = 20000;
	
	public static int reconnectInterval = 10000;
	
}
