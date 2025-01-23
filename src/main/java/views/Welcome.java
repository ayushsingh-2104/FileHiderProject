package views;

import dao.UserDAO;
import model.User;
import service.GenerateOTP;
import service.SendOTP;
import service.UserService;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.Scanner;

public class Welcome {
    public void WelcomeScreen() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("Welcome to the application!");
            System.out.println("Press 1 to login!");
            System.out.println("Press 2 to Sign Up!");
            System.out.println("Press 0 to Exit!");
            System.out.print("Please enter your desired choice: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    signUp();
                    break;
                case 0:
                    System.out.println("Exiting the application. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        }
    }

    private void signUp() {
        System.out.println("Sign Up logic goes here.");
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter name: ");
        String name = sc.nextLine();
        System.out.println("Enter email: ");
        String email = sc.nextLine();

        String genOTP = GenerateOTP.getOTP();
        SendOTP.sendOTP(email, genOTP);

        System.out.println("Enter the OTP: ");
        String otp = sc.nextLine();

        if(otp.equals(genOTP)){
            User user = new User(name, email);
            int response = UserService.saveUser(user);

            switch (response){
                case 0:
                    System.out.println("User registered!!");
                    break;

                case 1:
                    System.out.println("User already exists!!");
                    break;
            }
        }else {
            System.out.println("Wrong OTP !!");
        }
    }



    private void login() {
        System.out.println("Login logic goes here.");
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter email: ");
        String email = sc.nextLine();
        try {
            if(UserDAO.isExists(email)){
                String genOTP = GenerateOTP.getOTP();
                SendOTP.sendOTP(email, genOTP);

                System.out.println("Enter the OTP: ");
                String otp = sc.nextLine();

                if(otp.equals(genOTP)){
                    new UserView(email).home();

                }else {
                    System.out.println("Wrong OTP !!");
                }
            }else {
                System.out.println("User not found ..!!!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
