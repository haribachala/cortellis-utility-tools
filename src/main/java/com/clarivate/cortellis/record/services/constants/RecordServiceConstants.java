package com.clarivate.cortellis.record.services.constants;

/**
 * Created by U6015446 on 30-Nov-16.
 */
public class RecordServiceConstants {

    private String environment;
    private String dataSet;
    private String task;
    private String snapShotTime;
    private String protocol;
    private String host;
    private String port;
    private String slash;
    private String colan;
    private String urlPath;

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public String getRecordServerContext() {
        return recordServerContext;
    }

    public void setRecordServerContext(String recordServerContext) {
        this.recordServerContext = recordServerContext;
    }

    public String getRecordLoaderPath() {
        return recordLoaderPath;
    }

    public void setRecordLoaderPath(String recordLoaderPath) {
        this.recordLoaderPath = recordLoaderPath;
    }

    public String getRecordPublishPath() {
        return recordPublishPath;
    }

    public void setRecordPublishPath(String recordPublishPath) {
        this.recordPublishPath = recordPublishPath;
    }

    public String getRecordExtractPath() {
        return recordExtractPath;
    }

    public void setRecordExtractPath(String recordExtractPath) {
        this.recordExtractPath = recordExtractPath;
    }

    public String getRecordHealthPath() {
        return recordHealthPath;
    }

    public void setRecordHealthPath(String recordHealthPath) {
        this.recordHealthPath = recordHealthPath;
    }

    private String recordServerContext;
    private String recordLoaderPath;
    private String recordPublishPath;
    private String recordExtractPath;
    private String recordHealthPath;



    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }
    public String getSlash() {
        return "/";
    }
    public String getColan() {
        return ":";
    }

    public void setColan(String colan) {
        this.colan = colan;
    }
    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getSnapShotTime() {
        return snapShotTime;
    }

    public void setSnapShotTime(String snapShotTime) {
        this.snapShotTime = snapShotTime;
    }
    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setSlash(String slash) {
        this.slash = slash;
    }

}
