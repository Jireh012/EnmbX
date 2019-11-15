import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.jcraft.jsch.SftpException;
import util.*;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.GZIPInputStream;

import static util.Const.*;
import static util.FileUtil.isChartPathExist;
import static util.Mathematical.txfloat;

public class Test {
    private static String getFileNameForDate() {
        SimpleDateFormat mmFormat = new SimpleDateFormat("mm");
        int mm = Integer.parseInt(mmFormat.format(new Date()));
        String mmStr = "";
        if (mm < 15) {
            mmStr = "00";
        } else if (mm < 30) {
            mmStr = "15";
        } else if (mm < 45) {
            mmStr = "30";
        } else if (mm < 60) {
            mmStr = "45";
        }
        return mmStr + "00";
    }

    public static void main(String[] args) throws IOException, SftpException {
        Map<String, List<String>> map = new HashMap<>();
        System.out.println(map.size());
        //System.out.println(getPath());;
    }

    public void test1(){
        String str = "1979208.800";

        double num;

        DecimalFormat myformat = new DecimalFormat("#0");
        num = Double.parseDouble(str);//装换为double类型
        num = Double.parseDouble(myformat.format(num));//保留2为小数
        System.out.println(num);

        System.out.println(String.valueOf(
                (int) (0.43 * num / 100) * 100));
        double d = 1.986048E7;
        //String s ="38483672.800";
        String s = "45771120";
        double sd = Double.valueOf(s);
        double pdschPrbAssnData = Double.parseDouble("38483672.800");
        System.out.println(String.valueOf(
                (int) (0.43 * pdschPrbAssnData / 100) * 100));
        System.out.println(pdschPrbAssnData);
        System.out.println(sd);
        System.out.println(String.valueOf(
                (int) (0.43 * sd / 100) * 100));
        for (int i = 0; i < 10; i++) {
            int min = 40, max = 50;
            double rd = (double) (min + (int) (Math.random() * ((max - min) + 1))) / 100;
            System.out.println(rd);

            System.out.println("d" + (int) (rd * (d / 100)) * 100);
            System.out.println("d" + (int) (0.44 * d / 100) * 100);
            return;
        }
    }
}