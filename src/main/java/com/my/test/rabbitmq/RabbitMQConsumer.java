package com.my.test.rabbitmq;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class RabbitMQConsumer {

	private static String queue_name = "test_queue";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // 声明队列，主要为了防止消息接收者先运行此程序，队列还不存在时创建队列
        channel.queueDeclare(queue_name, false, false, true, null);
        
        Consumer consumer = new DefaultConsumer(channel) {
        	/*
        	 * (non-Javadoc)
        	 * @see com.rabbitmq.client.DefaultConsumer#handleDelivery(java.lang.String, com.rabbitmq.client.Envelope, com.rabbitmq.client.AMQP.BasicProperties, byte[])
        	 * 第一个参数consumerTag是接收到消息时的消费者Tag，如果我们没有在basicConsume方法中指定Consumer Tag，RabbitMQ将使用随机生成的Consumer Tag
        	 * 第二个参数envelope是消息的打包信息，包含了四个值:
        	 * 	1._deliveryTag，消息发送的编号，表示这条消息是RabbitMQ发送的第几条消息
        	 * 	2._redeliver，重传标志，确认在收到对消息的失败确认后，是否需要重发这条消息
        	 *  3._exchange，消息发送到的Exchange名称，如果exchange名称为空，使用的是Default Exchange
        	 *  4._routingKey,消息发送的路由Key，我们这里是发送消息时设置的“test_queue”
        	 * 第三个参数properties就是上面使用basicPublish方法发送消息时的props参数，如果消费者设置的properties为null，那接收到的properties是默认的Properties，只有bodySize，其他全是null
        	 * 第四个参数body是消息体
        	 */
        	@Override
        	public void handleDelivery(String consumerTag,
                    Envelope envelope,
                    AMQP.BasicProperties properties,
                    byte[] body) throws UnsupportedEncodingException {
        		String message = new String(body, "UTF-8");
        		System.out.println(" Consumer have received '" + message + "'");
        	}
        };
        /*
         * 第一个参数是Consumer绑定的队列名
         * 第二个参数是自动确认标志，如果为true，表示Consumer接受到消息后，会自动发确认消息(Ack消息)给消息队列，消息队列会将这条消息从消息队列里删除
         * 第三个参数就是Consumer对象，用于处理接受到的消息。
         */
        channel.basicConsume(queue_name, true, consumer);
	}
	
}
