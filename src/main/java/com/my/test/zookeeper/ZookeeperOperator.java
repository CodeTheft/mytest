package com.my.test.zookeeper;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;

public class ZookeeperOperator extends AbstractZookeeper {

	private static Logger LOG = Logger.getLogger(ZookeeperOperator.class);
	
	/**
	 * 创建一个znode节点
	 * @param path 节点路径
	 * @param data 节点数据
	 * @throws InterruptedException 
	 * @throws KeeperException 
	 */
	public void create(String path, byte[] data) throws KeeperException, InterruptedException {
		/*
		 * 第三个参数acl，表示访问控制权限
		 * perms 和 id。
		 * Perms 有 ALL、READ、WRITE、CREATE、DELETE、ADMIN 几种 
		 * 而 id 标识了访问目录节点的身份列表，默认情况下有以下两种：
		 * ANYONE_ID_UNSAFE = new Id("world", "anyone") 和 AUTH_IDS = new Id("auth", "") 分别表示任何人都可以访问和创建者拥有访问权限。
		 */
		/*
		 *  第四个参数createMode，表示节点类型
		 *  PERSISTENT 持久性
		 *  PERSISTENT_SEQUENTIAL 自增持久
		 *  EPHEMERAL 非持久
		 *  EPHEMERAL_SEQUENTIAL 自增非持久
		 */
		this.zooKeeper.create(path, data, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	}
	
	/**
	 * 获取节点信息
	 * @param path
	 * @throws InterruptedException 
	 * @throws KeeperException 
	 */
	public void getChild(String path) throws KeeperException, InterruptedException {
		List<String> list = this.zooKeeper.getChildren(path, false);//不设置特定的监控器
		if (list.isEmpty()) {
			LOG.debug(path + "没有节点");
		} else {
			LOG.debug(path + "有节点");
			for (String string : list) {
				LOG.debug("节点是：" + string);
			}
		}
	}
	
	public byte[] getData(String path) throws KeeperException, InterruptedException {
		return this.zooKeeper.getData(path, false, null);
	}
	
	public static void main(String[] args) {
		try {
			ZookeeperOperator zookeeperOperator = new ZookeeperOperator();
			zookeeperOperator.connect("192.168.20.199");
			
//			byte[] data = new byte[]{'a','b','c','d'};
//			String tests = "zookeeper测试";
//			zookeeperOperator.create("/myzk/children2", data);
			LOG.debug("获取设置的信息：" + new String(zookeeperOperator.getData("/myzk/children2")));  
            
            System.out.println("节点孩子信息:");     
            zookeeperOperator.getChild("/myzk");
            
            zookeeperOperator.close();
		} catch (Exception e) {
			LOG.error("error.", e);
		}
	}
	
}
