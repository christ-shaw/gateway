package com.ztev.module;

import lombok.Builder;
import lombok.Data;

import java.io.File;

/**
 * Created by Administrator on 2017/3/29.
 */
@Data
@Builder
public class ExecutorUnit implements Comparable<ExecutorUnit>{
    private String instanceName;
    private String port;
    private String workingPath;
    private int priority;
    private File executor;

    @Override
    public int compareTo(ExecutorUnit o) {
        if (o == null)
        {
            throw new  NullPointerException();
        }
        if (this.priority < o.priority)
        {
            return -1;
        }
        else if (this.priority == o.priority)
        {
            return 0;
        }
        return 1;
    }
}



