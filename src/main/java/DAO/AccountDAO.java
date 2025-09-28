package DAO;
import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    public List<Account>getAllAccounts(){
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();
        try {
            String sql = "select * from account;";;
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"),rs.getString("username"), rs.getString("password"));
                accounts.add(account);
            }
        } 
        catch (Exception e) {
           System.out.println(e.getMessage());
        }
        return accounts;
    }
    public Account insertAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "insert into account(username, password) values(?,?)";
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //write preparedStatement's setString method here.
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
                        preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_account_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }

        } catch (Exception e) {
             System.out.println(e.getMessage());
        }
         return null;
    }
    public Account getAccountByUserName(String username){
        String sql = "SELECT account_id, username, password FROM account WHERE username = ?";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("account_id");
                String uname = rs.getString("username");
                String pwd = rs.getString("password");

                return new Account(id, uname, pwd);
            } else {
                return null; // no account found
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null; // return null on error
        }
    }
    
}
