package com.ztev.module;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Administrator on 2017/3/29.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActionInfo
{
    private String instanceName;
    private String action;
    private long timestamp;

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }
}
