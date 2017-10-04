import sun.misc.VM;

import java.util.*;

public class Start {
    private static List<Task> taskList=new ArrayList<Task>();
    private static List<Vmware> vmwareList=new ArrayList<Vmware>();

    public static void main(String[] args) {

        Random random=new Random();

        for (int i=1;i<5;i++){//初始化任务集合
            Task task=new Task(i);
            task.setPeriod((int)(Math.random()*50));//运行时间设置为50秒以内
            task.setNeedUtilization(random.nextInt(10)+1);//设置任务需要使用的利用率，在1到10之间
            taskList.add(task);
        }


        Collections.sort(taskList);//按照任务到达顺序进行集合排序


        for (int j=1;j<5;j++){//初始化虚拟机集合
            Vmware vmware=new Vmware(j);
            vmwareList.add(vmware);
        }

        startDispatch();//开始调度

    }

    public static void startDispatch(){

        Task currentTask;//当前正在调度的任务
        Vmware currentVmware;//当前正在调度的虚拟机

        for(int i=0;i<taskList.size();i++){
            currentTask=taskList.get(i);


            while (i<taskList.size()){

                currentVmware=vmwareList.get(new Random().nextInt(vmwareList.size()));//随机挑选一个虚拟机，随机挑选

                if (!currentVmware.isRunning()){//如果当前虚拟机没有运行程序，就继续

                    if (currentVmware.getResidualUtilization()>currentTask.getNeedUtilization()){//当前虚拟机剩余的利用率大于任务所需要的利用率，就继续
                        currentTask.setStartTime(getMinutes());//设置任务开始时间,就是当前的时间的分钟数
                        currentVmware.runTask(currentTask);//开始运行
                        break;
                    }else {
                        break;
                    }

                }else {
                    break;

            }

            }


        }
        System.out.println();
        System.out.println("调度完成！！！！！！！！！！！！！！！！");



    }

    public static int getMinutes(){//获取当前时间分钟数
        Date date=new Date();
        return date.getMinutes();
    }
}
