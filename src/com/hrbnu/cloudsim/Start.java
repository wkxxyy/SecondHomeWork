package com.hrbnu.cloudsim;

import com.hrbnu.cloudsim.util.CalculatePower;
import com.hrbnu.cloudsim.util.PossionDis;
import com.hrbnu.cloudsim.util.getOrderList;
import sun.misc.VM;

import java.io.*;
import java.util.*;

public class Start {



    private static double minClock;//记录的最小任务完成时间
    private static List<Task> taskList=new ArrayList<Task>();
    private static List<Vmware> vmwareList=new ArrayList<Vmware>();
    private static int time=0;//计数时间

    private static double EVIM;//消耗的能耗

    private static int TaskNum=8;



    public static void main(String[] args) {

        List<Task> firstExList;//第一步执行
        List<Task> secondExList;//第二步执行

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
        Random random = new Random();
   /*
   需要固定给定时间的值


    */

        File fileDir = new File("E:/object");
        deleteFile(fileDir);


        int[] PT = new int[TaskNum];//{10,50,20,56,32,44,42,86};//构建运行时间
        int[] UT = new int[TaskNum];//{22,15,54,32,40,24,8,11};//构建利用率
        int[] AT = new int[TaskNum];//{0,1,4,2,5,6,1,7};//到达时间服从泊松分布
        int[] ST = new int[TaskNum];//{2,4,3,7,6,5,10,8};//开始时间

        for (int i = 0; i < TaskNum; i++) {
            PT[i] = random.nextInt(10) + 1;//先获取1000个任务到达时间1-10
        }

        for (int i = 0; i < TaskNum; i++) {
            UT[i] = random.nextInt(40) + 1;//先获取1000个任务到达时间1-10
        }

        for (int i = 0; i < TaskNum; i++) {
            AT[i] = PossionDis.getPossionVariable(i);
        }

        for (int i = 0; i < TaskNum; i++) {
            ST[i] = random.nextInt(10) + AT[i];
        }

 /*
    自动初始化任务集合

    */
        for (int i = 0; i <TaskNum; i++) {//初始化任务集合
            Task task = new Task(i);
            //task.setPeriod((int)(Math.random()*10));//运行时间设置为10秒以内
            task.setPeriod(PT[i]);//设置固定的任务运行时间
            //task.setNeedUtilization(random.nextInt(69)+1);//设置任务需要使用的利用率，在1到70之间
            task.setNeedUtilization(UT[i]);//固定的利用率
            task.setArrivalTime(AT[i]);//固定的到达时间
            task.setStartTime(ST[i]);
            task.setFinishTime(ST[i]+PT[i]);

            taskList.add(task);
        }


      /*  {
            Task task1 = new Task(1);
            task1.setArrivalTime(0);
            task1.setStartTime(2);
            task1.setPeriod(50);
            task1.setFinishTime(52);
            task1.setNeedUtilization(25);
            taskList.add(task1);

            Task task2 = new Task(2);
            task2.setArrivalTime(4);
            task2.setStartTime(7);
            task2.setPeriod(40);
            task2.setFinishTime(47);
            task2.setNeedUtilization(35);
            taskList.add(task2);

            Task task3 = new Task(3);
            task3.setArrivalTime(10);
            task3.setStartTime(15);
            task3.setPeriod(54);
            task3.setFinishTime(69);
            task3.setNeedUtilization(30);
            taskList.add(task3);
        }
*/

        minClock = taskList.get(0).getFinishTime();//将第一个任务的完成时间设置为minClock

        //Collections.sort(taskList);//按照任务到达顺序进行集合排序


        for (int j = 0; j <5; j++) {//初始化虚拟机集合
            Vmware vmware = new Vmware(j);
            vmwareList.add(vmware);
        }


        File fileTask = new File("E:/object/fileTask.txt");
        File fileVmware = new File("E:/object/fileVmware.txt");
        if (!fileTask.exists()) {
            fileTask.getParentFile().mkdir();
        }

        if (!fileTask.exists()) {
            try {
                fileTask.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!fileVmware.exists()) {
            try {
                fileVmware.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileOutputStream fosTa = null;
        FileOutputStream fosVm = null;

        try {
            fosTa = new FileOutputStream(fileTask);
            ObjectOutputStream oos = new ObjectOutputStream(fosTa);
            oos.writeObject(taskList);
            oos.flush();
            oos.close();
            fosTa.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fosVm = new FileOutputStream(fileVmware);
            ObjectOutputStream oos1 = new ObjectOutputStream(fosVm);
            oos1.writeObject(vmwareList);
            oos1.flush();
            oos1.close();
            fosVm.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }





/*
    计算各个的健康度


 */
        for (int i = 0; i < taskList.size(); i++) {

            List<Task> sameAt = new ArrayList<Task>();
            int maxPT;
            int maxUT;
            sameAt.add(taskList.get(i));
            maxPT = taskList.get(i).getPeriod();
            maxUT = taskList.get(i).getNeedUtilization();

            for (int fi = 0; fi < taskList.size(); fi++) {
                if (taskList.get(fi).getArrivalTime() == taskList.get(i).getArrivalTime()) {
                    sameAt.add(taskList.get(fi));
                    if (taskList.get(fi).getPeriod() > maxPT) {
                        maxPT = taskList.get(fi).getPeriod();
                    }
                    if (taskList.get(fi).getNeedUtilization() > maxUT) {
                        maxUT = taskList.get(fi).getNeedUtilization();
                    }
                }

            }

            for (int j = 0; j < sameAt.size(); j++) {
                sameAt.get(j).setFitness(0.5 * (sameAt.get(j).getPeriod()) / maxPT + (1 - 0.5) * (sameAt.get(j).getNeedUtilization()) / maxUT);
            }


        }

        //Collections.sort(taskList);//按照健康度排序
        Collections.sort(taskList);//按照到达排序



        startDispatchWithETC();//开始调度
        EVIM = CalculatePower.getCalculatePower(taskList, vmwareList, "ETC");
        System.out.println("ETC能耗为：" + EVIM);



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
                    //手动输入任务需要取消下面的语句
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


    private static void deleteFile(File file) {
        if (file.exists()) {//判断文件是否存在
            if (file.isFile()) {//判断是否是文件
                file.delete();//删除文件
            } else if (file.isDirectory()) {//否则如果它是一个目录
                File[] files = file.listFiles();//声明目录下所有的文件 files[];
                for (int i = 0;i < files.length;i ++) {//遍历目录下所有的文件
                    deleteFile(files[i]);//把每个文件用这个方法进行迭代
                }
                file.delete();//删除文件夹
            }
        } else {
            System.out.println("所删除的文件不存在");
        }
    }

}
