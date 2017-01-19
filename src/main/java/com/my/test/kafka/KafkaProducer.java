package com.my.test.kafka;

import java.util.Properties;

import org.apache.log4j.Logger;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

/**
 * 生产者
 * @author chufei
 *
 */
public class KafkaProducer extends Thread {

	private static final Logger LOG = Logger.getLogger(KafkaProducer.class);
	
	private final Producer<Integer, String> producer;
	private final String topic;
	
	public KafkaProducer(String topic) {
		Properties properties = new Properties();
		properties.put("serializer.class", "kafka.serializer.StringEncoder");
		properties.put("metadata.broker.list", KafakaProperties.kafkaServer);
		ProducerConfig producerConfig = new ProducerConfig(properties);
		producer = new Producer<Integer, String>(producerConfig);
		this.topic = topic;
	}
	
	public void run() {
		int num = 0;
		for (int i = 0; i < 10; i++) {
			String message = "Message_" + num;
			LOG.info("SendMessage: " + message);
			KeyedMessage<Integer, String> producerData = new KeyedMessage<Integer, String>(topic, message);
			producer.send(producerData);
			num ++;
			try {
				Thread.sleep(3000);
			} catch (Exception e) {
				LOG.error("SendMessage error", e);
			}
		}
	}
	
}
