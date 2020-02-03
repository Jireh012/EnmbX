package model.omc_to_sql;

import com.csvreader.CsvReader;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import org.apache.log4j.Logger;

import util.SftpUtilM;
import util.UnCompressFileGZIP;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.CountDownLatch;

import static util.Const.*;
import static util.FileUtil.isChartPathExist;

/**
 * @author last_
 */
public class Source1Thread implements Runnable {

    private CountDownLatch threadsSignal;
    private Map.Entry<String, String> sourceData;
    private Map<String, Integer> downField;
    private int c1 = 0;
    private StringBuffer prefix;
    private String type;

    private Logger logger = Logger.getLogger(Source1Thread.class);

    public Source1Thread(CountDownLatch threadsSignal, Map.Entry<String, String> sourceData, Map<String, Integer> downField) {
        this.threadsSignal = threadsSignal;
        this.sourceData = sourceData;
        this.downField = downField;
    }

    private void initDataPosition(CsvReader reader) {
        int i = 0;
        //初始化相关列位置
        while (true) {
            try {
                if (!(i < reader.getValues().length)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (final Map.Entry<String, Integer> map : downField.entrySet()) {
                try {
                    if (reader.get(i).equals(map.getKey().split("￥")[0]) && map.getKey().split("￥")[1].equals(type)) {
                        map.setValue(i);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            i++;
        }
    }

    @Override
    public void run() {
        logger.info(Thread.currentThread().getName() + "...开始...");
        Properties properties = SOURCE_PRO;
        String source = sourceData.getKey().split("￥")[0];
        String type1 = sourceData.getKey().split("￥")[1];
        if (properties.get(source + ".user") == null) {
            logger.warn("SFTP连接参数错误,请检查sourceConfig配置文件!!!");
            threadsSignal.countDown();
            logger.info(Thread.currentThread().getName() + "结束. 还有"
                    + threadsSignal.getCount() + " 个线程");
            return;
        }
        ChannelSftp sftp = SftpUtilM.login(properties.get(source + ".user").toString(),
                properties.get(source + ".password").toString(), source, 22);
        if (sftp == null) {
            logger.warn("SFTP登陆失败，请检查网络和连接参数");
        } else {
            logger.info("SFTP登陆成功耗时：" + (System.currentTimeMillis() - startTime) / 1000 + " (秒)");
            String path;
            String fileName;
            String str = null;
            long tttt = System.currentTimeMillis();
            if ("1".equals(TestModel)) {
                logger.info("获取：" + TestDirNameYmDH + "/PM-ENB-EUTRANCELL" + type1 + "-" +
                        properties.get(source + ".id") + "-*-" + TestDirNameYmDH + TestFileNameMMss + "-15.csv.gz");
                for (int t1 = 1; t1 <= ForCount; t1++) {
                    try {
                        str = SftpUtilM.listFiles(sftp, properties.get(source + ".path") + "/" +
                                TestDirNameYmDH + "/PM-ENB-EUTRANCELL" + type1 + "-" +
                                properties.get(source + ".id") + "-*-" + TestDirNameYmDH + TestFileNameMMss + "-15.csv.gz").toString();
                    } catch (SftpException e) {
                        e.printStackTrace();
                        logger.error("SFTP操作异常：" + e.getMessage());
                    }
                    if (!"[]".equals(str) && str != null) {
                        break;
                    } else {
                        logger.info(Thread.currentThread().getName() + "获取不到该时段文件，" +
                                SleepTime + " (秒) 后继续查询");
                        try {
                            Thread.sleep(SleepTime * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            logger.error("线程异常：" + e.getMessage());
                        }
                    }
                }
                if ("[]".equals(str)) {
                    threadsSignal.countDown();//必须等核心处理逻辑处理完成后才可以减1
                    logger.info(Thread.currentThread().getName() + "结束. 还有"
                            + threadsSignal.getCount() + " 个线程");
                    return;
                } else {
                    assert str != null;
                    String verSion = str.substring(str.indexOf("V"), str.indexOf("V") + 6);
                    fileName = "PM-ENB-EUTRANCELL"+type1 + "-" + properties.get(source + ".id") +
                            "-" + verSion + "-" + TestDirNameYmDH + TestFileNameMMss + "-15.csv";
                    logger.info("测试模式 文件名：" + fileName);
                    path = saveFilePath + TestDirNameYmDH + TestFileNameMMss + "_" + source + "_" + tttt + File.separator;
                    isChartPathExist(path);
                    SftpUtilM.download(sftp, properties.get(source + ".path") + "/" + TestDirNameYmDH,
                            fileName + ".gz", path + fileName + ".gz");
                }
            } else {
                logger.info("获取：" + nowTime + "/PM-ENB-EUTRANCELL" +type1 + "-" +
                        properties.get(source + ".id") + "-*-" + nowTime + TimeMm + "-15.csv.gz");
                for (int t1 = 1; t1 <= ForCount; t1++) {
                    try {
                        str = SftpUtilM.listFiles(sftp, properties.get(source + ".path") + "/" +
                                nowTime + "/PM-ENB-EUTRANCELL" +type1 + "-" +
                                properties.get(source + ".id") + "-*-" + nowTime + TimeMm + "-15.csv.gz").toString();
                    } catch (SftpException e) {
                        e.printStackTrace();
                        logger.error("SFTP操作异常：" + e.getMessage());
                    }
                    if (!"[]".equals(str) && str != null) {
                        break;
                    } else {
                        logger.info(Thread.currentThread().getName() + "获取不到该时段文件，" +
                                SleepTime + " (秒) 后继续查询");
                        try {
                            Thread.sleep(SleepTime * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            logger.error("线程异常：" + e.getMessage());
                        }
                    }

                }
                if ("[]".equals(str)) {
                    threadsSignal.countDown();//必须等核心处理逻辑处理完成后才可以减1
                    logger.info(Thread.currentThread().getName() + "结束. 还有"
                            + threadsSignal.getCount() + " 个线程");
                    return;
                } else {
                    assert str != null;
                    String verSion = str.substring(str.indexOf("V"), str.indexOf("V") + 6);
                    fileName = "PM-ENB-EUTRANCELL" +type1 + "-" + properties.get(source + ".id") + "-" +
                            verSion + "-" + nowTime + TimeMm + "-15.csv";
                    logger.info("正常模式 文件名：" + fileName);
                    path = saveFilePath + nowTime + TimeMm + "_" + source + "_" + tttt + File.separator;
                    isChartPathExist(path);
                    logger.info("========开始下载文件========");
                    SftpUtilM.download(sftp, properties.get(source + ".path") + "/" + nowTime,
                            fileName + ".gz", path + fileName + ".gz");
                }
            }
            UnCompressFileGZIP.doUncompressFile(path + fileName + ".gz");
            logger.info("=======开始处理文件=======");
            csvRun(path + fileName, sourceData.getValue());
            logger.info("文件处理耗时：" + (System.currentTimeMillis() - startTime) / 1000 + " (秒)");

        }

        // 线程结束时计数器减1
        threadsSignal.countDown();//必须等核心处理逻辑处理完成后才可以减1
        logger.info(Thread.currentThread().getName() + "结束. 还有"
                + threadsSignal.getCount() + " 个线程 耗时：" +
                (System.currentTimeMillis() - startTime) / 1000 + " (秒)");
    }

    private void csvRun(String readerPath, String value) {
        try {
            type = value;
            logger.info("Download长度:" + downField.size());
            CsvReader reader = new CsvReader(readerPath, '|', StandardCharsets.UTF_8);
            boolean isFirst = true;
            String[] sL;
            reader.readHeaders();
            int count = 0;
            long begin = 0L;
            prefix = new StringBuffer();
            // 保存sql后缀
            StringBuffer suffix = new StringBuffer();

            if (value.equals("1.0")) {
                // sql前缀
                prefix.append("INSERT INTO [dbo].[LTEtemp_小区性能指标_15分钟_FTPtemp] ([rmUID],[UserLabel],[StartTime]");
            } else {
                // sql前缀
                prefix.append("INSERT INTO [dbo].[LTEtemp_小区性能指标_15分钟_FTPtemp_5G] ([rmUID],[UserLabel],[StartTime]");
            }
            boolean on1=true;
            // 读取每行的内容
            while (reader.readRecord()) {
                if (isFirst) {
                    isFirst = false;
                    //读取列头 rmUID
                    logger.info("=======初始化列头位置=======");
                    initDataPosition(reader);
                } else {
                    //每一行的值
                    sL = reader.getValues();
                    // 构建SQL后缀
                    suffix.append("('"+sL[0]+"'"+ ",'"+sL[2]+"'"+",'"+sL[3]+"'" );

                    logger.info(sL[0]);

                    //读取每个单元格
                    for (final Map.Entry<String, Integer> map : downField.entrySet()) {
                        for (int i = 0; i <= sL.length; i++) {
                            if (map.getValue() == i) {
                                if (begin == 0L) {
                                    begin = new Date().getTime();
                                }
                                suffix.append(",'").append(sL[i].isEmpty()?"0":sL[i]).append("'");
                                if (on1){
                                    prefix.append(",[").append(map.getKey().split("￥")[0]).append("]");
                                }
                                logger.info(map.getKey().split("￥")[0]+" Value :"+sL[i]);
                            }
                        }
                    }
                    on1=false;
                    count++;
                    suffix.append("),");
                }
            }
            prefix.append(") VALUES");

            try {
                String sql = prefix + suffix.substring(0, suffix.length() - 1);
                logger.info("Sql语句："+sql);
                Statement stm =  conn.createStatement();
                stm.addBatch(sql);
                stm.executeBatch();

                // 提交事务
                conn.commit();
                // 头等连接
                stm.close();
            } catch (SQLException e) {
                logger.error("数据库错误：" + e.getMessage());
            }

            // 结束时间
            Long end = new Date().getTime();
            // 耗时
            logger.info(count + "条数据插入花费时间 : " + (end - begin) / 1000 + " s" + "  插入完成");
            reader.close();
        } catch (IOException e) {
            logger.error("文件流操作异常：" + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
