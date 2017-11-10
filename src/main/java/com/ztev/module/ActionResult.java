package com.ztev.module;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by ${xiaozb} on 2017/7/31.
 *
 * @copyright by ztev
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActionResult {
    boolean status;
    String msg;
}
