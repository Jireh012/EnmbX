import org.apache.commons.codec.digest.DigestUtils;
import util.MD4;

import java.io.UnsupportedEncodingException;

public class Test {
    public static void main(String[] args) {
        //step1();
        step2();
    }

    public static void step1(){
        Long start =System.currentTimeMillis();
        String data = "39";

        for (int i = 1;i<=100000000;i++) {
            try {
                data=  new String(DigestUtils.md2Hex(data).getBytes("gbk"),"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        System.out.println(data);
        System.out.println((System.currentTimeMillis()-start)/1000);
    }

    public static void step2(){
        Long start =System.currentTimeMillis();
        String data = "94b1cf2c8f4aa239dd69e90f0850b2f9"+"yVvdcU4szm+MMeo6Ufn5fMyLWm+9SW0qUEMnmQ";
        data= MD4.MD4(data);
        System.out.println(data);
        System.out.println((System.currentTimeMillis()-start)/1000);
    }
}
