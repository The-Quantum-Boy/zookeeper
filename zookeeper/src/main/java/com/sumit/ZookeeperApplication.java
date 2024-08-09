package com.sumit;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

@SpringBootApplication
public class ZookeeperApplication implements ApplicationRunner {
	public static  final Logger log= LoggerFactory.getLogger(ZookeeperApplication.class);

	@Autowired
	MyWatcher watcher;

	public static void main(String[] args) {
		SpringApplication.run(ZookeeperApplication.class, args);
	}


	@Override
	public void run(ApplicationArguments args) throws Exception {

//		ZooKeeper zooKeeper=new ZooKeeper("localhost:2182", 15000, new Watcher() {
//			//executed whenever the any node changes
//			@Override
//			public void process(WatchedEvent watchedEvent) {
//				log.info("***************************************************");
//				log.info("got the event for node = " +  watchedEvent.getPath());
//				log.info("the event type = "+watchedEvent.getType());log.info("***************************************************");
//
////				switch (watchedEvent.getPath()) {
////					case "/node":
////						log.info("event = "+watchedEvent.toString());
////						break;
////					case "/node/child":
////						log.info("event = "+watchedEvent.toString());
////						break;
////				}
//			}
//		});

		ZooKeeper zooKeeper=new ZooKeeper("localhost:2182", 15000,watcher);
				//create node and child node
		zooKeeper.create("/node","data".getBytes(),OPEN_ACL_UNSAFE, CreateMode.PERSISTENT,null);
		zooKeeper.create("/node/child","child".getBytes(),OPEN_ACL_UNSAFE, CreateMode.PERSISTENT,null);
		zooKeeper.create("/node/child2", "child2".getBytes(), OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, null);
		//here OPEN_ACL_UNSAFE -> constant by zookeeper

		//READS
		Stat stat=new Stat();
		var data =zooKeeper.getData("/node",true, stat);

		log.info("data: {}", new String(data));
		log.info(String.valueOf("version = "+stat.getVersion()));

		List<String > children= zooKeeper.getChildren("/node", true);
		children.forEach(child-> log.info("child found = "+child));

		/*
			whenever we register for any watcher that watcher is registered for only once
			for example  here we created the nodes
			zooKeeper.create("/node",...);
		    zooKeeper.create("/node/child",...);

		    and then
		     List<String > children= zooKeeper.getChildren("/node", true); -> set watcher as true

			so when i update the data of node or child node then it will trigger the watcher only once
			zooKeeper.setData("/node","this is new data".getBytes(),-1);
			so after this setting data we need to again set the watcher as true

			then we will see log for delete nodes

		 */

		//update data
		zooKeeper.setData("/node","this is new data".getBytes(),-1);
		//version = -1 means that is update the latest version of node


		//second change for watcher for see next action change
		data=zooKeeper.getData("/node", true, stat);

		//DELETE
//		zooKeeper.delete("/node/child", -1);
//		zooKeeper.delete("/node",-1);

		Thread.sleep(100000);

	}
}
