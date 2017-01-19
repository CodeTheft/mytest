package com.my.test.kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

/**
 * 消费者
 * @author chufei
 *
 */
public class KafkaConsumer extends Thread {

	private static final Logger LOG = Logger.getLogger(KafkaConsumer.class);
	
	private final ConsumerConnector consumer;
	private final String topic;
	
	public KafkaConsumer(String topic) {
		consumer = Consumer.createJavaConsumerConnector(getConsumerConfig());
		this.topic = topic;
	}
	
	private static ConsumerConfig getConsumerConfig() {
		Properties properties = new Properties();
		properties.put("zookeeper.connect", KafakaProperties.zkConnect);
		properties.put("zookeeper.session.timeout.ms", "40000");
		properties.put("group.id", "group1");
		properties.put("zookeeper.sync.time.ms", "200");
		properties.put("auto.commit.interval.ms", "1000");
        return new ConsumerConfig(properties);
	}

	public void run() {
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(topic, 1);
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
		KafkaStream<byte[], byte[]> stream = consumerMap.get(topic).get(0);
		ConsumerIterator<byte[], byte[]> iter = stream.iterator();
		while(iter.hasNext()) {
			LOG.info("ReceiveMessage: " + new String(iter.next().message()));
		}
	}
	
}
