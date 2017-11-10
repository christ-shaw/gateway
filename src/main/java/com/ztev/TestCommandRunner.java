package com.ztev;

import com.ztev.appMessage.ApplicationStartUp;
import com.ztev.module.ActionInfo;
import com.ztev.module.ActionResult;
import com.ztev.module.DatabaseConfig;
import com.ztev.module.ExecutorUnit;
import com.ztev.service.ProcessesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;
import java.util.jar.JarEntry;

/**
 * Created by Administrator on 2017/4/13.
 */
@Component
public class TestCommandRunner implements CommandLineRunner ,ApplicationEventPublisherAware{


    @Autowired
    StartupAnalyze analyze;


    private Logger logger = LoggerFactory.getLogger(TestCommandRunner.class);


    @Autowired
    DatabaseConfig config;

    @Autowired
    ProcessesService ps;

    @Autowired
    ApplicationEventPublisher publisher;

    @Override
    public void run(String... args) throws Exception
    {

        if (args.length > 0 && args[0].equalsIgnoreCase("quite")) {

            ps.setDatabaseConfig(config);
            PriorityQueue<ExecutorUnit> allProcesses = new PriorityQueue<>(ps.getAllProcesses());
            ExecutorUnit unit;
            while ( (unit = allProcesses.poll()) !=null )
            {
                logger.info("ready to star {}, priority = {}" , unit.getInstanceName(),unit);
                 ps.execute(ActionInfo.builder().instanceName(unit.getInstanceName()).action("start").timestamp(new Date().getTime()).build());
            }
            logger.info("\n\n");

            logger.info("*************************************** startup summary******************************************");

            analyze.startUPInfo.forEach((key,value) ->
            {
                logger.info("instance = {} , spent {} seconds",key,(value.end - value.begin) / (1000));
            });
            logger.info("*********************************** summary end **************************************************");
            publisher.publishEvent( new ApplicationStartUp(this.getClass(),analyze.startUPInfo));
            }

        }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {

    }
}
