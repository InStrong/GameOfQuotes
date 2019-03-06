import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

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

//    protected void plusPoint(String userName){
//        String query = "UPDATE game_of_quotes.score SET score = score + 1 WHERE id='"+userName+"'";
//        try {
//            stmt.executeUpdate(query);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    protected void minusPoint(String userName){
//        String query = "UPDATE game_of_quotes.score SET score = score - 1 WHERE id='"+userName+"'";
//        try {
//            stmt.executeUpdate(query);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }


//    protected String upTo20Spaces(String s){
//        StringBuilder sb = new StringBuilder();
//        char[] str = s.toCharArray();
//        for (int i = 0; i < s.length(); i++) {
//            if (str[i]!='_') {
//                sb.append(str[i]);
//            }
//            else sb.append("_");
//        }
//        do{
//            sb.append(" ");
//        } while (sb.toString().length()==25);
//        return sb.toString();
//    }
//
//    protected String top10(){
//        StringBuilder sb= new StringBuilder();
//        int i=1;
//        String query = "SELECT * FROM game_of_quotes.score order by score DESC LIMIT 10";
//
//        try {
//            ResultSet rs = stmt.executeQuery(query);
//            while (rs.next()){
//                sb.append(i++);
//                sb.append(". ");
//                sb.append("@");
//                sb.append(upTo20Spaces(rs.getString(1)));
//                sb.append(" : ").append(rs.getInt(3)).append(" монет");
//                sb.append(System.lineSeparator());
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return sb.toString();
//    }
//
//
//
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
//
//    protected int currentScore(String userId){
//        String query = "select score from score where id='"+userId+"'";
//        int currentScore=0;
//        try {
//            rs = stmt.executeQuery(query);
//            while (rs.next()) {
//                currentScore = rs.getInt(1);
//            }
//        }catch(SQLException e){
//            e.printStackTrace();
//
//        }
//        return currentScore;
//
//    }
//
//    protected void info() {
//        String query = "select id,FirstName,score from score";
//
//        try {
//            rs = stmt.executeQuery(query);
//            while (rs.next()) {
//                String id = rs.getString(1);
//                String firstName = rs.getString(2);
//                int score = rs.getInt(3);
//                System.out.printf("id: %s, name: %s, score: %d %n", id, firstName, score);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//
//        }
//    }
}
