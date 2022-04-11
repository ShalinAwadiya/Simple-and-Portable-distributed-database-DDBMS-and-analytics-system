package ca.dal.database.iam;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

import static ca.dal.database.utils.StringUtils.getHash;

public class User {
    private String uid;
    private String pwd;
    private String securityQuestion;

    private String ans;
    private String encryptedUid;
    private String encryptedPwd;
    final static String user_profile_path = Path.of("datastore", "system", "UserProfile.txt").toString();

    final static String separator = "%^&";
    final static String separator_regex = "%\\^&";


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


    public String getUser() {
        String data = "";
        data += getHash(getUid()) + separator;
        data += getHash(getPwd()) + separator;
        data += getSecurityQuestion() + separator + getAns() + "\n";
        return data;
    }

    public void save() {
        File f = new File(user_profile_path);

        if (!f.exists()) {
            new File(f.getParent()).mkdirs();
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileWriter fileWriter = new FileWriter(f.getAbsolutePath(), true);
            fileWriter.write(getUser());
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public User[] writeUsers() {
        File f = new File(user_profile_path);

        if (!f.exists()) {
            new File(f.getParent()).mkdirs();
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
                users[counter] = writeData(sc.nextLine());
                counter++;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return users;
    }

    public User writeData(String data) {
        String[] dataArr = data.split(separator_regex);
        User user = new User();
        if (dataArr[0] != null && dataArr[1] != null && dataArr[2] != null && dataArr[3] != null) {
            user = new User("", "", dataArr[2], dataArr[3], dataArr[0], dataArr[1]);
        }
        return user;
    }

    public boolean userIdCheck(String userId) {
        User[] users = writeUsers();
        boolean isFound = false;
        for (int i = 0; i < users.length; i++) {
            if (users[i].getEncryptedUid().equals(getHash(userId))) {
                isFound = true;
                break;
            }
        }
        return isFound;
    }

    public User validateUserIdAndPassword(String userId, String password) {
        User[] users = writeUsers();
        boolean isFound = false;
        User user = null;
        for (int i = 0; i < users.length; i++) {
            if (users[i].getEncryptedUid().equals(getHash(userId)) && users[i].getEncryptedPwd().equals(getHash(password))) {
                isFound = true;
                user = users[i];
                break;
            }
        }
        return user;
    }
}