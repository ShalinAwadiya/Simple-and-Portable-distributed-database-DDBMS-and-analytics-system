package ca.dal.database.identitymanagement;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

public class User {
    private String uid;
    private String pwd;
    private String securityQuestion;
    
    private String ans;
    private String encryptedUid;
    private String encryptedPwd;
    final static String user_profile_path = "src/main/java/ca/dal/database/Security/UserProfile.txt";
    final static String separator = "%\\^&";

    public User() {
    }

    public User(String userId, String pwd, String securityQuestion, String ans) {
        this.uid = userId;
        this.pwd = pwd;
        this.securityQuestion = securityQuestion;
        this.ans = ans;
    }

    public User(String userId, String pwd, String securityQuestion, String ans, String encryptedUid, String encryptedPwd) {
        this.uid = userId;
        this.pwd = pwd;
        this.securityQuestion = securityQuestion;
        this.ans = ans;
        this.encryptedUid = encryptedUid;
        this.encryptedPwd = encryptedPwd;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public String getEncryptedUid() {
        return encryptedUid;
    }

    public void setEncryptedUid(String encryptedUid) {
        this.encryptedUid = encryptedUid;
    }

    public String getEncryptedPwd() {
        return encryptedPwd;
    }

    public void setEncryptedPwd(String encryptedPwd) {
        this.encryptedPwd = encryptedPwd;
    }


    public String serializeUser() {
        Hashing encrypt = new Hashing();
        String data = "";
        data += encrypt.getHash(getUid()) + separator;
        data += encrypt.getHash(getPwd()) + separator;
        data += getSecurityQuestion() + separator + getAns() + "\n";
        return data;
    }

    public void save() {
        File f = new File(user_profile_path);
        try {
            FileWriter fileWriter = new FileWriter(f.getAbsolutePath(), true);
            fileWriter.write(serializeUser());
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public User[] deserializeUsers() {
        File f = new File(user_profile_path);
        int lineCounter = 0;
        try {
            Scanner sc = new Scanner(new FileReader(f.getAbsolutePath()));
            while (sc.hasNext()) {
                lineCounter++;
                sc.nextLine();
            }
            sc.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        User[] users = new User[lineCounter];
        try {
            Scanner sc = new Scanner(new FileReader(f.getAbsolutePath()));
            int counter = 0;
            while (sc.hasNext()) {
                users[counter] = deserialize(sc.nextLine());
                counter++;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return users;
    }

    public User deserialize(String data) {
        String[] dataArr = data.split(separator);
        User user = new User();
        if (dataArr[0] != null && dataArr[1] != null && dataArr[2] != null && dataArr[3] != null) {
            user = new User("", "", dataArr[2], dataArr[3], dataArr[0], dataArr[1]);
        }
        return user;
    }

    public boolean userIdCheck(String userId) {
        Hashing hashing = new Hashing();
        User[] users = deserializeUsers();
        boolean isFound = false;
        for (int i = 0; i < users.length; i++) {
            if (users[i].getEncryptedUid().equals(hashing.getHash(userId))) {
                isFound = true;
                break;
            }
        }
        return isFound;
    }

    public User validateUserIdAndPassword(String userId, String password) {
        Hashing hashing = new Hashing();
        User[] users = deserializeUsers();
        boolean isFound = false;
        User user = null;
        for (int i = 0; i < users.length; i++) {
            System.out.println(users[i].getEncryptedUid() +"----->"+ hashing.getHash(userId));
            if (users[i].getEncryptedUid().equals(hashing.getHash(userId))) {
                isFound = true;
                user = users[i];
                break;
            }
        }
        return user;
    }
}
