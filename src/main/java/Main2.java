//import com.google.common.util.concurrent.ThreadFactoryBuilder;
//import com.jcraft.jsch.ChannelSftp;
//import com.jcraft.jsch.SftpException;
//import org.apache.log4j.Logger;
//import org.apache.poi.xssf.usermodel.XSSFCell;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import util.PropertiesConfigs;
//import util.SftpUtilM;
//import util.UuidUtil;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Map;
//import java.util.Properties;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.ScheduledThreadPoolExecutor;
//import java.util.concurrent.ThreadFactory;
//
//import static util.Const.*;
//import static util.FileUtil.isChartPathExist;
//
///**
// * @author last_
// */
//public class Main2 {
//
//    private static Logger logger = Logger.getLogger(Main2.class);
//
//    public static void main(String[] args) throws Exception {
//        startTime = System.currentTimeMillis();
//        try {
//            if (args.length == 0) {
//                PropertiesConfigs.loadConf();
//            } else {
//                PropertiesConfigs.loadConf(args[0]);
//                PropertiesConfigs.loadSource(args[1]);
//            }
//        } catch (Exception e) {
//            //读取配置异常
//            e.printStackTrace();
//            logger.error("读取配置异常" + e.getMessage());
//        }
//        if (SOURCE_PRO==null){
//            logger.warn("数据源异常，请检查sourceConfig是否被正确加载");
//            return;
//        }
//
//        if (!"1".equals(TestModel)) {
//           initTimeData();
//        }
//
//        File xlsFile = new File(sourceFilePath);
//        // 获得工作簿
//        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(xlsFile));
//        // 获得工作表
//        XSSFSheet sheet = workbook.getSheetAt(0);
//        int threadNum = sheet.getPhysicalNumberOfRows();
//        CountDownLatch threadSignal1 = new CountDownLatch(threadNum-1);
//        ThreadFactory threadFactory = new ThreadFactoryBuilder()
//                .setNameFormat("enmbx-pool-%d").setDaemon(true).build();
//        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(threadNum-1,
//                 threadFactory);
//        for (int s = 1; s < sheet.getPhysicalNumberOfRows(); s++) {
//            XSSFRow sheetRow = sheet.getRow(s);
//            String source = sheetRow.getCell(0).toString();
//            XSSFCell aims = sheetRow.getCell(1);
//            String type1 = sheetRow.getCell(2).toString();
//            XSSFCell pdschPrbAssn = sheetRow.getCell(3);
//            XSSFCell puschPrbAssn = sheetRow.getCell(4);
//            XSSFCell nbrCqi = sheetRow.getCell(5);
//            XSSFCell ulmeannl = sheetRow.getCell(6);
//
//            if(FILE_IS_DOWN.get(source+"_"+type1)==null){
//                Properties properties = SOURCE_PRO;
//                if(properties.get(source + ".user")==null){
//                    logger.warn("SFTP连接参数错误,请检查sourceConfig配置文件!!!");
//                    return;
//                }
//                ChannelSftp sftp = SftpUtilM.login(properties.get(source + ".user").toString(),
//                        properties.get(source + ".password").toString(), source, 22);
//                String rndStrFile = UuidUtil.get32UUID();
//                String path = saveFilePath + rndStrFile + File.separator;
//                String fileName;
//                if ("1".equals(TestModel)) {
//                    String str= null;
//                    try {
//                        str = SftpUtilM.listFiles(sftp,properties.get(source + ".path") + "/" + TestDirNameYmDH+"/PM-ENB-EUTRANCELL" + type1 + "-"+
//                                properties.get(source + ".id") +"-*-" + TestDirNameYmDH + TestFileNameMMss + "-15.csv.gz").toString();
//                    } catch (SftpException e) {
//                        e.printStackTrace();
//                    }
//                    assert str != null;
//                    String verSion = str.substring(str.indexOf("V"), str.indexOf("V") + 6);
//                    fileName = "PM-ENB-EUTRANCELL" + type1 + "-" + properties.get(source + ".id") +
//                            "-" + verSion + "-" + TestDirNameYmDH + TestFileNameMMss + "-15.csv";
//                    logger.info("测试模式 文件名：" + fileName);
//                    isChartPathExist(path);
//                    SftpUtilM.download(sftp, properties.get(source + ".path") + "/" + TestDirNameYmDH,
//                            fileName + ".gz", path + fileName + ".gz");
//                }
//                else {
//                    String str= null;
//                    try {
//                        logger.info("获取："+nowTime+"/PM-ENB-EUTRANCELL" + type1 + "-"+
//                                properties.get(source + ".id") +"-*-" + nowTime + TimeMm + "-15.csv.gz");
//                        str = SftpUtilM.listFiles(sftp,properties.get(source + ".path") + "/" +
//                                nowTime+"/PM-ENB-EUTRANCELL" + type1 + "-"+
//                                properties.get(source + ".id") +"-*-" + nowTime + TimeMm + "-15.csv.gz").toString();
//                        if ("[]".equals(str)){
//                            logger.warn("获取不到当前时间上一时段数据！！！");
//                            return;
//                        }
//                    } catch (SftpException e) {
//                        e.printStackTrace();
//                    }
//                    assert str != null;
//                    String verSion = str.substring(str.indexOf("V"), str.indexOf("V") + 6);
//                    fileName = "PM-ENB-EUTRANCELL" + type1 + "-" + properties.get(source + ".id") + "-" +
//                            verSion+ "-" + nowTime + TimeMm + "-15.csv";
//                    logger.info("正常模式 文件名：" + fileName);
//                    isChartPathExist(path);
//                    SftpUtilM.download(sftp, properties.get(source + ".path") + "/" + nowTime,
//                            fileName + ".gz", path + fileName + ".gz");
//                }
//                FILE_IS_DOWN.put(source+"_"+type1,path + fileName + ".gz");
//            }
//
////            Runnable task = new HandleThread(threadSignal1, source.toString(), aims.toString(), type1.toString(),
////                    (int)pdschPrbAssn.getNumericCellValue(),(int)puschPrbAssn.getNumericCellValue(),
////                    (int)nbrCqi.getNumericCellValue(),(int)ulmeannl.getNumericCellValue());
////            // 执行
////            executorService.execute(task);
//        }
//
//        // 等待所有子线程执行完
//        threadSignal1.await();
//        //固定线程池执行完成后 将释放掉资源 退出主进程
//        //并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
//        executorService.shutdown();
//
//        CountDownLatch threadSignal2 = new CountDownLatch(FILE_IS_DOWN.size());
//        ThreadFactory threadFactory2 = new ThreadFactoryBuilder()
//                .setNameFormat("enmbx-upload-pool-%d").setDaemon(true).build();
//        ScheduledExecutorService executorService2 = new ScheduledThreadPoolExecutor(FILE_IS_DOWN.size(),
//                threadFactory2);
//        if (FILE_IS_DOWN.size()!=0){
//            logger.info("=============开始执行上传线程===============");
//            for (Map.Entry<String, String> entry : FILE_IS_DOWN.entrySet()) {
//                String source  = entry.getKey().split("_")[0];
//                String path =entry.getValue().split("_")[0];
//                String fileName =entry.getValue().split("_")[1];
//                String savePath =entry.getValue().split("_")[2];
//                String time =entry.getValue().split("_")[3];
//
//                Runnable task = new UploadThread(threadSignal2, source, path,
//                        fileName,savePath,time);
//                executorService2.execute(task);
//            }
//        }else {
//            logger.warn("=========上传列表为空，跳出上传===========");
//        }
//        // 等待所有子线程执行完
//        threadSignal2.await();
//        //固定线程池执行完成后 将释放掉资源 退出主进程
//        //并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
//        executorService2.shutdown();
//
//        //退出所有连接
//        SftpUtilM.logoutList();
//        // do work end
//        //退出主进程
//        logger.info("==========="+Thread.currentThread().getName()+"结束===========");
//        logger.info("本次耗时："+(System.currentTimeMillis() - startTime)/1000+" (秒)");
//    }
//
//    private static void initTimeData(){
//        SimpleDateFormat mmFormat = new SimpleDateFormat("mm");
//        int nowTimeMm = Integer.parseInt(mmFormat.format(new Date()));
//        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHH");
//        if (nowTimeMm>7 && nowTimeMm <= 22) {
//            TimeMm = "4500";
//            nowTime=timeFormat.format(new Date(System.currentTimeMillis()-3600*1000));
//        } else if (nowTimeMm>22 &&nowTimeMm <= 37) {
//            TimeMm = "0000";
//            nowTime=timeFormat.format(new Date(System.currentTimeMillis()));
//        } else if (nowTimeMm>37 &&nowTimeMm <= 52) {
//            TimeMm = "1500";
//            nowTime=timeFormat.format(new Date(System.currentTimeMillis()));
//        } else if (nowTimeMm>52 || nowTimeMm <= 7) {
//            TimeMm = "3000";
//            if (nowTimeMm <= 7){
//                nowTime=timeFormat.format(new Date(System.currentTimeMillis()-3600*1000));
//            }
//            if (nowTimeMm>52){
//                nowTime=timeFormat.format(new Date(System.currentTimeMillis()));
//            }
//        }
//    }
//}
