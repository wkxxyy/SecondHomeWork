package com.hrbnu.cloudsim;

import com.hrbnu.cloudsim.util.CalculatePower;

import java.util.*;

public class Start {
    private static double minClock;//记录的最小任务完成时间
    private static List<Task> taskList=new ArrayList<Task>();
    private static List<Vmware> vmwareList=new ArrayList<Vmware>();
    private static int time=0;//计数时间

    private static double EVIM;//消耗的能耗

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    time++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Random random=new Random();
/*
    自动初始化任务集合

 */
       /* for (int i=1;i<6;i++){//初始化任务集合
            Task task=new Task(i);
            task.setPeriod((int)(Math.random()*10));//运行时间设置为50秒以内
            task.setNeedUtilization(random.nextInt(69)+1);//设置任务需要使用的利用率，在1到70之间
            taskList.add(task);
        }*/

        {//如果手动输入任务，就需要在调度里面去除设置start时间和finish时间的操作
            Task task1=new Task(1);
            task1.setArrivalTime(0);
            task1.setStartTime(2);
            task1.setPeriod(50);
            task1.setFinishTime(52);
            task1.setNeedUtilization(25);
            taskList.add(task1);

            Task task2=new Task(2);
            task2.setArrivalTime(4);
            task2.setStartTime(7);
            task2.setPeriod(40);
            task2.setFinishTime(47);
            task2.setNeedUtilization(35);
            taskList.add(task2);

            Task task3=new Task(3);
            task3.setArrivalTime(10);
            task3.setStartTime(15);
            task3.setPeriod(54);
            task3.setFinishTime(69);
            task3.setNeedUtilization(30);
            taskList.add(task3);

        }

        minClock=taskList.get(0).getFinishTime();//将第一个任务的完成时间设置为minClock

        Collections.sort(taskList);//按照任务到达顺序进行集合排序


        for (int j=1;j<5;j++){//初始化虚拟机集合
            Vmware vmware=new Vmware(j);
            vmwareList.add(vmware);
        }

        startDispatchWithETC();//开始调度
        //startDispatchWithETSC();

        EVIM= CalculatePower.getCalculatePower(taskList,vmwareList);
        System.out.println("能耗为："+EVIM);

        System.out.println();
        System.out.println("调度完成！！！！！！！！！！！！！！！！");
    }

    public static void startDispatchWithETC() {

        Task currentTask;//当前正在调度的任务
        Vmware currentVmware;//当前正在调度的虚拟机

        for (int i = 0; i < taskList.size(); i++) {
            boolean tag=false;//判断任务有没有被运行

            currentTask = taskList.get(i);

            for (int j = 0; j < vmwareList.size(); j++) {
                currentVmware = vmwareList.get(j);//从第一个开始挑虚拟机

                if (currentTask.getNeedUtilization()+currentVmware.getUsedUtilization()<=70){//如果当前虚拟机的利用率加上来的任务的利用率小于70，那就可以运行，就进行绑定
                    //手动输入任务需要取出下面的语句
                    //currentTask.setStartTime(getMinutes()+currentTask.getArrivalTime());//设置任务开始时间,就是当前的时间的分钟数
                    //currentTask.setFinishTime(currentTask.getStartTime()+currentTask.getPeriod());
                    if (currentTask.getFinishTime()<minClock){
                        minClock=currentTask.getFinishTime();
                    }//寻找最小的完成时间
                    BindTasksToVm.RunTaskWithFifo(currentTask,currentVmware);//开始运行
                    tag=true;
                    break;//这个任务运行了，那就开始给下个任务找虚拟机

                }else {
                    continue;//这个任务没有运行，所以就继续查询下个虚拟机满不满足条件

                }

            }
            /*
                如果所有虚拟机都没运行，就找最小利用率的
             */
            if (!tag){
                double minUtli=vmwareList.get(0).getUsedUtilization();//把第一个虚拟机的利用率设为初始值
                int index=0;//用来技术那个虚拟机的利用率最小
                for (int k=0;k<vmwareList.size();k++){
                    if (minUtli<vmwareList.get(k).getUsedUtilization()){
                        minUtli=vmwareList.get(k).getUsedUtilization();
                        index=k;
                    }
                }
                //手动输入任务需要取出下面的语句
                //currentTask.setStartTime(getMinutes()+currentTask.getArrivalTime());//设置任务开始时间,就是当前的时间的分钟数
                //currentTask.setFinishTime(currentTask.getStartTime()+currentTask.getPeriod());
                if (currentTask.getFinishTime()<minClock){
                    minClock=currentTask.getFinishTime();
                }//寻找最小的完成时间
                BindTasksToVm.RunTaskWithFifo(currentTask,vmwareList.get(index));
            }
        }
    }

    public static void startDispatchWithETSC() {

        Task currentTask;//当前正在调度的任务
        Vmware currentVmware;//当前正在调度的虚拟机

        for (int i = 0; i < taskList.size(); i++) {
            boolean tag=false;//用来标记是否任务在空虚拟机上运行了

            currentTask = taskList.get(i);

            for (int j = 0; j < vmwareList.size(); j++) {
                currentVmware = vmwareList.get(j);//从第一个开始挑虚拟机
                if (currentVmware.getMinClock()>time) {//最小完成时间大于当前的时间，那么虚拟机就是在运行的
                    continue;
                } else {
                    //手动输入任务需要取出下面的语句
                    //currentTask.setStartTime(getMinutes()+currentTask.getArrivalTime());//设置任务开始时间,就是当前的时间的分钟数
                    //currentTask.setFinishTime(currentTask.getStartTime()+currentTask.getPeriod());
                    if (currentTask.getFinishTime()<minClock){
                        minClock=currentTask.getFinishTime();
                    }//寻找最小的完成时间
                    BindTasksToVm.RunTaskWithFifo(currentTask,currentVmware);
                    tag=true;
                    break;
                }
            }
            /*
                如果tag为false就是虚拟机都满了，就开始下面的找最小利用率的开始执行

             */
            if (!tag){
                double minUtli=vmwareList.get(0).getUsedUtilization();//把第一个虚拟机的利用率设为初始值
                int index=0;//用来技术那个虚拟机最小
                for (int k=0;k<vmwareList.size();k++){
                    if (minUtli<vmwareList.get(k).getUsedUtilization()){
                        minUtli=vmwareList.get(k).getUsedUtilization();
                        index=k;
                    }
                }
                //手动输入任务需要取出下面的语句
                //currentTask.setStartTime(getMinutes()+currentTask.getArrivalTime());//设置任务开始时间,就是当前的时间的分钟数
                //currentTask.setFinishTime(currentTask.getStartTime()+currentTask.getPeriod());
                if (currentTask.getFinishTime()<minClock){
                    minClock=currentTask.getFinishTime();
                }//寻找最小的完成时间
                BindTasksToVm.RunTaskWithFifo(currentTask,vmwareList.get(index));
            }





        }
    }


    public static int getMinutes(){//获取当前时间分钟数
        return time;
    }

}
