package ca.dal.database.identitymanagement;

import java.util.HashMap;
import java.util.Scanner;
import ca.dal.database.logger.IdentityManagementLog;

public class Authentication {

    IdentityManagementLog identityManagementLog;

    public Authentication(){
        identityManagementLog =  new IdentityManagementLog();
    }
    public void init() {
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Enter Your Choice: ");

        Scanner sc = new Scanner(System.in);
        String userInput = sc.nextLine();

        if(userInput.equals("1")){
            userRegistration();
        }
        else if(userInput.equals("2")){
            userLogin();
        }
        else if(userInput.equals("3")){
            System.out.println("Good Bye!");
        }
        else{
            System.out.println("Incorrect option chosen, Please try Again");
            init();
        }

    }

    private void userRegistration() {
        Scanner sc = new Scanner(System.in);
        User u = new User();

        boolean isUserIdCorrect = false;
        while (!isUserIdCorrect) {
            System.out.println("");
            System.out.print("Enter UserId: ");
            String userId = sc.nextLine();
            if (userId.length() < 1) {
                System.out.println("userId cannot be empty.");
                continue;
            }
            if (u.userIdCheck(userId)) {
                System.out.println("userId already exists.");
                continue;
            }
            isUserIdCorrect = true;
            u.setUid(userId);
        }

        boolean isPasswordCorrect = false;
        while (!isPasswordCorrect) {
            System.out.println("");
            System.out.print("Enter Password: ");
            String password;
            if(System.console()==null){
                password = sc.nextLine();
            }
            else{
                password = String.valueOf(System.console().readPassword());
            }

            if (password.length() < 1) {
                System.out.println("password cannot be empty");
                continue;
            }
            isPasswordCorrect = true;
            u.setPwd(password);
        }

        boolean isSecurityCorrect = false;
        while (!isSecurityCorrect) {
            System.out.println("");
            System.out.print("Enter Security Question: ");
            String securityQuestion = sc.nextLine();
            if (securityQuestion.length() < 1) {
                System.out.println("Security Question cannot be empty");
                continue;
            }
            isSecurityCorrect = true;
            u.setSecurityQuestion(securityQuestion);
        }

        boolean isAnswerCorrect = false;
        while (!isAnswerCorrect) {
            System.out.println("");
            System.out.print("Enter Answer: ");
            String answer = sc.nextLine();
            if (answer.length() < 1) {
                System.out.println("Answer cannot be empty");
                continue;
            }
            isAnswerCorrect = true;
            u.setAns(answer);
        }

        u.save();
        //registration successful
        HashMap<String,String> data = new HashMap<String, String>();
        data.put("username",u.getUid());
        identityManagementLog.writeLog("Information","IdentityManagement","User registered",data);
    }

    private void userLogin() {
        Scanner sc = new Scanner(System.in);
        String userId = "";
        String password = "";

        boolean isUserIdCorrect = false;
        while (!isUserIdCorrect) {
            System.out.println("");
            System.out.print("Enter UserId: ");
            userId = sc.nextLine();
            if (userId.length() < 1) {
                System.out.println("userId cannot be empty.");
                continue;
            }
            isUserIdCorrect = true;
        }

        boolean isPasswordCorrect = false;
        while (!isPasswordCorrect) {
            System.out.println("");
            System.out.print("Enter Password: ");
            password = sc.nextLine();
            if (password.length() < 1) {
                System.out.println("password cannot be empty");
                continue;
            }
            isPasswordCorrect = true;
        }

        User user = new User();
        user = user.validateUserIdAndPassword(userId, password);
        if (user == null) {
            System.out.println("Incorrect UserId or Password");
            //unsuccessful login
            HashMap<String,String> data = new HashMap<String, String>();
            data.put("username",userId);
            identityManagementLog.writeLog("Information","IdentityManagement","User Login Failed",data);

            init();
            return;
        }

        boolean isAnswerCorrect = false;
        String answer = "";
        while (!isAnswerCorrect) {
            System.out.println("");
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
            HashMap<String,String> data = new HashMap<String, String>();
            data.put("username",user.getUid());
            identityManagementLog.writeLog("Information","IdentityManagement","User Login Failed",data);

            return;
        }
        //successful login
        HashMap<String,String> data = new HashMap<String, String>();
        data.put("username",userId);
        identityManagementLog.writeLog("Information","IdentityManagement","User Login Success",data);


        printMenu();

    }

    private void printMenu(){

        System.out.println("Menu");
        System.out.println("1. Write Queries");
        System.out.println("2. Export");
        System.out.println("3. Data Model");
        System.out.println("4. Analytics");
        System.out.print("Enter your choice of operation:");


        Scanner sc = new Scanner(System.in);
        String userChoice = sc.nextLine();

    }
}
