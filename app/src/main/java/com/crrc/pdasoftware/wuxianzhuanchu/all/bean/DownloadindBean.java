package com.crrc.pdasoftware.wuxianzhuanchu.all.bean;

public class DownloadindBean {
    public int current;//下载进度
    public String fileName;//当前下载的文件名称
    public String downloaded;//文件是否已下载完成

    public synchronized int getCurrent() {
        return current;
    }

    public String getFileName() {
        return fileName;
    }

    public synchronized String getDownloaded() {
        return downloaded;
    }

    public synchronized void setDownloaded(String downloaded) {
        this.downloaded = downloaded;
    }

    public synchronized void setCurrent(int current) {
        this.current = current;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
