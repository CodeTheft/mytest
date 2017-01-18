package com.my.test.zookeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;

public class AbstractZookeeper implements Watcher {

	// 缓存时间
	private static final int SESSION_TIME = 2000;
	
	protected ZooKeeper zooKeeper;
	protected CountDownLatch countDownLatch = new CountDownLatch(1);
	
	public void connect(String host) throws IOException, InterruptedException {
		zooKeeper = new ZooKeeper(host, SESSION_TIME, this);
		countDownLatch.await();
	}
	
	public void process(WatchedEvent event) {
		if (event.getState() == KeeperState.SyncConnected) {
			countDownLatch.countDown();
		}
	}

	public void close() throws InterruptedException {
		zooKeeper.close();
	}
}
