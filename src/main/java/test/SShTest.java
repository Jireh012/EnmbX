package test;

import com.jcraft.jsch.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.checkerframework.checker.units.qual.C;
import util.MD4;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

public class SShTest {
    private static int timeout = 60000;
    private static ChannelExec channelExec;

    private static Session session = null;

    public static void main(String[] args) throws JSchException {
        Config conf = new Config();
        LOG.info("尝试连接到....host:" + conf.getHost() + ",username:" + conf.getUsername() + ",password:" + conf.getPassword() + ",port:" + conf.getPort());
        JSch jsch = new JSch(); // 创建JSch对象
        session = jsch.getSession(conf.getUsername(), conf.getHost(), conf.getPort()); // 根据用户名，主机ip，端口获取一个Session对象
        session.setPassword(conf.getPassword()); // 设置密码
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config); // 为Session对象设置properties
        session.setTimeout(timeout); // 设置timeout时间
        session.connect(); // 通过Session建立链接
        try {
            runCmd("ls -l","UTF-8");
            runCmd("chmod 777 123","UTF-8");
            runCmd("ls -l","UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static final Logger LOG = Logger.getLogger(SShTest.class);


    public static class Config{
        String host = "192.168.1.117";
        String username="root";
        String password="...........";
        Integer port=22;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }
    }

    public static void runCmd(String cmd, String charset) throws Exception
    {
        channelExec = (ChannelExec)session.openChannel("exec");
        channelExec.setCommand(cmd);
        channelExec.setInputStream(null);
        channelExec.setErrStream(System.err);
        channelExec.connect();
        InputStream in = channelExec.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName(charset)));
        String buf = null;
        while ((buf = reader.readLine()) != null)
        {
            System.out.println(buf);
        }
        reader.close();
        channelExec.disconnect();
    }

    public void close()
    {
        session.disconnect();
    }

}
