package com.ztev.module;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Created by Administrator on 2017/4/11.
 */
@Configuration
@PropertySource("file:sharedConfig.properties")

public class DatabaseConfig {
    @Value("${database.ip}")
    private String ip;

    @Value("${database.user}")
    private String userName;

    @Value("${database.password}")
    private String password;


    @Value("${ftp.host}")
    private String ftpAddress;

    @Value("${ftp.port}")
    private String ftpPort;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @Value("${ztev.nms.mode}")
    private String mode;

    public String getFtpAddress() {
        return ftpAddress;
    }

    public void setFtpAddress(String ftpAddress) {
        this.ftpAddress = ftpAddress;
    }

    public String getFtpPort() {
        return ftpPort;
    }

    public void setFtpPort(String ftpPort) {
        this.ftpPort = ftpPort;
    }

    public String getFtpUser() {
        return ftpUser;
    }

    public void setFtpUser(String ftpUser) {
        this.ftpUser = ftpUser;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

    @Value("${ftp.user}")
    private String ftpUser;

    @Value(("${ftp.password}"))
    private String ftpPassword;

    public String getDubbo_registy() {
        return dubbo_registy;
    }

    public void setDubbo_registy(String dubbo_registy) {
        this.dubbo_registy = dubbo_registy;
    }

    public String getMq_borker() {
        return mq_borker;
    }

    public void setMq_borker(String mq_borker) {
        this.mq_borker = mq_borker;
    }

    @Value("${dubbo.registry.address}")
    private String dubbo_registy;

    @Value("${spring.activemq.broker-url}")
    private String mq_borker;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    //To resolve ${} in @Value
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
