package ca.dal.database.utils;

import ca.dal.database.config.ApplicationConfiguration;
import ca.dal.database.config.model.InstanceModel;
import com.jcraft.jsch.*;

public class SSHUtils {

    private static final String SIMPLE_FILE_TRANSFER_PROTOCOL = "";

    public static final String PUSH = "PUSH";
    public static final String PULL = "PULL";

    public void transfer(String transferType, String fileName) {
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

            ChannelSftp c = (ChannelSftp) channel;

            if (transferType.equalsIgnoreCase(PUSH)) {
                c.put(instance.getSharedResourceLocation(), instance.getSharedResourceLocation());
            } else if (transferType.equalsIgnoreCase(PULL)) {
                c.get(instance.getSharedResourceLocation(), instance.getSharedResourceLocation());
            }

            c.exit();
        } catch (JSchException | SftpException e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.disconnect();
            }
        }
    }
}
