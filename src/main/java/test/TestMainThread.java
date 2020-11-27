package test;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import model.omc_data_modify.ConnEstabThread;
import model.omc_data_modify.CoverEstabThread;
import model.omc_data_modify.SourceLteThread;
import model.omc_data_modify.SourceThread;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import util.MD4;
import util.PropertiesConfigs;
import util.SftpUtilM;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

import static util.Const.*;

public class TestMainThread {
    public static void main(String[] args) throws Exception {
        startTime=System.currentTimeMillis();
        System.out.println("===========主程序开始===========");

        int threadNum = 10;
        CountDownLatch threadSignal1 = new CountDownLatch(threadNum);
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("test-pool-%d").setDaemon(true).build();
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(threadNum,
                threadFactory);

        for (int i=1;i<=threadNum;i++) {
            Runnable task = new TestThread(threadSignal1, i);
            // 执行
            executorService.execute(task);
        }


        // 等待所有子线程执行完
        threadSignal1.await();

        //固定线程池执行完成后 将释放掉资源 退出主进程
        //并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
        executorService.shutdown();



        // do work end
        //退出主进程
        System.out.println("===========主程序结束===========");
        System.out.println("本次耗时：" + (System.currentTimeMillis() - startTime) / 1000 + " (秒)");
    }
}
