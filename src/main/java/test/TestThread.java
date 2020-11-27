package test;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import org.apache.log4j.Logger;
import util.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import static util.Const.*;
import static util.FileUtil.isChartPathExist;
import static util.Mathematical.StringToInt;

/**
 * @author last_
 */
public class TestThread implements Runnable {

    private CountDownLatch threadsSignal;
    private Integer number;

    private Logger logger = Logger.getLogger(TestThread.class);

    public TestThread(CountDownLatch threadsSignal, Integer number) {
        this.threadsSignal = threadsSignal;
        this.number = number;
    }

    @Override
    public void run() {
        logger.info(Thread.currentThread().getName() + "...开始...");

        for (int i=1;i<=7;i++){
            System.out.println(i);
            if (i==3){
                exit();
                return;
            }
        }

        System.out.println("run");
        // 线程结束时计数器减1
        threadsSignal.countDown();//必须等核心处理逻辑处理完成后才可以减1
        logger.info(Thread.currentThread().getName() + "结束. 还有"
                + threadsSignal.getCount() + " 个线程 耗时：" +
                (System.currentTimeMillis() - startTime) / 1000 + " (秒)");
    }

    public void exit(){
        // 线程结束时计数器减1
        threadsSignal.countDown();//必须等核心处理逻辑处理完成后才可以减1
        logger.info(Thread.currentThread().getName() + "结束. 还有"
                + threadsSignal.getCount() + " 个线程 耗时：" +
                (System.currentTimeMillis() - startTime) / 1000 + " (秒)");
    }
}
