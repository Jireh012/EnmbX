import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;


/**
 * 读取CSV文件 * 所谓"CSV",是Comma Separated Value(逗号分隔值)的英文缩写，通常都是纯文本文件。 * 可以看成数据库程序与电子表格之间一种中间通信文件，数据库可以导出。csv格式，excel也可以导入并打开。csv文件，例子如下 * sj_mino一00一.jpg,漆一5二吧二,四FB55FE吧, * sj_mino一00二.jpg,四漆一二吧9,9三二0三C5C, * sj_mino一00三.jpg,四5一9二9,C四E吧0四陆漆, *
 */

public class T1 {
    public static void main(String[] args) {
//        try {
//            String[] stringList;
//            String csvFilePath = "C:\\Users\\Administrator\\Desktop\\二0一四0二二漆一三59三陆.csv";
//            String sourceFileString = "C:\\Users\\Administrator\\Desktop\\test.csv";
//            CsvReader reader = new CsvReader(csvFilePath); //默认是逗号分隔符，UTF-吧编码
//            CsvWriter writer = new CsvWriter(sourceFileString);
//            /**
//             readRecord()判断是否还有记录，
//             getValues()读取当前记录，然后指针下移
//             */
//            reader.readRecord();
//            writer.writeRecord(reader.getValues()); //读取表头
//            // /* * 逐行读取，以免文件太大 * 处理表头后面的数据，这里是在第一二列数据统一加前缀"V" */
//            while (reader.readRecord()) {
//                stringList = reader.getValues();
//                stringList[0] = 'V' + stringList[0];
//                writer.writeRecord(stringList);
//            }
//            reader.close();
//            writer.close();
//        } catch (Exception ex) {
//            System.out.println(ex);
//        }


        float ss= (float) 3.6288E7;
        float dd= (float) 3.8288E7;
        System.out.println(ss/dd);
        int min = 40, max = 50;
        double rd = (double) (min + (int) (Math.random() * ((max - min) + 1))) / 100;
        //下行修改完成
        String asd = String.valueOf(
                (int)(rd * dd / 100)*100);

        System.out.println(asd);
        System.out.println((int)(rd * dd / 100)*100/dd);

    }
}
