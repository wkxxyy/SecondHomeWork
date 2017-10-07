package com.hrbnu.cloudsim.util;
import com.hrbnu.cloudsim.Task;

public class CalculatePower {

    public static final double P20=78.5;
    public static final double P30=83;
    public static final double P40=85;
    public static final double P50=88;
    public static final double P60=93;
    public static final double P70=102;
    public static final double P80=109;
    public static final double P90=122;
    public static final double P100=136;

    private static double EVIM;//最后的功耗

    public static double getCalculatePower(Task task){

        int usedUtilization=task.getNeedUtilization();//获取任务的使用率，简化后面判断操作

        if (usedUtilization>=1&&usedUtilization<20){
            EVIM=task.getPeriod()*P20;
        }else if (usedUtilization>=21&&usedUtilization<30){
            EVIM=task.getPeriod()*P30;
        }else if (usedUtilization>=31&&usedUtilization<40){
            EVIM=task.getPeriod()*P40;
        }else if (usedUtilization>=41&&usedUtilization<50){
            EVIM=task.getPeriod()*P50;
        }else if (usedUtilization>=51&&usedUtilization<60){
            EVIM=task.getPeriod()*P60;
        }else if (usedUtilization>=61&&usedUtilization<70){
            EVIM=task.getPeriod()*P70;
        }else if (usedUtilization>=71&&usedUtilization<80){
            EVIM=task.getPeriod()*P80;
        }else if (usedUtilization>=81&&usedUtilization<90){
            EVIM=task.getPeriod()*P90;
        }else if (usedUtilization>=91&&usedUtilization<100){
            EVIM=task.getPeriod()*P100;
        }

        return EVIM;
    }

}
