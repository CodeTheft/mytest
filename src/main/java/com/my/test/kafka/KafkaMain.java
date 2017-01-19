package com.my.test.kafka;

public class KafkaMain {

	public static void main(String[] args) {
		
		KafkaProducer producer = new KafkaProducer("mykafka1");
		producer.start();
		
		KafkaConsumer consumer = new KafkaConsumer("mykafka1");
		consumer.start();
	}
	
}
