package com.ztev.service;

import com.ztev.StartupAnalyze;
import com.ztev.module.*;
import com.ztev.utils.CompressUtils;
import de.codecentric.boot.admin.event.ClientApplicationStatusChangedEvent;
import org.codehaus.plexus.archiver.tar.GZipTarFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by Administrator on 2017/3/23.
 */

@Service
public class ProcessesService {
    @Autowired
    StartupAnalyze analyze;

    private Logger logger = LoggerFactory.getLogger(ProcessesService.class);

    private ExecutorUnit curUnit;

    @Autowired
    private DatabaseConfig genv = null;

    PriorityQueue<ExecutorUnit> executorUnits = new PriorityQueue<>();

    private final String url = "works";

    public String[] createArgs(String action) throws UnsupportedOperationException {

            if (System.getProperty("os.name").contains("Windows")){
                String scriptFile = action.equalsIgnoreCase("start") ? "run.bat" : "stop.bat";
                return new String[]{"cmd.exe", "/c", scriptFile};
            }
            else
            {
                String scriptFile = action.equalsIgnoreCase("start") ? "run.sh" : "stop.sh";
                return new String[]{"/bin/bash",  scriptFile};
            }

    }


    @PostConstruct
    public void init() {

        // 深度优先搜索
        HashMap<String, List<File>> files = listFilesWithSuffix("hostagent.properties");
        files.forEach((key, value) ->
        {
            if (!value.isEmpty()) {
                try {
                    Properties properties = new Properties();
                    properties.load(new FileInputStream(value.get(0)));
                    ExecutorUnit unit = ExecutorUnit.builder().workingPath(key).instanceName(properties.getProperty("instancename"))
                                        .priority(properties.getProperty("priority") != null?
                                                new Integer(properties.getProperty("priority")).intValue():255 ).port("port").build();
                    executorUnits.add(unit);
                } catch (FileNotFoundException ex) {
                    logger.error(ex.getLocalizedMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error(e.getLocalizedMessage());
                }

            } else {
                logger.error("no file found in " + key);
            }

        });

    }

    public PriorityQueue<ExecutorUnit> getAllProcesses() {
        return executorUnits;
    }


    private HashMap<String, List<File>> listFilesWithSuffix(String... suffixes) {
        HashMap<String, List<File>> result = new HashMap<String, List<File>>();
        for (String suffix : suffixes) {
            listRunScripts(new File(url), suffix, result);
        }
        return result;
    }

    private void listRunScripts(File rootFile, String suffix, Map<String, List<File>> result) {
        File[] files = rootFile.listFiles(pathname -> {
            if (pathname.isDirectory()) {
                listRunScripts(pathname, suffix, result);
            } else {
                if (pathname.getName().endsWith(suffix)) {
                    return true;
                }
            }
            return false;
        });
        if (files == null) {
            logger.warn("found no script file");
            return;
        }
        for (File f : files) {
            // get parent directory name
            if (result.containsKey(f.getParent())) {
                result.get(f.getParent()).add(f);
            } else {
                List<File> values = new ArrayList<>();
                values.add(f);
                result.put(f.getParent(), values);
            }
        }
    }


    // 执行cmd
    public ActionResult execute(ActionInfo actionInfo) {
        boolean bFound = false;
        try {
            for (ExecutorUnit it : executorUnits) {
                if(it == null ||it.getInstanceName() == null)
                {
                    logger.error(it.getWorkingPath());
                    continue;
                }
                if (it.getInstanceName().trim().equalsIgnoreCase(actionInfo.getInstanceName().trim())) {
                    TimeRange range = new TimeRange();
                    range.begin = new Date().getTime();
                    analyze.startUPInfo.put(it.getInstanceName(),range);
                    execInstance(it, actionInfo.getAction().trim());
                    curUnit = it;
                    bFound = true;
                }
            }
            if (bFound)
            {
                if (!curUnit.getInstanceName().startsWith("ztev-hhq")) {
                    synchronized (curUnit) {
                        curUnit.wait(2 * 60 * 1000);
                    }
                }
                return   ActionResult.builder().status(true).msg("OK").build();
            }
            else
            {
                return ActionResult.builder().status(false).msg("unknown instance").build();
            }
        } catch (IOException ex) {
            logger.error(ex.getLocalizedMessage());
            return ActionResult.builder().status(false).msg(ex.getLocalizedMessage()).build();
        } catch (InterruptedException e) {
            logger.error(e.getLocalizedMessage());
            return ActionResult.builder().status(false).msg(e.getLocalizedMessage()).build();
        }
    }

    private void execInstance(ExecutorUnit it, String action) throws IOException, InterruptedException {
        //设置通用配置信息 环境变量
        ProcessBuilder pb = new ProcessBuilder(createArgs(action));
        modifyEnv(pb.environment());
        // print pb environment
        pb.environment().forEach((k,v) ->
        {
            logger.info("key={},value={}",k,v);
        });

        pb.directory(new File(it.getWorkingPath()));
        File consoleLog = new File(it.getWorkingPath() + File.separator + "consolelog");
        if (!consoleLog.exists()) {
            consoleLog.mkdir();
        }
        String consoleOutPutLog = consoleLog + File.separator + action + "_output.log";
        String consoleErrorLog = consoleLog + File.separator + action + "_error.log";
        pb.redirectOutput(new File(consoleOutPutLog));
        pb.redirectError(new File(consoleErrorLog));
        Process p = pb.start();
        if (p.waitFor() == 0) {
            logger.info("START " + it.getInstanceName() + "successfully");
        } else {
            logger.info("START " + it.getInstanceName() + "failed");
        }

    }

    //  deleiver env
    private void modifyEnv(Map<String, String> env) {
        env.put("database.ip", genv.getIp() != null ? genv.getIp() : "");
        env.put("database.user", genv.getUserName() != null ? genv.getUserName() : "");
        env.put("database.password", genv.getPassword() != null ? genv.getPassword() : "");

        env.put("dubbo.address", genv.getDubbo_registy() != null ? genv.getDubbo_registy() : "");
        env.put("activemq.broker-url", genv.getMq_borker() != null ? genv.getMq_borker() : "");

        env.put("ztev.ftp.host", genv.getFtpAddress());
        env.put("ztev.ftp.port", genv.getFtpPort());
        env.put("ztev.ftp.user", genv.getFtpUser());
        env.put("ztev.ftp.password", genv.getFtpPassword());
        env.put("ztev.nms.mode", genv.getMode());
    }

    public void setDatabaseConfig(DatabaseConfig databaseConfig) {
        this.genv = databaseConfig;
    }

    public List<InstanceName> getAllInstanceName() {
        List<InstanceName> names = new LinkedList<>();
        executorUnits.forEach(it ->
                {
                    InstanceName name = new InstanceName();
                    name.name = it.getInstanceName();
                    names.add(name);
                }

        );
        return names;
    }

    public void killAll() {
        for (ExecutorUnit unit : executorUnits)
        {
            try {
                execInstance(unit,"stop");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @EventListener
    public  void onClientAppStatusUpdated(ClientApplicationStatusChangedEvent event)
    {
        if (curUnit != null && event.getApplication().getName().equalsIgnoreCase(curUnit.getInstanceName()))
        {
            synchronized (curUnit)
            {
                curUnit.notify();
            }
        }
    }


    public Path downloadLogArchive(String instanceName) throws IOException {
        for (ExecutorUnit unit : executorUnits)
        {
           if( instanceName.equalsIgnoreCase(unit.getInstanceName()))
            {
                Path path  = Paths.get(unit.getWorkingPath());
                Path log = Files.exists(path.resolve("log"))?path.resolve("log"):path.resolve("logs");
                SimpleDateFormat fmt = new SimpleDateFormat("uuuu-MM-dd-HH-mm-ss");
                Path zipPath = path.resolve(instanceName + fmt.format(new Date())+".zip");
                CompressUtils.createZip(log, zipPath);
               return  zipPath;
            }
        }
        return null;
    }

}
