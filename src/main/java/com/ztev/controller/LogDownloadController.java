package com.ztev.controller;

import com.ztev.service.ProcessesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by ${xiaozb} on 2017/9/6.
 *
 * @copyright by ztev
 */
@Controller
@RequestMapping("ztev")
public class LogDownloadController
{
    @Autowired
    ProcessesService ps;

    @RequestMapping(value="/instance/{instanceName}/log", method = RequestMethod.GET)
    public ResponseEntity<byte[]> retriveDownloadUrl(@PathVariable String instanceName)
    {
        try {
            Path file = ps.downloadLogArchive(instanceName);
            if (file != null)
            {
                byte[] bytes =Files.readAllBytes(file);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType("application/zip; charset=utf-8"));
                headers.setContentDispositionFormData(file.getFileName().toString(), file.getFileName().toString());
                return new ResponseEntity<byte[]>(bytes,headers,HttpStatus.OK);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
