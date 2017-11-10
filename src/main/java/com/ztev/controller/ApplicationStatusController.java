package com.ztev.controller;

import com.ztev.appMessage.ApplicationStartUp;
import com.ztev.module.InstanceStartUpSummary;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ${xiaozb} on 2017/8/14.
 *
 * @copyright by ztev
 */
@RequestMapping("/ztev/status")
@RestController
public class ApplicationStatusController {

    InstanceStartUpSummary summaryList = new InstanceStartUpSummary();

    @RequestMapping(method = RequestMethod.GET)
    public  synchronized InstanceStartUpSummary getSummary()
    {
          return summaryList;
    }

    @EventListener
    public  synchronized void onAPPStarted(ApplicationStartUp msg)
    {
         summaryList.bStarted = true;
          msg.getStartUPInfo().forEach((k,v) ->
          summaryList.summaries.put(k, (v.end-v.begin)/1000));
    }

}
