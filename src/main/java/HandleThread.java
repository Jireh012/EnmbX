//import com.csvreader.CsvReader;
//import com.csvreader.CsvWriter;
//import com.jcraft.jsch.ChannelSftp;
//import com.jcraft.jsch.SftpException;
//import org.apache.log4j.Logger;
//import util.*;
//
//import java.io.*;
//import java.nio.charset.StandardCharsets;
//import java.text.DecimalFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Properties;
//import java.util.Random;
//import java.util.concurrent.CountDownLatch;
//
//import static util.Const.*;
//import static util.FileUtil.isChartPathExist;
//import static util.Mathematical.txfloat;
//
///**
// * @author last_
// */
//class HandleThread implements Runnable {
//
//    private CountDownLatch threadsSignal;
//    private String source;
//    private String aims;
//    private String type1;
//    private int pdschPrbAssn;
//    private int puschPrbAssn;
//    private int nbrCqi;
//    private int ulmeannl;
//
//    private static Logger logger = Logger.getLogger(HandleThread.class);
//
//    HandleThread(CountDownLatch threadsSignal, String source, String aims, String type1,
//                 int pdschPrbAssn, int puschPrbAssn, int nbrCqi, int ulmeannl) {
//        this.threadsSignal = threadsSignal;
//        this.source = source;
//        this.aims = aims;
//        this.type1 = type1;
//        this.pdschPrbAssn =pdschPrbAssn;
//        this.puschPrbAssn =puschPrbAssn;
//        this.nbrCqi =nbrCqi;
//        this.ulmeannl =ulmeannl;
//    }
//
//    private static void initDataPosition(CsvReader reader) throws IOException {
//        int i = 0;
//        //初始化相关列位置
//        while (i < reader.getValues().length) {
//            switch (reader.get(i)) {
//                case "RRU.PdschPrbAssn":
//                    RRU_PdschPrbAssn_position = i;
//                    break;
//                case "RRU.PdschPrbTot":
//                    RRU_PdschPrbTot_position = i;
//                    break;
//                case "RRU.PuschPrbAssn":
//                    RRU_PuschPrbAssn_position = i;
//                    break;
//                case "RRU.PuschPrbTot":
//                    RRU_PuschPrbTot_position = i;
//                    break;
//                case "PHY.NbrCqi0":
//                    PHY_NbrCqi0_position = i;
//                    break;
//                case "PHY.NbrCqi1":
//                    PHY_NbrCqi1_position = i;
//                    break;
//                case "PHY.NbrCqi2":
//                    PHY_NbrCqi2_position = i;
//                    break;
//                case "PHY.NbrCqi3":
//                    PHY_NbrCqi3_position = i;
//                    break;
//                case "PHY.NbrCqi4":
//                    PHY_NbrCqi4_position = i;
//                    break;
//                case "PHY.NbrCqi5":
//                    PHY_NbrCqi5_position = i;
//                    break;
//                case "PHY.NbrCqi6":
//                    PHY_NbrCqi6_position = i;
//                    break;
//                case "PHY.NbrCqi7":
//                    PHY_NbrCqi7_position = i;
//                    break;
//                case "PHY.NbrCqi8":
//                    PHY_NbrCqi8_position = i;
//                    break;
//                case "PHY.NbrCqi9":
//                    PHY_NbrCqi9_position = i;
//                    break;
//                case "PHY.NbrCqi10":
//                    PHY_NbrCqi10_position = i;
//                    break;
//                case "PHY.NbrCqi11":
//                    PHY_NbrCqi11_position = i;
//                    break;
//                case "PHY.NbrCqi12":
//                    PHY_NbrCqi12_position = i;
//                    break;
//                case "PHY.NbrCqi13":
//                    PHY_NbrCqi13_position = i;
//                    break;
//                case "PHY.NbrCqi14":
//                    PHY_NbrCqi14_position = i;
//                    break;
//                case "PHY.NbrCqi15":
//                    PHY_NbrCqi15_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB0":
//                    PHY_ULMeanNL_PRB0_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB1":
//                    PHY_ULMeanNL_PRB1_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB2":
//                    PHY_ULMeanNL_PRB2_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB3":
//                    PHY_ULMeanNL_PRB3_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB4":
//                    PHY_ULMeanNL_PRB4_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB5":
//                    PHY_ULMeanNL_PRB5_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB6":
//                    PHY_ULMeanNL_PRB6_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB7":
//                    PHY_ULMeanNL_PRB7_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB8":
//                    PHY_ULMeanNL_PRB8_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB9":
//                    PHY_ULMeanNL_PRB9_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB10":
//                    PHY_ULMeanNL_PRB10_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB11":
//                    PHY_ULMeanNL_PRB11_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB12":
//                    PHY_ULMeanNL_PRB12_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB13":
//                    PHY_ULMeanNL_PRB13_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB14":
//                    PHY_ULMeanNL_PRB14_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB15":
//                    PHY_ULMeanNL_PRB15_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB16":
//                    PHY_ULMeanNL_PRB16_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB17":
//                    PHY_ULMeanNL_PRB17_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB18":
//                    PHY_ULMeanNL_PRB18_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB19":
//                    PHY_ULMeanNL_PRB19_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB20":
//                    PHY_ULMeanNL_PRB20_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB21":
//                    PHY_ULMeanNL_PRB21_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB22":
//                    PHY_ULMeanNL_PRB22_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB23":
//                    PHY_ULMeanNL_PRB23_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB24":
//                    PHY_ULMeanNL_PRB24_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB25":
//                    PHY_ULMeanNL_PRB25_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB26":
//                    PHY_ULMeanNL_PRB26_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB27":
//                    PHY_ULMeanNL_PRB27_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB28":
//                    PHY_ULMeanNL_PRB28_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB29":
//                    PHY_ULMeanNL_PRB29_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB30":
//                    PHY_ULMeanNL_PRB30_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB31":
//                    PHY_ULMeanNL_PRB31_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB32":
//                    PHY_ULMeanNL_PRB32_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB33":
//                    PHY_ULMeanNL_PRB33_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB34":
//                    PHY_ULMeanNL_PRB34_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB35":
//                    PHY_ULMeanNL_PRB35_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB36":
//                    PHY_ULMeanNL_PRB36_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB37":
//                    PHY_ULMeanNL_PRB37_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB38":
//                    PHY_ULMeanNL_PRB38_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB39":
//                    PHY_ULMeanNL_PRB39_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB40":
//                    PHY_ULMeanNL_PRB40_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB41":
//                    PHY_ULMeanNL_PRB41_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB42":
//                    PHY_ULMeanNL_PRB42_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB43":
//                    PHY_ULMeanNL_PRB43_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB44":
//                    PHY_ULMeanNL_PRB44_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB45":
//                    PHY_ULMeanNL_PRB45_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB46":
//                    PHY_ULMeanNL_PRB46_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB47":
//                    PHY_ULMeanNL_PRB47_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB48":
//                    PHY_ULMeanNL_PRB48_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB49":
//                    PHY_ULMeanNL_PRB49_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB50":
//                    PHY_ULMeanNL_PRB50_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB51":
//                    PHY_ULMeanNL_PRB51_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB52":
//                    PHY_ULMeanNL_PRB52_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB53":
//                    PHY_ULMeanNL_PRB53_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB54":
//                    PHY_ULMeanNL_PRB54_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB55":
//                    PHY_ULMeanNL_PRB55_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB56":
//                    PHY_ULMeanNL_PRB56_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB57":
//                    PHY_ULMeanNL_PRB57_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB58":
//                    PHY_ULMeanNL_PRB58_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB59":
//                    PHY_ULMeanNL_PRB59_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB60":
//                    PHY_ULMeanNL_PRB60_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB61":
//                    PHY_ULMeanNL_PRB61_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB62":
//                    PHY_ULMeanNL_PRB62_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB63":
//                    PHY_ULMeanNL_PRB63_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB64":
//                    PHY_ULMeanNL_PRB64_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB65":
//                    PHY_ULMeanNL_PRB65_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB66":
//                    PHY_ULMeanNL_PRB66_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB67":
//                    PHY_ULMeanNL_PRB67_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB68":
//                    PHY_ULMeanNL_PRB68_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB69":
//                    PHY_ULMeanNL_PRB69_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB70":
//                    PHY_ULMeanNL_PRB70_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB71":
//                    PHY_ULMeanNL_PRB71_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB72":
//                    PHY_ULMeanNL_PRB72_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB73":
//                    PHY_ULMeanNL_PRB73_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB74":
//                    PHY_ULMeanNL_PRB74_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB75":
//                    PHY_ULMeanNL_PRB75_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB76":
//                    PHY_ULMeanNL_PRB76_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB77":
//                    PHY_ULMeanNL_PRB77_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB78":
//                    PHY_ULMeanNL_PRB78_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB79":
//                    PHY_ULMeanNL_PRB79_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB80":
//                    PHY_ULMeanNL_PRB80_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB81":
//                    PHY_ULMeanNL_PRB81_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB82":
//                    PHY_ULMeanNL_PRB82_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB83":
//                    PHY_ULMeanNL_PRB83_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB84":
//                    PHY_ULMeanNL_PRB84_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB85":
//                    PHY_ULMeanNL_PRB85_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB86":
//                    PHY_ULMeanNL_PRB86_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB87":
//                    PHY_ULMeanNL_PRB87_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB88":
//                    PHY_ULMeanNL_PRB88_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB89":
//                    PHY_ULMeanNL_PRB89_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB90":
//                    PHY_ULMeanNL_PRB90_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB91":
//                    PHY_ULMeanNL_PRB91_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB92":
//                    PHY_ULMeanNL_PRB92_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB93":
//                    PHY_ULMeanNL_PRB93_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB94":
//                    PHY_ULMeanNL_PRB94_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB95":
//                    PHY_ULMeanNL_PRB95_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB96":
//                    PHY_ULMeanNL_PRB96_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB97":
//                    PHY_ULMeanNL_PRB97_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB98":
//                    PHY_ULMeanNL_PRB98_position = i;
//                    break;
//                case "PHY.ULMeanNL.PRB99":
//                    PHY_ULMeanNL_PRB99_position = i;
//                    break;
//                default:
//                    break;
//            }
//            i++;
//        }
//    }
//
//    @Override
//    public void run() {
//        logger.info(Thread.currentThread().getName() + "...开始...");
//        Properties properties = SOURCE_PRO;
//        if(properties.get(source + ".user")==null){
//            logger.warn("SFTP连接参数错误,请检查sourceConfig配置文件!!!");
//            return;
//        }
//        ChannelSftp sftp = SftpUtilM.login(properties.get(source + ".user").toString(),
//                properties.get(source + ".password").toString(), source, 22);
//        if (sftp == null) {
//            logger.warn("SFTP登陆失败，请检查网络和连接参数");
//        } else {
//            logger.info("SFTP登陆成功耗时："+(System.currentTimeMillis()-startTime)/1000+" (秒)");
//            String rndStrFile = UuidUtil.get32UUID();
//            String path = saveFilePath + rndStrFile + File.separator;
//            csvDown(type1, properties, sftp, path, source, aims, pdschPrbAssn, puschPrbAssn, nbrCqi, ulmeannl);
//        }
//        // 线程结束时计数器减1
//        threadsSignal.countDown();//必须等核心处理逻辑处理完成后才可以减1
//        logger.info(Thread.currentThread().getName() + "结束. 还有"
//                + threadsSignal.getCount() + " 个线程");
//    }
//
//    private static synchronized void csvRun(String readerPath, String writePath, String aims,
//                                            int pdschPrbAssn, int puschPrbAssn, int nbrCqi, int ulmeannl) {
//        try {
//            CsvReader reader = new CsvReader(readerPath, '|', StandardCharsets.UTF_8);
//            boolean isFirst = true;
//            String[] stringList;
//            reader.readHeaders();
//            //读取表头 TimeStamp
//            String[] title = reader.getHeaders();
//            CsvWriter writerTemp = new CsvWriter(writePath + "temp.csv", '|', StandardCharsets.UTF_8);
//            writerTemp.setUseTextQualifier(false);
//            writerTemp.writeRecord(title);
//            // 读取每行的内容
//            while (reader.readRecord()) {
//                if (isFirst) {
//                    //读取列头 rmUID
//                    writerTemp.writeRecord(reader.getValues());
//                    isFirst = false;
//                    initDataPosition(reader);
//                }
//                else {
//                    //每一行的值
//                    stringList = reader.getValues();
//                    stringList[2] = "\"" + stringList[2] + "\"";
//                    if (reader.get(Integer.parseInt(Const.aimsType)).equals(aims)) {
//                        try {
//                            if (pdschPrbAssn==1) {
//                                float pdschPrbAssnData = Float.parseFloat(reader.get(RRU_PdschPrbAssn_position));
//                                float pdschPrbTotData = Float.parseFloat(reader.get(RRU_PdschPrbTot_position));
//                                if (pdschPrbAssnData/pdschPrbTotData > 0.5) {
//                                    logger.info("下行指标修正 当前：" + reader.get(2));
//                                    logger.info("下行指标 当前值：" +pdschPrbAssnData);
//                                    int min = 40, max = 50;
//                                    double rd = (double) (min + (int) (Math.random() * ((max - min) + 1))) / 100;
//                                    //下行修改完成
//                                    stringList[RRU_PdschPrbAssn_position] = String.valueOf(
//                                            (int)(rd * pdschPrbTotData / 100)*100);
//                                    logger.info("下行指标 修改后值：" + stringList[RRU_PdschPrbAssn_position]);
//                                }
//                            }
//                            if (puschPrbAssn==1) {
//                                //上行
//                                float puschPrbAssnData = Float.parseFloat(reader.get(RRU_PuschPrbAssn_position));
//                                float puschPrbTot = Float.parseFloat(reader.get(RRU_PuschPrbTot_position));
//                                if (puschPrbAssnData/ puschPrbTot > 0.5) {
//                                    logger.info("上行指标修正 当前" + reader.get(2));
//                                    logger.info("上行指标 当前值" + puschPrbAssnData);
//                                    int min = 40, max = 50;
//                                    double rd = (double) (min + (int) (Math.random() * ((max - min) + 1))) / 100;
//                                    stringList[RRU_PuschPrbAssn_position] = String.valueOf(
//                                            (int)(rd * puschPrbTot / 100)*100);
//                                    logger.info("上行指标 修改后值：" + stringList[RRU_PuschPrbAssn_position]);
//                                }
//                            }
//                            if (nbrCqi==1) {
//                                int sum0To6 = 0, sum7To15 = 0;
//                                int[] intList = new int[7];
//                                for (int i = 0; i <= 15; i++) {
//                                    if (i <= 6) {
//                                        sum0To6 = sum0To6 + Integer.parseInt(reader.get(getPHYNbrCqiposition(i)));
//                                        intList[i] = Integer.parseInt(reader.get(getPHYNbrCqiposition(i)));
//                                    }else{
//                                        sum7To15 = sum7To15 + Integer.parseInt(reader.get(getPHYNbrCqiposition(i)));
//                                    }
//                                }
//                                if ((float) sum0To6 / (sum0To6+sum7To15) > 0.2) {
//                                    logger.info("NbrCqi指标修正 当前" + reader.get(2));
//                                    double n = Math.ceil(sum0To6 - 0.25 * sum7To15);
//                                    int rnd = (int) (new Random().nextDouble() * (sum0To6 - (n)) + (n));
//                                    for (int j = 0; j <= 6; j++) {
//                                        logger.info("NbrCqi指标 修正前：" + stringList[getPHYNbrCqiposition(j)]);
//                                        double bl = (double) intList[j] / sum0To6;
//                                        stringList[getPHYNbrCqiposition(j)] = String.valueOf(Integer.parseInt(
//                                                stringList[getPHYNbrCqiposition(j)]) - (int) (rnd * bl));
//                                        logger.info("NbrCqi指标 修正后：" + stringList[getPHYNbrCqiposition(j)]);
//                                    }
//                                }
//                            }
//                            if (ulmeannl==1) {
//                                for (int i = 0; i <= 99; i++) {
//                                    if (!reader.get(getPHYULMeanNLPRBPosition(i)).isEmpty() && Integer.parseInt(reader.get(getPHYULMeanNLPRBPosition(i))) > -110) {
//                                        logger.info("ULMeanNL指标修正 当前:" + reader.get(2));
//                                        logger.info("ULMeanNL指标 当前:" + reader.get(getPHYULMeanNLPRBPosition(i)));
//                                        int min = 111, max = 118;
//                                        int rd = 0 - (min + (int) (Math.random() * ((max - min) + 1)));
//                                        stringList[getPHYULMeanNLPRBPosition(i)] = String.valueOf(rd);
//                                        logger.info("ULMeanNL指标 修改后:" + stringList[getPHYULMeanNLPRBPosition(i)]);
//                                    }
//                                }
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            logger.warn("aims: " + aims + "\n源数据异常: " + e.getMessage());
//                            logger.warn("异常详情: " + e.getLocalizedMessage());
//                        }
//                    }
//                    writerTemp.writeRecord(stringList);
//                }
//            }
//            reader.close();
//            FileUtil.deleteFile(readerPath);
//            // 关闭Writer
//            writerTemp.close();
//            new File(writePath + "temp.csv").renameTo(new File(writePath));
//        } catch (IOException e) {
//            e.printStackTrace();
//            logger.error("文件流操作异常：" + e.getMessage());
//        }
//    }
//
//    private static synchronized void csvDown(String type1, Properties properties, ChannelSftp sftp, String path, String source, String aims
//            , int pdschPrbAssn, int puschPrbAssn, int nbrCqi, int ulmeannl) {
//        if (FILE_IS_DOWN.get(source + "_" + type1) == null) {
//            String fileName;
//            if ("1".equals(TestModel)) {
//                String str= null;
//                try {
//                    str = SftpUtilM.listFiles(sftp,properties.get(source + ".path") + "/" + TestDirNameYmDH+"/PM-ENB-EUTRANCELL" + type1 + "-"+
//                            properties.get(source + ".id") +"-*-" + TestDirNameYmDH + TestFileNameMMss + "-15.csv.gz").toString();
//                } catch (SftpException e) {
//                    e.printStackTrace();
//                }
//                assert str != null;
//                String verSion = str.substring(str.indexOf("V"), str.indexOf("V") + 6);
//                fileName = "PM-ENB-EUTRANCELL" + type1 + "-" + properties.get(source + ".id") +
//                        "-" + verSion + "-" + TestDirNameYmDH + TestFileNameMMss + "-15.csv";
//                logger.info("测试模式 文件名：" + fileName);
//                isChartPathExist(path);
//                SftpUtilM.download(sftp, properties.get(source + ".path") + "/" + TestDirNameYmDH,
//                        fileName + ".gz", path + fileName + ".gz");
//            }
//            else {
//                String str= null;
//                try {
//                    logger.info("获取："+nowTime+"/PM-ENB-EUTRANCELL" + type1 + "-"+
//                            properties.get(source + ".id") +"-*-" + nowTime + TimeMm + "-15.csv.gz");
//                    str = SftpUtilM.listFiles(sftp,properties.get(source + ".path") + "/" +
//                            nowTime+"/PM-ENB-EUTRANCELL" + type1 + "-"+
//                            properties.get(source + ".id") +"-*-" + nowTime + TimeMm + "-15.csv.gz").toString();
//                    if ("[]".equals(str)){
//                        logger.warn("获取不到当前时间上一时段数据！！！");
//                        return;
//                    }
//                } catch (SftpException e) {
//                    e.printStackTrace();
//                }
//                assert str != null;
//                String verSion = str.substring(str.indexOf("V"), str.indexOf("V") + 6);
//                fileName = "PM-ENB-EUTRANCELL" + type1 + "-" + properties.get(source + ".id") + "-" +
//                        verSion+ "-" + nowTime + TimeMm + "-15.csv";
//                logger.info("正常模式 文件名：" + fileName);
//                isChartPathExist(path);
//                SftpUtilM.download(sftp, properties.get(source + ".path") + "/" + nowTime,
//                        fileName + ".gz", path + fileName + ".gz");
//            }
//            UnCompressFileGZIP.doUncompressFile(path + fileName + ".gz");
//            isChartPathExist(path + "Write" + File.separator);
//            csvRun(path + fileName, path + "Write" + File.separator + fileName,
//                    aims,pdschPrbAssn,puschPrbAssn,nbrCqi,ulmeannl);
//            FILE_IS_DOWN.put(source + "_" + type1, path + "Write" + File.separator + fileName + "_" + fileName + "_" + path+"_"+nowTime);
//        } else {
//            csvRun(FILE_IS_DOWN.get(source + "_" + type1).split("_")[0],
//                    FILE_IS_DOWN.get(source + "_" + type1).split("_")[0],
//                    aims,pdschPrbAssn,puschPrbAssn,nbrCqi,ulmeannl);
//        }
//    }
//}
