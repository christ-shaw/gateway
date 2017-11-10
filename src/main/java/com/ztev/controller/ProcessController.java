package com.ztev.controller;

import com.ztev.module.*;
import com.ztev.service.ProcessesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by Administrator on 2017/3/23.
 */

@RequestMapping("/ztev")
@RestController
public class ProcessController
{
    @Autowired
    ProcessesService ps;

    //@RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model)
    {
        PriorityQueue<ExecutorUnit> allUnits = ps.getAllProcesses();
        model.addAttribute("units",allUnits);
        model.addAttribute("databaseConfig",new DatabaseConfig());
        return "index";
    }


    @RequestMapping(path = "/databaseConfig", method = RequestMethod.POST)
    public String addDatabaseConfig(DatabaseConfig config ,Model model) {

        ps.setDatabaseConfig(config);
        PriorityQueue<ExecutorUnit> allUnits = ps.getAllProcesses();
        model.addAttribute("units",allUnits);
        model.addAttribute("databaseConfig",config);
        return "index";
    }



    @RequestMapping(path = "/instance", method = RequestMethod.POST)
    public ActionResult instanceAction(@RequestBody ActionInfo info ) {

       return ps.execute(info);
    }
    @RequestMapping(path = "/instance", method = RequestMethod.GET)
    public List<InstanceName> getAllInstanceNames()
    {

      return   ps.getAllInstanceName();
    }

    @RequestMapping(path = "/instance/kill", method = RequestMethod.POST)
    public void killAll()
    {
        ps.killAll();
    }

}
