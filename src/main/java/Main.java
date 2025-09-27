import java.sql.Connection;
import java.sql.PreparedStatement;

import Controller.SocialMediaController;
import Util.ConnectionUtil;
import io.javalin.Javalin;

/**
 * This class is provided with a main method to allow you to manually run and test your application. This class will not
 * affect your program in any way and you may write whatever code you like here.
 */
public class Main {
    public static void main(String[] args) {
        databaseSetup();
        SocialMediaController controller = new SocialMediaController();
        Javalin app = controller.startAPI();
        app.start(8080);
    }
    public static void databaseSetup(){
        try {
            Connection conn = ConnectionUtil.getConnection();
          
            PreparedStatement ps1 = conn.prepareStatement("drop table if exists message");
            ps1.executeUpdate();
            PreparedStatement ps2 = conn.prepareStatement("drop table if exists account");
            ps2.executeUpdate();
            PreparedStatement ps3 = conn.prepareStatement("create table account(" +
              "account_id int primary key auto_increment,"+
              "username varchar(255) unique,"+
              "password varchar(255)); ");
            ps3.executeUpdate();
            PreparedStatement ps4 = conn.prepareStatement("create table message(" +
            "message_id int primary key auto_increment,"+
            "posted_by int,"+
            "message_text varchar(255),"+
            "time_posted_epoch bigint,"+
            "foreign key (posted_by) references  account(account_id));");
            ps4.executeUpdate();
            PreparedStatement ps5 = conn.prepareStatement(
                    "insert into account (username, password) values " +
                            "('user1', '11111')," +
                            "('user2', '22222')," +
                            "('user3', '33333')");
            ps5.executeUpdate(); 
           /*  PreparedStatement ps6 = conn.prepareStatement(
                    "insert into message (posted_by, message_text, time_posted_epoch) values " +
                            "(100, 1, 'ficciones', 2)," +
                            "(101, 1, 'book of sand', 0)," +
                            "(102, 2, 'mr palomar', 1)," +
                            "(103, 2, 'invisible cities', 3)," +
                            "(104, 3, 'crying of lot 49', 0)," +
                            "(105, 3, 'mason and dixon', 0)," +
                            "(106, 4, 'understanding media', 1)," +
                            "(107, 5, 'critique of pure reason', 7);");
            ps6.executeUpdate();*/

            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
