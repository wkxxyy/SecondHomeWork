import java.util.Date;

public class Vmware {
    public static final double P20=78.5;
    public static final double P30=83;
    public static final double P40=85;
    public static final double P50=88;
    public static final double P60=93;
    public static final double P70=102;
    public static final double P80=109;
    public static final double P90=122;
    public static final double P100=136;


    private double EVIM;//最后的功耗




    private int id;
    private int totalUtilization=100;//总利用率
    private int usedUtilization=0;//已使用利用率
    private int residualUtilization=100;//剩余利用率

    private static int taskId;//运行的任务ID
    //private static int usedTime;//任务已经运行的时间
    //private static int residualTime;//任务还需要多少时间

    private boolean isRunning=false;//虚拟机是否字运行的标识

    public Vmware(int id){
        this.id=id;
    }

    public void runTask(Task task){

        residualUtilization=totalUtilization-task.getNeedUtilization();//设置虚拟机的剩余利用率
        isRunning=true;//虚拟机设置为正在运行状态
        task.setRunning(true);//设置任务正在运行

        usedUtilization=task.getNeedUtilization();//设置已经使用的利用率
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

        taskId=task.getId();//设置虚拟机当前运行的程序id

        System.out.println("*********************************************");
        System.out.println(taskId+"号任务正在运行，请稍后。。。。。。。");
        System.out.println(taskId+"号任务运行在"+id+"号虚拟机上");
        System.out.println(taskId+"号任务的到达时间为："+task.getArrivalTime());
        System.out.println(taskId+"号任务开始时间为："+task.getStartTime());
        System.out.println(taskId+"号任务需要的时间为："+task.getPeriod());
        System.out.println(taskId+"号任务需要的利用率为："+task.getNeedUtilization());
        System.out.println("-------------------------------------");

        for (int time=0;time<task.getPeriod();time++){
            System.out.println(taskId+"号任务剩余运行时间为："+(task.getPeriod()-time));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
        task.setFinishTime(new Date().getMinutes());//这是任务结束时间
        task.setFinish(true);//任务设置为完成状态
        isRunning=false;//虚拟机设置为没有在运行状态
        System.out.println("-------------------------------------");
        System.out.println(taskId+"号任务运行完毕，运行结束时间为："+task.getFinishTime());
        System.out.println(taskId+"号任务在"+id+"号虚拟机上消耗的能耗为："+EVIM);
        System.out.println("*********************************************");
        System.out.println();


    }

    public int getId() {
        return id;
    }

    public int getTotalUtilization() {
        return totalUtilization;
    }

    public int getUsedUtilization() {
        return usedUtilization;
    }

    public int getResidualUtilization() {
        return residualUtilization;
    }

    public int getTaskId() {
        return taskId;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
