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
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import static util.Const.*;
import static util.FileUtil.isChartPathExist;
import static util.Mathematical.StringToInt;

/**
 * @author last_
 */
public class Source1Thread implements Runnable {

    private CountDownLatch threadsSignal;
    private Map.Entry<String, String> sourceData;
    private Map<String, Integer> downField;

    private Logger logger = Logger.getLogger(Source1Thread.class);

    Source1Thread(CountDownLatch threadsSignal, Map.Entry<String, String> sourceData, Map<String, Integer> downField) {
        this.threadsSignal = threadsSignal;
        this.sourceData = sourceData;
        this.downField = downField;
    }

    private void initDataPosition(CsvReader reader) throws IOException {
        int i = 0;
        //初始化相关列位置
        while (i < reader.getValues().length) {
            for (final Map.Entry<String, Integer> map : downField.entrySet()) {
                if (reader.get(i).equals(map.getKey())) {
                    map.setValue(i);
                }
            }
            i++;
        }
    }

    @Override
    public void run() {
        logger.info(Thread.currentThread().getName() + "...开始...");
        Properties properties = SOURCE_PRO;
        String source = sourceData.getKey();
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
            String type = sourceData.getValue();
            logger.info("SFTP登陆成功耗时：" + (System.currentTimeMillis() - startTime) / 1000 + " (秒)");
            String path;
            String fileName;
            String str = null;
            long tttt = System.currentTimeMillis();
            if ("1".equals(TestModel)) {
                logger.info("获取：" + TestDirNameYmDH + "/PM-ENB-EUTRANCELLTDD" + "-" +
                        properties.get(source + ".id") + "-*-" + TestDirNameYmDH + TestFileNameMMss + "-15.csv.gz");
                for (int t1 = 1; t1 <= ForCount; t1++) {
                    try {
                        str = SftpUtilM.listFiles(sftp, properties.get(source + ".path") + "/" +
                                TestDirNameYmDH + "/PM-ENB-EUTRANCELLTDD" + "-" +
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
                    fileName = "PM-ENB-EUTRANCELLTDD" + "-" + properties.get(source + ".id") +
                            "-" + verSion + "-" + TestDirNameYmDH + TestFileNameMMss + "-15.csv";
                    logger.info("测试模式 文件名：" + fileName);
                    path = saveFilePath + TestDirNameYmDH + TestFileNameMMss + "_" + source + "_" + tttt + File.separator;
                    isChartPathExist(path);
                    SftpUtilM.download(sftp, properties.get(source + ".path") + "/" + TestDirNameYmDH,
                            fileName + ".gz", path + fileName + ".gz");
                }
            } else {
                logger.info("获取：" + nowTime + "/PM-ENB-EUTRANCELLTDD" + "-" +
                        properties.get(source + ".id") + "-*-" + nowTime + TimeMm + "-15.csv.gz");
                for (int t1 = 1; t1 <= ForCount; t1++) {
                    try {
                        str = SftpUtilM.listFiles(sftp, properties.get(source + ".path") + "/" +
                                nowTime + "/PM-ENB-EUTRANCELLTDD" + "-" +
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
                    fileName = "PM-ENB-EUTRANCELLTDD" + "-" + properties.get(source + ".id") + "-" +
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
                logger.info("=======" + source + "上传成功=======");
                long dalen;
                if ("1".equals(TestModel)) {
                    dalen = SftpUtilM.listFiles1(sftp, properties.get(source + ".path") + "/" +
                            TestDirNameYmDH + "/PM-ENB-EUTRANCELLTDD" + "-" +
                            properties.get(source + ".id") + "-*-" + TestDirNameYmDH + TestFileNameMMss + "-15.csv.gz").getSize();
                    logger.info("PM-ENB-EUTRANCELLTDD" + "-" +
                            properties.get(source + ".id") + "-*-" + TestDirNameYmDH + TestFileNameMMss + "-15.csv.gz  文件大小为：" + dalen);
                } else {
                    dalen = SftpUtilM.listFiles1(sftp, properties.get(source + ".path") + "/" +
                            nowTime + "/PM-ENB-EUTRANCELLTDD" + "-" +
                            properties.get(source + ".id") + "-*-" + nowTime + TimeMm + "-15.csv.gz").getSize();
                    logger.info("PM-ENB-EUTRANCELLTDD" + "-" +
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

    private void csvRun(String readerPath, String writePath, String value) {
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
            int count = 0;
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
                    for (final Map.Entry<String, Integer> map : downField.entrySet()) {
                        if (map.getKey().split("$")[1].equals(value) && map.getValue()==count) {


                            logger.info(reader.get(count));
                        }
                    }


//                    if (value.toString().contains(reader.get(Integer.parseInt(Const.aimsType)) + "￥")) {
//                        for (String li : value) {
//                            if (reader.get(Integer.parseInt(Const.aimsType)).equals(li.split("￥")[2])) {
//                                int pdschPrbAssn = Integer.parseInt(li.split("￥")[3]);
//                                int puschPrbAssn = Integer.parseInt(li.split("￥")[4]);
//                                int nbrCqi = Integer.parseInt(li.split("￥")[5]);
//                                int ulmeannl = Integer.parseInt(li.split("￥")[6]);
//                                try {
//                                    if (pdschPrbAssn == 1) {
//                                        int pdschPrbAssnData = StringToInt(reader.get(private int RRU_PdschPrbAssn_position));
//                                        int pdschPrbTotData = StringToInt(reader.get(private int RRU_PdschPrbTot_position));
//                                        if ((float) pdschPrbAssnData / pdschPrbTotData > 0.5) {
//                                            logger.info("下行指标修正 当前：" + reader.get(2));
//                                            logger.info("下行指标 当前值：" + pdschPrbAssnData);
//                                            int min = 40, max = 50;
//                                            double rd = (double) (min + (int) (Math.random() * ((max - min) + 1))) / 100;
//                                            //下行修改完成
//                                            stringList[private int RRU_PdschPrbAssn_position] = String.valueOf(
//                                                    (int) (rd * pdschPrbAssnData / 100) * 100);
//                                            logger.info("下行指标 修改后值：" + stringList[private int RRU_PdschPrbAssn_position]);
//                                        }
//                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                    logger.warn("aims: " + reader.get(2) + "\n源数据异常: " + e.getMessage());
//                                }
//
//                                try {
//                                    if (puschPrbAssn == 1) {
//                                        //上行
//                                        int puschPrbAssnData = StringToInt(reader.get(private int RRU_PuschPrbAssn_position));
//                                        int puschPrbTot = StringToInt(reader.get(private int RRU_PuschPrbTot_position));
//                                        if ((float) puschPrbAssnData / puschPrbTot > 0.5) {
//                                            logger.info("上行指标修正 当前" + reader.get(2));
//                                            logger.info("上行指标 当前值" + puschPrbAssnData);
//                                            int min = 40, max = 50;
//                                            double rd = (double) (min + (int) (Math.random() * ((max - min) + 1))) / 100;
//                                            stringList[private int RRU_PuschPrbAssn_position] = String.valueOf(
//                                                    (int) (rd * puschPrbAssnData / 100) * 100);
//                                            logger.info("上行指标 修改后值：" + stringList[private int RRU_PuschPrbAssn_position]);
//                                        }
//                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                    logger.warn("aims: " + reader.get(2) + "\n源数据异常: " + e.getMessage());
//                                }
//
//                                try {
//                                    if (nbrCqi == 1) {
//                                        int sum0To6 = 0, sum7To15 = 0;
//                                        int[] intList = new int[7];
//                                        for (int i = 0; i <= 15; i++) {
//                                            if (i <= 6) {
//                                                sum0To6 = sum0To6 + Integer.parseInt(reader.get(getPHYNbrCqiposition(i)));
//                                                intList[i] = Integer.parseInt(reader.get(getPHYNbrCqiposition(i)));
//                                            } else {
//                                                sum7To15 = sum7To15 + Integer.parseInt(reader.get(getPHYNbrCqiposition(i)));
//                                            }
//                                        }
//                                        if ((float) sum0To6 / (sum0To6 + sum7To15) > 0.2) {
//                                            logger.info("NbrCqi指标修正 当前" + reader.get(2));
//                                            double n = Math.ceil(sum0To6 - 0.25 * sum7To15);
//                                            int rnd = (int) (new Random().nextDouble() * (sum0To6 - (n)) + (n));
//                                            for (int j = 0; j <= 6; j++) {
//                                                logger.info("NbrCqi指标" + j + " 修正前：" + stringList[getPHYNbrCqiposition(j)]);
//                                                double bl = (double) intList[j] / sum0To6;
//                                                stringList[getPHYNbrCqiposition(j)] = String.valueOf(Integer.parseInt(
//                                                        stringList[getPHYNbrCqiposition(j)]) - (int) (rnd * bl));
//                                                logger.info("NbrCqi指标" + j + " 修正后：" + stringList[getPHYNbrCqiposition(j)]);
//                                            }
//                                        }
//
//                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                    logger.warn("aims: " + reader.get(2) + "\n源数据异常: " + e.getMessage());
//                                }
//
//                                try {
//                                    if (ulmeannl == 1) {
//                                        for (int i = 0; i <= 99; i++) {
//                                            if (!reader.get(getPHYULMeanNLPRBPosition(i)).isEmpty() && Integer.parseInt(reader.get(getPHYULMeanNLPRBPosition(i))) > -110) {
//                                                logger.info("ULMeanNL指标修正 当前:" + reader.get(2));
//                                                logger.info("ULMeanNL指标 当前:" + reader.get(getPHYULMeanNLPRBPosition(i)));
//                                                int min1 = 114, max1 = 118;
//                                                int rd1 = 0 - (min1 + (int) (Math.random() * ((max1 - min1) + 1)));
//                                                stringList[getPHYULMeanNLPRBPosition(i)] = String.valueOf(rd1);
//                                                logger.info("ULMeanNL指标 修改后:" + stringList[getPHYULMeanNLPRBPosition(i)]);
//                                            }
//                                        }
//                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                    logger.warn("aims: " + reader.get(2) + "\n源数据异常: " + e.getMessage());
//                                }
//                                break;
//                            }
//                        }
//                    }

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

}
