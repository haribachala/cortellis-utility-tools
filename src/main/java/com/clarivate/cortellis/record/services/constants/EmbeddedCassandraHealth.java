package com.clarivate.cortellis.record.services.constants;

import java.util.Date;

/**
 * Created by U6015446 on 15-Dec-16.
 */
public class EmbeddedCassandraHealth {
    private String status;

    public String getStatus() { return this.status; }

    public void setStatus(String status) { this.status = status; }

    private String StorageType;

    public String getStorageType() { return this.StorageType; }

    public void setStorageType(String StorageType) { this.StorageType = StorageType; }

    private String cassandraVersion;

    public String getCassandraVersion() { return this.cassandraVersion; }

    public void setCassandraVersion(String cassandraVersion) { this.cassandraVersion = cassandraVersion; }

    private String availableKeySpaces;

    public String getAvailableKeySpaces() { return this.availableKeySpaces; }

    public void setAvailableKeySpaces(String availableKeySpaces) { this.availableKeySpaces = availableKeySpaces; }

    private String availableColumnFamilies;

    public String getAvailableColumnFamilies() { return this.availableColumnFamilies; }

    public void setAvailableColumnFamilies(String availableColumnFamilies) { this.availableColumnFamilies = availableColumnFamilies; }

    private Date latestSnapshot;

    public Date getLatestSnapshot() { return this.latestSnapshot; }

    public void setLatestSnapshot(Date latestSnapshot) { this.latestSnapshot = latestSnapshot; }

    private String availableSnapshots;

    public String getAvailableSnapshots() { return this.availableSnapshots; }

    public void setAvailableSnapshots(String availableSnapshots) { this.availableSnapshots = availableSnapshots; }

    private int recordCount;

    public int getRecordCount() { return this.recordCount; }

    public void setRecordCount(int recordCount) { this.recordCount = recordCount; }

}
