package model.omc_data_modify;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import org.apache.log4j.Logger;
import util.CompressFileGZIP;
import util.Const;
import util.SftpUtilM;
import util.UnCompressFileGZIP;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import static util.Const.*;
import static util.FileUtil.isChartPathExist;

/**
 * @author last_
 */
public class CoverEstabThread implements Runnable {

    private CountDownLatch threadsSignal;
    private Map.Entry<String, List<String>> sourceData;

    private Logger logger = Logger.getLogger(CoverEstabThread.class);

    public CoverEstabThread(CountDownLatch threadsSignal, Map.Entry<String, List<String>> sourceData) {
        this.threadsSignal = threadsSignal;
        this.sourceData = sourceData;
    }

    private void initDataPosition(CsvReader reader) throws IOException {
        int i = 0;
        //初始化相关列位置
        while (i < reader.getValues().length) {
            switch (reader.get(i)) {
                case "NBRRC.SuccConnEstabCL":
                    NBRRC_SuccConnEstabCL_position = i;
                    break;
                case "NBRRC.AttConnEstabCL":
                    NBRRC_AttConnEstabCL_position = i;
                    break;
                default:
                    break;
            }
            i++;
        }
    }

    @Override
    public void run() {
        logger.info(Thread.currentThread().getName() + "...开始...");
        Properties properties = SOURCE_PRO;
        String source = sourceData.getKey().split("￥")[0];
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
            String type1 = sourceData.getKey().split("￥")[1];
            logger.info("SFTP登陆成功耗时：" + (System.currentTimeMillis() - startTime) / 1000 + " (秒)");
            String path;
            String fileName;
            String str = null;
            long tttt = System.currentTimeMillis();
            if ("1".equals(TestModel)) {
                logger.info("获取：" + TestDirNameYmDH + "/PM-NBIoT-CELLCOVERAGELEVELNB-" +
                        properties.get(source + ".id") + "-*-" + TestDirNameYmDH + TestFileNameMMss + "-15.csv.gz");
                for (int t1 = 1; t1 <= ForCount; t1++) {
                    try {
                        str = SftpUtilM.listFiles(sftp, properties.get(source + ".path") + "/" +
                                TestDirNameYmDH + "/PM-NBIoT-CELLCOVERAGELEVELNB-" +
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
                if ("[]".equals(str)|| str == null) {
                    threadsSignal.countDown();//必须等核心处理逻辑处理完成后才可以减1
                    logger.info(Thread.currentThread().getName() + "结束. 还有"
                            + threadsSignal.getCount() + " 个线程");
                    return;
                } else {
                    assert str != null;
                    String verSion = str.substring(str.indexOf("-V")+1, str.indexOf("-V") + 7);
                    fileName = "PM-NBIoT-CELLCOVERAGELEVELNB-" + properties.get(source + ".id") +
                            "-" + verSion + "-" + TestDirNameYmDH + TestFileNameMMss + "-15.csv";
                    logger.info("测试模式 文件名：" + fileName);
                    path = saveFilePath + TestDirNameYmDH + TestFileNameMMss + "_" + source + "_" + type1 + tttt + File.separator;
                    isChartPathExist(path);
                    try {
                        SftpUtilM.download(sftp, properties.get(source + ".path") + "/" + TestDirNameYmDH,
                                fileName + ".gz", path + fileName + ".gz");
                    }catch (Exception e){
                        logger.error("下载失败源文件问题");
                    }

                }
            } else {
                logger.info("获取：" + nowTime + "/PM-NBIoT-CELLCOVERAGELEVELNB-" +
                        properties.get(source + ".id") + "-*-" + nowTime + TimeMm + "-15.csv.gz");
                for (int t1 = 1; t1 <= ForCount; t1++) {
                    try {
                        str = SftpUtilM.listFiles(sftp, properties.get(source + ".path") + "/" +
                                nowTime + "/PM-NBIoT-CELLCOVERAGELEVELNB-" +
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
                if ("[]".equals(str)|| str == null) {
                    threadsSignal.countDown();//必须等核心处理逻辑处理完成后才可以减1
                    logger.info(Thread.currentThread().getName() + "结束. 还有"
                            + threadsSignal.getCount() + " 个线程");
                    return;
                } else {
                    assert str != null;
                    String verSion = str.substring(str.indexOf("-V")+1, str.indexOf("-V") + 7);
                    fileName = "PM-NBIoT-CELLCOVERAGELEVELNB-" + properties.get(source + ".id") + "-" +
                            verSion + "-" + nowTime + TimeMm + "-15.csv";
                    logger.info("正常模式 文件名：" + fileName);
                    path = saveFilePath + nowTime + TimeMm + "_" + source + "_" + type1 + tttt + File.separator;
                    isChartPathExist(path);
                    logger.info("========开始下载文件========");
                    SftpUtilM.download(sftp, properties.get(source + ".path") + "/" + nowTime,
                            fileName + ".gz", path + fileName + ".gz");
                    logger.info("========开始删除文件========");
                    SftpUtilM.delete(sftp, properties.get(source + ".path") + "/" + nowTime, fileName + ".gz");
                }
            }
            UnCompressFileGZIP.doUncompressFile(path + fileName + ".gz");
            isChartPathExist(path + "Write" + File.separator);
            File y1 = new File(path + fileName + ".gz");
            logger.info(fileName + ".gz " + "原文件大小：" + y1.length());
            csvRun(path + fileName, path + "Write" + File.separator + fileName,
                    sourceData.getValue());
            logger.info("文件处理耗时：" + (System.currentTimeMillis() - startTime) / 1000 + " (秒)");

            CompressFileGZIP.doCompressFile(path + "Write" + File.separator + fileName);
            File file = new File(path + "Write" + File.separator + fileName + ".gz");
            logger.info(fileName + ".gz " + "文件大小：" + file.length());
            try {
                InputStream is = new FileInputStream(file);
                if ("1".equals(TestModel)) {
                    SftpUtilM.upload(sftp, "/", properties.get(source + ".path") + "/" +
                            TestDirNameYmDH, fileName + ".gz", is);
                } else {
                    SftpUtilM.upload(sftp, "/", properties.get(source + ".path") + "/" +
                            nowTime, fileName + ".gz", is);
                }
                logger.info("=======" + source + type1 + "COVER上传成功=======");
                long dalen;
                if ("1".equals(TestModel)) {
                    dalen = SftpUtilM.listFiles1(sftp, properties.get(source + ".path") + "/" +
                            TestDirNameYmDH + "/PM-NBIoT-CELLCOVERAGELEVELNB-" +
                            properties.get(source + ".id") + "-*-" + TestDirNameYmDH + TestFileNameMMss + "-15.csv.gz").getSize();
                    logger.info("PM-NBIoT-CELLCOVERAGELEVELNB-" +
                            properties.get(source + ".id") + "-*-" + TestDirNameYmDH + TestFileNameMMss + "-15.csv.gz  文件大小为：" + dalen);
                } else {
                    dalen = SftpUtilM.listFiles1(sftp, properties.get(source + ".path") + "/" +
                            nowTime + "/PM-NBIoT-CELLCOVERAGELEVELNB-" +
                            properties.get(source + ".id") + "-*-" + nowTime + TimeMm + "-15.csv.gz").getSize();
                    logger.info("PM-NBIoT-CELLCOVERAGELEVELNB-" +
                            properties.get(source + ".id") + "-*-" + nowTime + TimeMm + "-15.csv.gz  文件大小为：" + dalen);
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("文件操作异常：" + e.getMessage());
            } catch (SftpException e) {
                e.printStackTrace();
                logger.error("SFTP操作异常：" + e.getMessage());
            }
        }

        // 线程结束时计数器减1
        threadsSignal.countDown();//必须等核心处理逻辑处理完成后才可以减1
        logger.info(Thread.currentThread().getName() + "结束. 还有"
                + threadsSignal.getCount() + " 个线程 耗时：" +
                (System.currentTimeMillis() - startTime) / 1000 + " (秒)");
    }

    private void csvRun(String readerPath, String writePath, List<String> value) {
        try {
            CsvReader reader = new CsvReader(readerPath, '|', StandardCharsets.UTF_8);
            boolean isFirst = true;
            String[] stringList;
            reader.readHeaders();
            //读取表头 TimeStamp
            String[] title = reader.getHeaders();
            CsvWriter writerTemp = new CsvWriter(writePath, '|', StandardCharsets.UTF_8);
            writerTemp.setUseTextQualifier(false);
            writerTemp.writeRecord(title);
            // 读取每行的内容
            while (reader.readRecord()) {
                if (isFirst) {
                    isFirst = false;
                    //读取列头 rmUID
                    writerTemp.writeRecord(reader.getValues());
                    initDataPosition(reader);
                } else {
                    //每一行的值
                    stringList = reader.getValues();
                    stringList[2] = "\"" + stringList[2] + "\"";
                    if (value.toString().contains(reader.get(Integer.parseInt(Const.aimsType)).substring(0,reader.get(Integer.parseInt(Const.aimsType)).length()-17))){
                        for (String li : value) {
                            if (reader.get(Integer.parseInt(Const.aimsType)).equals(li.split("￥")[2]+"-COVERAGE_LEVEL_0")) {
                                logger.info(reader.get(Integer.parseInt(Const.aimsType)));
                                logger.info("修改前SuccCL0：" + stringList[NBRRC_SuccConnEstabCL_position]);
                                logger.info("修改前AttCL0：" + stringList[NBRRC_AttConnEstabCL_position]);
                                stringList[NBRRC_SuccConnEstabCL_position] = stringList[NBRRC_AttConnEstabCL_position];
                                logger.info("修改后SuccCL0：" + stringList[NBRRC_SuccConnEstabCL_position]);
                                break;
                            }
                            if (reader.get(Integer.parseInt(Const.aimsType)).equals(li.split("￥")[2]+"-COVERAGE_LEVEL_1")) {
                                logger.info(reader.get(Integer.parseInt(Const.aimsType)));
                                logger.info("修改前SuccCL1：" + stringList[NBRRC_SuccConnEstabCL_position]);
                                logger.info("修改前AttCL1：" + stringList[NBRRC_AttConnEstabCL_position]);
                                stringList[NBRRC_SuccConnEstabCL_position] = stringList[NBRRC_AttConnEstabCL_position];
                                logger.info("修改后SuccCL1：" + stringList[NBRRC_SuccConnEstabCL_position]);
                                break;
                            }
                            if (reader.get(Integer.parseInt(Const.aimsType)).equals(li.split("￥")[2]+"-COVERAGE_LEVEL_2")) {
                                logger.info(reader.get(Integer.parseInt(Const.aimsType)));
                                logger.info("修改前SuccCL2：" + stringList[NBRRC_SuccConnEstabCL_position]);
                                logger.info("修改前AttCL2：" + stringList[NBRRC_AttConnEstabCL_position]);
                                stringList[NBRRC_SuccConnEstabCL_position] = stringList[NBRRC_AttConnEstabCL_position];
                                logger.info("修改后SuccCL2：" + stringList[NBRRC_SuccConnEstabCL_position]);
                                break;
                            }
                        }
                    }
                    writerTemp.writeRecord(stringList);
                }
            }
            reader.close();
            // 关闭Writer
            writerTemp.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("文件流操作异常：" + e.getMessage());
        }
    }

    private int NBRRC_SuccConnEstabCL_position = 0;
    private int NBRRC_AttConnEstabCL_position = 0;
}
