package com.ztev.appMessage;

import com.ztev.module.TimeRange;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

/**
 * Created by ${xiaozb} on 2017/8/14.
 *
 * @copyright by ztev
 */
public class ApplicationStartUp extends ApplicationEvent
{
    private final Map<String, TimeRange> startUPInfo;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     * @param startUPInfo
     */
    public ApplicationStartUp(Object source, Map<String, TimeRange> startUPInfo) {
        super(source);
        this.startUPInfo = startUPInfo;
    }


    public Map<String, TimeRange> getStartUPInfo() {
        return startUPInfo;
    }
}
