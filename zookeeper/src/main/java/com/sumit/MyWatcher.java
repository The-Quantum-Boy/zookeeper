package com.sumit;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MyWatcher  implements Watcher {

    public static  final Logger log= LoggerFactory.getLogger(ZookeeperApplication.class);

    @Override
    public void process(WatchedEvent watchedEvent) {
        //executed whenever the any node changes
            //this willl give only parent stat only
            log.info("***************************************************");
            log.info("got the event for node = " +  watchedEvent.getPath());
            log.info("the event type = "+watchedEvent.getType());
            log.info("***************************************************");

//				switch (watchedEvent.getPath()) {
//					case "/node":
//						log.info("event = "+watchedEvent.toString());
//						break;
//					case "/node/child":
//						log.info("event = "+watchedEvent.toString());
//						break;
//				}
    }

}
