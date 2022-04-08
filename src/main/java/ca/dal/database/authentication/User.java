package ca.dal.database.authentication;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

public class User {
    private String userId;
    private String password;
    private String securityQuestion;
    private String answer;
    private String encryptedUserId;
    private String encryptedPassword;

    public User() {
    }

    public User(String userId, String password, String securityQuestion, String answer) {
        this.userId = userId;
        this.password = password;
        this.securityQuestion = securityQuestion;
        this.answer = answer;
    }

    public User(String userId, String password, String securityQuestion, String answer, String encryptedUserId, String encryptedPassword) {
        this.userId = userId;
        this.password = password;
        this.securityQuestion = securityQuestion;
        this.answer = answer;
        this.encryptedUserId = encryptedUserId;
        this.encryptedPassword = encryptedPassword;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getEncryptedUserId() {
        return encryptedUserId;
    }

    public void setEncryptedUserId(String encryptedUserId) {
        this.encryptedUserId = encryptedUserId;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

//TODO delimeter
    public String serializeUser() {
        Encryption encrypt = new Encryption();
        String data = "";
        data += encrypt.MD5(getUserId()) + Constants.delimiter;
        data += encrypt.MD5(getPassword()) + Constants.delimiter;
        data += getSecurityQuestion() + Constants.delimiter + getAnswer() + "\n";
        return data;
    }

    public void save() {
        File f = new File(Constants.USER_PROFILE_TXT);
        try {
            FileWriter fileWriter = new FileWriter(f.getAbsolutePath(), true);
            fileWriter.write(serializeUser());
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
//Todo
    public User[] deserializeUsers() {
        File f = new File(Constants.USER_PROFILE_TXT);
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
//ToDO
    public User deserialize(String data) {
        String[] dataArr = data.split(Constants.delimiter);
        User user = new User();
        if (dataArr[0] != null && dataArr[1] != null && dataArr[2] != null && dataArr[3] != null) {
            user = new User("", "", dataArr[2], dataArr[3], dataArr[0], dataArr[1]);
        }
        return user;
    }

    public boolean userIdCheck(String userId) {
        Encryption encryption = new Encryption();
        User[] users = deserializeUsers();
        boolean isFound = false;
        for (int i = 0; i < users.length; i++) {
            if (users[i].getEncryptedUserId().equals(encryption.MD5(userId))) {
                isFound = true;
                break;
            }
        }
        return isFound;
    }

    public User validateUserIdAndPassword(String userId, String password) {
        Encryption encryption = new Encryption();
        User[] users = deserializeUsers();
        boolean isFound = false;
        User user = null;
        for (int i = 0; i < users.length; i++) {
            if (users[i].getEncryptedUserId().equals(encryption.MD5(userId)) && users[i].getEncryptedPassword().equals(encryption.MD5(password))) {
                isFound = true;
                user = users[i];
                break;
            }
        }
        return user;
    }
}
