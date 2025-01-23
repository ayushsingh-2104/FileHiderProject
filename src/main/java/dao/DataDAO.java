package dao;

import db.MyConnection;
import model.Data;

import javax.print.DocFlavor;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataDAO {
    public static List<Data> getAllFiles(String email) throws SQLException {
        Connection connection = MyConnection.getConnection();
        PreparedStatement preparedStatement= connection.prepareStatement("select * from data where email = ?");
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<Data> files = new ArrayList<>();
        while(resultSet.next()){
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            String path = resultSet.getString(3);
            files.add(new Data(id, name , path));
        }
        return  files;
    }

    public static int hideFile(Data file) throws SQLException, FileNotFoundException, IOException {
        Connection connection = MyConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("insert into data(name, path, email, bin_data) values(?,?,?,?)");

        preparedStatement.setString(1, file.getFileName());
        preparedStatement.setString(2, file.getPath());
        preparedStatement.setString(3, file.getEmail());
        File f = new File(file.getPath());

        FileReader fr = new FileReader(f);
        preparedStatement.setCharacterStream(4, fr, f.length());

        int ans = preparedStatement.executeUpdate();

        fr.close();
        f.delete();
        return ans;
    }

    public static void unHide(int id)throws SQLException, IOException {
        Connection connection = MyConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("select path, bin_data from data where id = ?");

        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        String path = resultSet.getString("path");
        Clob c = resultSet.getClob("bin_data");
        Reader r = c.getCharacterStream();
        FileWriter fw = new FileWriter(path);
        int i ;

        while((i = r.read()) != -1){
            fw.write((char) i);
        }
        fw.close();

        preparedStatement = connection.prepareStatement("delete from data where id = ?");
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        System.out.println("Successfully Unhidden the file !!");
    }
}
