package com.hrbnu.cloudsim;

import java.util.Date;

public class Vmware {

    private int id;
    private int totalUtilization=100;//总利用率
    private int usedUtilization=0;//已使用利用率
    private int residualUtilization=100;//剩余利用率

    private int taskId;//运行的任务ID
    //private static int usedTime;//任务已经运行的时间
    //private static int residualTime;//任务还需要多少时间

    private boolean isRunning=false;//虚拟机是否字运行的标识

    public Vmware(int id){
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public int getTotalUtilization() {
        return totalUtilization;
    }

    public void setTotalUtilization(int totalUtilization) {
        this.totalUtilization = totalUtilization;
    }

    public int getUsedUtilization() {
        return usedUtilization;
    }

    public void setUsedUtilization(int usedUtilization) {
        this.usedUtilization = usedUtilization;
    }

    public int getResidualUtilization() {
        return residualUtilization;
    }

    public void setResidualUtilization(int residualUtilization) {
        this.residualUtilization = residualUtilization;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
