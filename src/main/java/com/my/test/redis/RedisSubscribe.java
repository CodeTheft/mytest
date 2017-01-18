package com.my.test.redis;

import org.apache.log4j.Logger;

import redis.clients.jedis.JedisPubSub;

public class RedisSubscribe extends JedisPubSub {

	private static Logger LOG = Logger.getLogger(RedisSubscribe.class);
	
	@Override
	public void onMessage(String channel, String message) {
		LOG.debug("onMessage, channel: " + channel + ", message: " + message);
	}

	@Override
	public void onPMessage(String pattern, String channel, String message) {
		LOG.debug("onPMessage, pattern: " + pattern + ", channel: " + channel + ", message: " + message);
	}

	@Override
	public void onSubscribe(String channel, int subscribedChannels) {
		LOG.debug("onSubscribe, channel: " + channel + ", subscribedChannels: " + subscribedChannels);
	}

	@Override
	public void onUnsubscribe(String channel, int subscribedChannels) {
		LOG.debug("onUnsubscribe, channel: " + channel + ", subscribedChannels: " + subscribedChannels);
	}

	@Override
	public void onPUnsubscribe(String pattern, int subscribedChannels) {
		LOG.debug("onPUnsubscribe, pattern: " + pattern + ", subscribedChannels: " + subscribedChannels);
	}

	@Override
	public void onPSubscribe(String pattern, int subscribedChannels) {
		LOG.debug("onPSubscribe, pattern: " + pattern + ", subscribedChannels: " + subscribedChannels);
	}

}
