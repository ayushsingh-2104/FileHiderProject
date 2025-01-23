package service;

import dao.UserDAO;
import model.User;

import java.sql.SQLException;

public class UserService {
    public static Integer saveUser(User user){
        try{
            if(UserDAO.isExists(user.getEmail())){
                return 0;
            }else {
                UserDAO.saveUser(user);
                return 1;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}
