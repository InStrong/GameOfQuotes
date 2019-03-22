import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SQLHandler {
    private static final String url = "jdbc:mysql://localhost:3306/game_of_quotes?autoReconnect=true&useSSL=false&ftimeCode=false&serverTimezone=UTC";
    //private static final String user = "instrong";
    //private static final String password = "fhwheyrfhnjy";
     private static final String user = "root";
     private static final String password = "fhwheyrfhnjy94";

    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;

    protected void connect(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void disconnect(){
        try {
            con.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected boolean isUserRegistered(Long chatId){
        int temp=-1;
        String query = "SELECT EXISTS(SELECT * FROM game_of_quotes.users WHERE chatId='"+chatId+"')";

        try {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
                temp=rs.getInt(1);
            }
            if (temp!=0) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return false;
    }

    protected void registerUser(Long chatId,String firstName, String userName){
        String query = "REPLACE INTO game_of_quotes.users (chatId,firstName,userName) VALUES ('"+chatId+"','"+firstName+"','"+userName+"')";
        try {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void addFoundedQuote(Long chatId,int quoteId){
        String query = "INSERT INTO game_of_quotes.founded_quotes (chatId,quoteId) VALUES ('"+chatId+"','"+quoteId+"')";
        try {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected boolean isQuoteAlreadyFound(Long chatId,int quoteId){
        String query = "select chatId,quoteId from game_of_quotes.founded_quotes where chatId='"+chatId+"'";
        Long tempId = 1l;
        Integer tempQuote = -1;
        try {
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                tempId= rs.getLong(1);
                tempQuote= rs.getInt(2);
                if (tempId.equals(chatId) && tempQuote.equals(quoteId)) return true;
            }


        }catch(SQLException e){
            e.printStackTrace();

        }
        return false;

    }

    protected ArrayList<String> getQuote(Long chatId){
        ArrayList<String> quote = new ArrayList<>();
        int countOfQuotes=0;
        String query = "SELECT * FROM game_of_quotes.quotes ";
        try {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                countOfQuotes++;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        Random ran = new Random();


        String query2 = "SELECT * FROM game_of_quotes.quotes ORDER BY RAND() LIMIT 1";
        try {
            rs = stmt.executeQuery(query2);
            while (rs.next()) {
                quote.add(rs.getString(2));
                quote.add(rs.getString(1));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        String query3 = "SELECT * FROM game_of_quotes.characters ORDER BY RAND() LIMIT 3";
        try {
            rs = stmt.executeQuery(query3);
            while (rs.next()) {
                quote.add(rs.getString(2));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        return quote;

    }
}
