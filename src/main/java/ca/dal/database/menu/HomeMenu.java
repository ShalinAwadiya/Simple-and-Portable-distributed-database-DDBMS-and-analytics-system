package ca.dal.database.menu;

import java.util.Scanner;

import static ca.dal.database.utils.PrintUtils.println;

public class HomeMenu {

    public void show(){

        println("Select option from the Menu");
        System.out.println("1. Write Queries");
        System.out.println("2. Export");
        System.out.println("3. Data Model");
        System.out.println("4. Analytics");
        System.out.println("5. Exit");
        System.out.print("Enter your choice of operation: ");


        Scanner sc = new Scanner(System.in);
        int userChoice = sc.nextInt();

    }
}
