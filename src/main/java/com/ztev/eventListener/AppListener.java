package com.ztev.eventListener;


import com.ztev.StartupAnalyze;

import com.ztev.module.TimeRange;
import de.codecentric.boot.admin.event.ClientApplicationRegisteredEvent;
import de.codecentric.boot.admin.event.ClientApplicationStatusChangedEvent;
import de.codecentric.boot.admin.event.RoutesOutdatedEvent;
import de.codecentric.boot.admin.registry.StatusUpdateApplicationListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

/**
 * Created by ${xiaozb} on 2017/8/4.
 *
 * @copyright by ztev
 */
@Component
public class AppListener {

    @Autowired
    StartupAnalyze analyze;

    @EventListener
    public void onClientApplicationRegistered(ClientApplicationRegisteredEvent event) {

    }


    @EventListener
    public void onApplicationReady(ApplicationReadyEvent event) {
       event.toString();
    }

    @EventListener
    public  void onClientAppStatusUpdated(ClientApplicationStatusChangedEvent event)
    {
        String appName = event.getApplication().getName();
        if ( event.getTo().isUp())
        if (analyze.startUPInfo.containsKey(appName))
        {
            TimeRange range = analyze.startUPInfo.get(appName);
            range.end = new Date().getTime();
        }
    }
}
