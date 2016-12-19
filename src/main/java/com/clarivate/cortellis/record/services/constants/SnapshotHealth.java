package com.clarivate.cortellis.record.services.constants;

/**
 * Created by U6015446 on 15-Dec-16.
 */
public class SnapshotHealth {

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDataSet() {
        return DataSet;
    }

    public void setDataSet(String dataSet) {
        DataSet = dataSet;
    }

    String description;
    String status;
    String DataSet;
}
