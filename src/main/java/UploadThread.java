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
//class UploadThread implements Runnable {
//
//    private CountDownLatch threadsSignal;
//    private String source;
//    private String path;
//    private String fileName;
//    private String time;
//
//    private static Logger logger = Logger.getLogger(UploadThread.class);
//
//    public UploadThread(CountDownLatch threadsSignal, String source, String path,
//                        String fileName,String savePath,String time) {
//        this.threadsSignal = threadsSignal;
//        this.source = source;
//        this.path = path;
//        this.fileName = fileName;
//        this.time=time;
//    }
//
//    @Override
//    public void run() {
//        logger.info(Thread.currentThread().getName() + "开始...");
//        Properties properties = SOURCE_PRO;
//        CompressFileGZIP.doCompressFile(path);
//        File file = new File(path + ".gz");
//        try (InputStream is = new FileInputStream(file)) {
//            ChannelSftp sftp = SftpUtilM.login(properties.get(source + ".user").toString(), properties.get(source + ".password").toString(), source, 22);
//            if ("1".equals(TestModel)){
//                SftpUtilM.upload(sftp,"/", properties.get(source + ".path") + "/" + TestDirNameYmDH, fileName + ".gz", is);
//            }else {
//                SftpUtilM.upload(sftp,"/", properties.get(source + ".path") + "/" + time, fileName + ".gz", is);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            logger.error("文件操作异常："+e.getMessage());
//        } catch (SftpException e) {
//            e.printStackTrace();
//            logger.error("SFTP操作异常："+e.getMessage());
//        }
//
//        if(FileDelSwitch==1){
//            FileUtil.delFile(new File(saveFilePath));
//        }
//
//        // 线程结束时计数器减1
//        threadsSignal.countDown();//必须等核心处理逻辑处理完成后才可以减1
//        logger.info(Thread.currentThread().getName() + "结束. 还有"
//                + threadsSignal.getCount() + " 个线程");
//    }
//}
