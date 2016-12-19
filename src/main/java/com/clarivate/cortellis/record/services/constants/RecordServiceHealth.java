package com.clarivate.cortellis.record.services.constants;

/**
 * Created by U6015446 on 15-Dec-16.
 */
public class RecordServiceHealth {

     String status;

     EmbeddedCassandraHealth embeddedCassandraHealth;
     RecordServerHealth recordServerHealth;
     SnapshotHealth snapshotHealth;
     DiskSpace diskSpace;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public EmbeddedCassandraHealth getEmbeddedCassandraHealth() {
        return embeddedCassandraHealth;
    }

    public void setEmbeddedCassandraHealth(EmbeddedCassandraHealth embeddedCassandraHealth) {
        this.embeddedCassandraHealth = embeddedCassandraHealth;
    }

    public RecordServerHealth getRecordServerHealth() {
        return recordServerHealth;
    }

    public void setRecordServerHealth(RecordServerHealth recordServerHealth) {
        this.recordServerHealth = recordServerHealth;
    }

    public SnapshotHealth getSnapshotHealth() {
        return snapshotHealth;
    }

    public void setSnapshotHealth(SnapshotHealth snapshotHealth) {
        this.snapshotHealth = snapshotHealth;
    }

    public DiskSpace getDiskSpace() {
        return diskSpace;
    }

    public void setDiskSpace(DiskSpace diskSpace) {
        this.diskSpace = diskSpace;
    }
}
