package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    static String url = "jdbc:mysql://localhost:3306/filehiderproject";
    static String user_name = "root";
    static String password = "Ragnar@s11";

    static Connection connection = null;
    public static Connection getConnection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver Loaded Successfully!");
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try{
            connection = DriverManager.getConnection(url, user_name, password);
            System.out.println("Connection stablished successfully!!");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return connection;
    }

    public static void closeConnection(){
        if(connection != null){
            try{
                connection.close();
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }
}
