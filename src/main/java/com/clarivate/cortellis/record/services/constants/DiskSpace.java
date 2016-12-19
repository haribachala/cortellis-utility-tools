package com.clarivate.cortellis.record.services.constants;

/**
 * Created by U6015446 on 15-Dec-16.
 */
public class DiskSpace {
    String status;
    String total;
    String free;
    String threshold;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getFree() {
        return free;
    }

    public void setFree(String free) {
        this.free = free;
    }

    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }
}
