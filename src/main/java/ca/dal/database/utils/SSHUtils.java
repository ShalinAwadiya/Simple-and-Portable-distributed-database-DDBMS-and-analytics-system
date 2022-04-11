package ca.dal.database.utils;

import ca.dal.database.config.ApplicationConfiguration;
import ca.dal.database.config.model.InstanceModel;
import com.jcraft.jsch.*;

import java.io.InputStream;
import java.util.List;

import static ca.dal.database.utils.PathUtils.builder;
import static ca.dal.database.utils.PrintUtils.print;

public class SSHUtils {

    private static final String SIMPLE_FILE_TRANSFER_PROTOCOL = "sftp";

    public static final String PUSH = "PUSH";
    public static final String PULL = "PULL";
    public static final String PEEK = "PEEK";

    public static List<String> operation(String operation, String fileName){
        Session session = null;

        InstanceModel instance = ApplicationConfiguration.getInstance();

        try {
            JSch jsch = new JSch();
            jsch.addIdentity(instance.getPrivateKey());
            session = jsch.getSession(instance.getUser(), instance.getHost(), instance.getPort());
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            Channel channel = session.openChannel(SIMPLE_FILE_TRANSFER_PROTOCOL);
            channel.setInputStream(System.in);
            channel.setOutputStream(System.out);
            channel.connect();

            ChannelSftp channelSftp = (ChannelSftp) channel;

            String operatingPath = builder(instance.getSharedResourceLocation(), fileName);

            switch (operation){
                case PUSH:
                    channelSftp.put(operatingPath, operatingPath);
                    break;
                case PULL:
                    channelSftp.get(operatingPath, operatingPath);
                    break;
                case PEEK:
                    InputStream inputStream = channelSftp.get(operatingPath);
                    return FileUtils.read(inputStream);
                default:
            }

            channelSftp.exit();
        } catch (JSchException | SftpException e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.disconnect();
            }
        }
        return null;
    }


    public static List<String> operation(String operation, String srcFilePath, String descFilePath){
        Session session = null;

        InstanceModel instance = ApplicationConfiguration.getInstance();

        try {
            JSch jsch = new JSch();
            jsch.addIdentity(instance.getPrivateKey());
            session = jsch.getSession(instance.getUser(), instance.getHost(), instance.getPort());
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            Channel channel = session.openChannel(SIMPLE_FILE_TRANSFER_PROTOCOL);
            channel.setInputStream(System.in);
            channel.setOutputStream(System.out);
            channel.connect();

            ChannelSftp channelSftp = (ChannelSftp) channel;

            String operatingPath = builder(instance.getSharedResourceLocation(), srcFilePath);

            switch (operation){
                case PUSH:
                    channelSftp.put(operatingPath, "/home/harshshah1295/d2_db/"+descFilePath);
                    break;
                case PULL:
                    channelSftp.get(operatingPath, "/home/harshshah1295/d2_db/"+descFilePath);
                    break;
                case PEEK:
                    InputStream inputStream = channelSftp.get("/home/harshshah1295/d2_db/" + descFilePath);
                    return FileUtils.read(inputStream);
                default:
            }

            channelSftp.exit();
        } catch (JSchException | SftpException e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.disconnect();
            }
        }
        return null;
    }
}
