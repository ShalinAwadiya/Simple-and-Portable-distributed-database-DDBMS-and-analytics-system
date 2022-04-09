package ca.dal.database.security;

import ca.dal.database.menu.HomeMenu;
import ca.dal.database.utils.PrintUtils;

import java.util.Scanner;

import static ca.dal.database.utils.PrintUtils.println;
import static ca.dal.database.utils.PrintUtils.success;

public class Authentication {
    public void init() {
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Enter Your Choice: ");

        Scanner sc = new Scanner(System.in);
        String userInput = sc.nextLine();

        if(userInput.equals("1")){
            userRegistration();
            init();
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

            System.out.print("Enter Answer: ");
            String answer = sc.nextLine();
            if (answer.length() < 1) {
                System.out.println("Answer cannot be empty");
            }
            isAnswerCorrect = true;
            u.setAns(answer);
        }

        u.save();

        success("User Registered Successfully!");
    }

    private void userLogin() {
        Scanner sc = new Scanner(System.in);
        String userId = "";
        String password = "";

        boolean isUserIdCorrect = false;
        while (!isUserIdCorrect) {

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
            return;
        }

        success("Logged In Successfully!");

        HomeMenu homeMenu = new HomeMenu();
        homeMenu.show();
    }


}