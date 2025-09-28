package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            //Write SQL logic here
            String sql = "select * from message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
    public List<Message> getAllMessagesByUser(int userId) {
    List<Message> messages = new ArrayList<>();
    String sql = "SELECT * FROM message WHERE posted_by = ?";

    try (Connection connection = ConnectionUtil.getConnection();
         PreparedStatement ps = connection.prepareStatement(sql)) {

        ps.setInt(1, userId); // important!
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Message message = new Message(
                rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch")
            );
            messages.add(message);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return messages;
   }
    public Message getMessageById(int id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            //Write SQL logic here
            String sql = "select * from message where message_id = ? ";
            
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //write preparedStatement's setString and setInt methods here.
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                return message;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;

    }
    public Message deleteMessage(int id){
            Message toDelete = getMessageById(id); // fetch from DB first
        if (toDelete == null) {
            return null;
        }

        String sql = "delete from message where message_id = ?";
        try (Connection conn = ConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toDelete;  

    }
    public Message insertMessage(Message message){
        if (message.getMessage_text() == null || message.getMessage_text().trim().isEmpty()) {
        throw new IllegalArgumentException("message_text cannot be blank");
    }

    try (Connection connection = ConnectionUtil.getConnection()) {
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setInt(1, message.getPosted_by());
        ps.setString(2, message.getMessage_text());
        ps.setLong(3, message.getTime_posted_epoch());

        int rowsAffected = ps.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("Inserting message failed, no rows affected.");
        }

        try (ResultSet keys = ps.getGeneratedKeys()) {
            if (keys.next()) {
                int generatedId = keys.getInt(1);
                return new Message(
                    generatedId,
                    message.getPosted_by(),
                    message.getMessage_text(),
                    message.getTime_posted_epoch()
                );
            } else {
                throw new SQLException("Inserting message failed, no ID obtained.");
            }
         }
        } catch (SQLException e) {
        e.printStackTrace();
        return null;
    }

    }
    public void updateMessage(int id, Message message){
        if (message.getMessage_text() == null || message.getMessage_text().trim().isEmpty()) {
          throw new IllegalArgumentException("message_text cannot be blank");
        }               
        try { Connection connection = ConnectionUtil.getConnection();
            String sql = "update message set message_text =?, time_posted_epoch = ? where message_id =?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //write PreparedStatement setString and setInt methods here.
             preparedStatement.setString(1, message.getMessage_text());
             preparedStatement.setLong(2, message.getTime_posted_epoch());
             preparedStatement.setInt(3, id);

            
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
            throw new SQLException("Inserting message failed, no rows affected.");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

