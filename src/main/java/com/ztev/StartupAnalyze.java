package com.ztev;

import com.ztev.module.TimeRange;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ${xiaozb} on 2017/8/4.
 *
 * @copyright by ztev
 */


@Component
public class StartupAnalyze
{

    public Map<String,TimeRange> startUPInfo = new HashMap<>();

}
