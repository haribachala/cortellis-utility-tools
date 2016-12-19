package com.clarivate.cortellis.record.services.constants;

/**
 * Created by U6015446 on 15-Dec-16.
 */
public class RecordServerHealth {
    String status;
    String ActiveMq;
    String FreeMemory;
    String AllocatedMemory;
    String MaxMemory;
    String TotalFreeMemory;
    String DataHealth;
    String DataHealth_Error;
    String TimeTaken;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActiveMq() {
        return ActiveMq;
    }

    public void setActiveMq(String activeMq) {
        ActiveMq = activeMq;
    }

    public String getFreeMemory() {
        return FreeMemory;
    }

    public void setFreeMemory(String freeMemory) {
        FreeMemory = freeMemory;
    }

    public String getAllocatedMemory() {
        return AllocatedMemory;
    }

    public void setAllocatedMemory(String allocatedMemory) {
        AllocatedMemory = allocatedMemory;
    }

    public String getMaxMemory() {
        return MaxMemory;
    }

    public void setMaxMemory(String maxMemory) {
        MaxMemory = maxMemory;
    }

    public String getTotalFreeMemory() {
        return TotalFreeMemory;
    }

    public void setTotalFreeMemory(String totalFreeMemory) {
        TotalFreeMemory = totalFreeMemory;
    }

    public String getDataHealth() {
        return DataHealth;
    }

    public void setDataHealth(String dataHealth) {
        DataHealth = dataHealth;
    }

    public String getDataHealth_Error() {
        return DataHealth_Error;
    }

    public void setDataHealth_Error(String dataHealth_Error) {
        DataHealth_Error = dataHealth_Error;
    }

    public String getTimeTaken() {
        return TimeTaken;
    }

    public void setTimeTaken(String timeTaken) {
        TimeTaken = timeTaken;
    }
}
