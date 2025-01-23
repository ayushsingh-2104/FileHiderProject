package views;

import dao.DataDAO;
import model.Data;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class UserView {
    private String email;

    public UserView(String email) {
        this.email = email;
    }

    public void home() throws SQLException, IOException {
        Scanner sc = new Scanner(System.in);

        do {
            System.out.println("\nWelcome " + this.email);
            System.out.println("Press 1 to show the hidden files");
            System.out.println("Press 2 to hide a file");
            System.out.println("Press 3 to unhide a file");
            System.out.println("Press 0 to exit");

            System.out.print("Enter your choice: ");
            int ch;
            try {
                ch = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (ch) {
                case 1 -> { // Show hidden files
                    try {
                        List<Data> files = DataDAO.getAllFiles(this.email);
                        System.out.println("ID = File Name");
                        for (Data file : files) {
                            System.out.println(file.getId() + " - " + file.getFileName());
                        }
                    } catch (SQLException e) {
                        System.err.println("Error fetching files: " + e.getMessage());
                    }
                }
                case 2 -> { // Hide a file
                    System.out.print("Enter the file path: ");
                    String path = sc.nextLine();

                    File f = new File(path);
                    if (!f.exists()) {
                        System.out.println("File does not exist. Please try again.");
                        continue;
                    }

                    Data file = new Data(0, f.getName(), path, this.email);
                    try {
                        DataDAO.hideFile(file);
                        System.out.println("File successfully hidden.");
                    } catch (SQLException e) {
                        System.err.println("Database error: " + e.getMessage());
                    } catch (IOException e) {
                        System.err.println("Error hiding file: " + e.getMessage());
                    }
                }
                case 3 -> {
                    List<Data> files = DataDAO.getAllFiles(this.email);
                    System.out.println("ID = File Name");
                    for (Data file : files) {
                        System.out.println(file.getId() + " - " + file.getFileName());
                    }
                    System.out.println("Enter the id of the file to unhide: ");
                    int id = Integer.parseInt(sc.nextLine());

                    boolean isValidId = false;

                    for(Data file : files){
                        if(file.getId() == id){
                            isValidId = true;
                            break;
                        }
                    }
                    if (isValidId){
                        DataDAO.unHide(id);

                    }else {
                        System.out.println("Wrong id");
                    }
                }
                case 0 -> {
                    System.exit(0);
                }


                default -> System.out.println("Invalid choice. Please try again.");
            }

            if (ch == 0) break;
        } while (true);

        sc.close();
    }
}
