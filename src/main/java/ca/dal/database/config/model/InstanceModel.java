package ca.dal.database.config.model;

public class InstanceModel {

    private String identifier;

    private String privateKey;

    private String user;

    private String host;

    private int port;

    private String sharedResourceLocation;

    public InstanceModel(String identifier, String privateKey, String user, String host, int port, String sharedResourceLocation) {
        this.identifier = identifier;
        this.privateKey = privateKey;
        this.user = user;
        this.host = host;
        this.port = port;
        this.sharedResourceLocation = sharedResourceLocation;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getSharedResourceLocation() {
        return sharedResourceLocation;
    }

    public void setSharedResourceLocation(String sharedResourceLocation) {
        this.sharedResourceLocation = sharedResourceLocation;
    }
}
