package ca.dal.database.iam;

import ca.dal.database.connection.Connection;
import ca.dal.database.logger.IdentityManagementLog;
import ca.dal.database.menu.HomeMenu;
import ca.dal.database.utils.SSHUtils;

import java.util.HashMap;
import java.util.Scanner;

import static ca.dal.database.utils.PrintUtils.error;
import static ca.dal.database.utils.PrintUtils.success;

public class Authentication {

    IdentityManagementLog identityManagementLog;

    public Authentication() {
        identityManagementLog = new IdentityManagementLog();
    }

    public void init() {
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Enter Your Choice: ");

        Scanner sc = new Scanner(System.in);
        String userInput = sc.nextLine();

        if (userInput.equals("1")) {
            userRegistration();
        } else if (userInput.equals("2")) {
            userLogin();
        } else if (userInput.equals("3")) {
            System.out.println("Good Bye!");
        } else {
            error("Incorrect option chosen, Please try Again");
            init();
        }

    }

    private void userRegistration() {
        Scanner sc = new Scanner(System.in);
        User u = new User();

        boolean isUserIdCorrect = false;
        while (!isUserIdCorrect) {
            System.out.println();
            System.out.print("Enter UserId: ");
            String userId = sc.nextLine();
            if (userId.length() < 1) {
                error("userId cannot be empty.");
                continue;
            }
            if (u.userIdCheck(userId)) {
                error("userId already exists.");
                continue;
            }
            isUserIdCorrect = true;
            u.setUid(userId);
        }

        boolean isPasswordCorrect = false;
        while (!isPasswordCorrect) {

            System.out.print("Enter Password: ");
            String password;
            if (System.console() == null) {
                password = sc.nextLine();
            } else {
                password = String.valueOf(System.console().readPassword());
            }

            if (password.length() < 1) {
                error("password cannot be empty");
                continue;
            }
            isPasswordCorrect = true;
            u.setPwd(password);
        }

        boolean isSecurityCorrect = false;
        while (!isSecurityCorrect) {

            System.out.print("Enter Security Question: ");
            String securityQuestion = sc.nextLine();
            if (securityQuestion.length() < 1) {
                error("Security Question cannot be empty");
                continue;
            }
            isSecurityCorrect = true;
            u.setSecurityQuestion(securityQuestion);
        }

        boolean isAnswerCorrect = false;
        while (!isAnswerCorrect) {

            System.out.print("Enter Answer: ");
            String answer = sc.nextLine();
            if (answer.length() < 1) {
                error("Answer cannot be empty");
                continue;
            }
            isAnswerCorrect = true;
            u.setAns(answer);
        }

        u.save();
        //registration successful
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("username", u.getUid());
        identityManagementLog.writeLog("Information", "IdentityManagement", "User registered", data);

        success("User Registered Successfully!");
        init();

    }

    private void userLogin() {
        Scanner sc = new Scanner(System.in);
        String userId = "";
        String password = "";

        boolean isUserIdCorrect = false;
        while (!isUserIdCorrect) {
            System.out.println();
            System.out.print("Enter UserId: ");
            userId = sc.nextLine();
            if (userId.length() < 1) {
                error("userId cannot be empty.");
                continue;
            }
            isUserIdCorrect = true;
        }

        boolean isPasswordCorrect = false;
        while (!isPasswordCorrect) {

            System.out.print("Enter Password: ");
            if (System.console() == null) {
                password = sc.nextLine();
            } else {
                password = String.valueOf(System.console().readPassword());
            }
            if (password.length() < 1) {
                error("password cannot be empty");
                continue;
            }
            isPasswordCorrect = true;
        }

        User user = new User();
        user = user.validateUserIdAndPassword(userId, password);
        if (user == null) {
            System.out.println("Incorrect UserId or Password");
            //unsuccessful login
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("username", userId);
            identityManagementLog.writeLog("Information", "IdentityManagement", "User Login Failed", data);

            init();
            return;
        }

        boolean isAnswerCorrect = false;
        String answer = "";
        while (!isAnswerCorrect) {

            System.out.print("Enter Answer for " + user.getSecurityQuestion() + ": ");
            answer = sc.nextLine();
            if (answer.length() < 1) {
                System.out.println("Answer cannot be empty");
            }
            isAnswerCorrect = true;
        }
        if (!user.getAns().equalsIgnoreCase(answer)) {
            System.out.println("Invalid Security");
            //unsuccessful login
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("username", user.getUid());
            identityManagementLog.writeLog("Information", "IdentityManagement", "User Login Failed", data);

            init();
            return;
        }
        //successful login
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("username", userId);
        identityManagementLog.writeLog("Information", "IdentityManagement", "User Login Success", data);

        success("Logged In Successfully!");

        Connection connection = new Connection(userId);
        HomeMenu homeMenu = new HomeMenu(connection);
        homeMenu.show();

    }
}
