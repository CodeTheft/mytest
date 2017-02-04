package com.my.test.rabbitmq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQProducer {

	private static String queue_name = "test_queue";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		Connection connection = null;
		Channel channel = null;
		try {
			ConnectionFactory factory = new ConnectionFactory();
			
			/*
			 * 设置MabbitMQ所在主机ip或者主机名
			 * 使用rabbitmq服务器默认的virutal host(“/”),默认的用户guest/guest进行连接
			 * 
			 * 可以设置其他参数
			 * setHost("localhost")
			 * setPort(5672);
			 * setUsername("rabbitmq_producer");
			 * setPassword("123456");
			 * setVirtualHost("test_vhosts");
			 */
			factory.setHost("localhost");
			
			connection = factory.newConnection();
			channel = connection.createChannel();
			
			/*
			 * 第二个参数durable表示建立的消息队列是否是持久化(RabbitMQ重启后仍然存在,并不是指消息的持久化)
			 * 第三个参数exclusive 表示建立的消息队列是否只适用于当前TCP连接
			 * 第四个参数autoDelete表示当队列不再被使用时，RabbitMQ是否可以自动删除这个队列
			 * 第五个参数arguments定义了队列的一些参数信息，主要用于Headers Exchange进行消息匹配时
			 */
			channel.queueDeclare(queue_name, false, false, true, null);
			
			String message = "hello world";
			
			/*
			 * 第一个参数exchange是消息发送的Exchange名称，如果没有指定，则使用Default Exchange
			 * 第二个参数routingKey是消息的路由Key，是用于Exchange将消息路由到指定的消息队列时使用(如果Exchange是Fanout Exchange，这个参数会被忽略)
			 * 第三个参数props是消息包含的属性信息
			 * 第四个参数body是RabbitMQ消息体
			 */
			channel.basicPublish("", queue_name, null, message.getBytes());
			System.out.println("Send Message is:'" + message + "'");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (channel != null) {
				channel.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
	}
	
}
