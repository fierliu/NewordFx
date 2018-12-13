package dao;

import tool.JdbcUtils;
import tool.ParseTXT;

import java.sql.*;
import java.util.ArrayList;

/*
* 创建新表
* CREATE TABLE "GRE_copy1" (
  "ID" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  "en" varchar,
  "ch" varchar,
  "PhoneticSymbol" varchar,
  "Remark" varchar
);
* */
public class WordSQLDao {

    private Connection c ;
    private PreparedStatement statement;
    private static String fileName = "D:\\en\\英语\\english\\李阳英语365句.txt";
    private static String SqliteTableName = "Common365Sentences";
    public WordSQLDao(Connection c){
        this.c = c;
    }

//    生成预设词库用, Arraylist内容的格式[en,ch,en,ch...]
    public void addDefaulVoc(ArrayList<String> wordList){
//        从arraylist中遍历，然后逐个添加到数据库中
        try {
//            要把autocommit关掉！
            c.setAutoCommit(false);
            String sql;
            int id;
            for (int i = 0; i < wordList.size(); i += 2) {
                id = i/2 +1;
                sql = "INSERT INTO " + SqliteTableName +" (en, ch) VALUES (?,?);";
                statement = c.prepareStatement(sql);
                statement.setString(1, wordList.get(i));
                statement.setString(2,wordList.get(i+1));
                if(i % 10 == 0 ) System.out.print("\r" + i );
                else System.out.print("*");
                statement.executeUpdate();
            }
            statement.close();
            c.commit();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Records created successfully");
    }

    public static void main(String[] args) throws SQLException {
//        new WordSQLDao(JdbcUtils.getConnection()).addDefaulVoc(
//                new ParseTXT().readLinedFile(fileName));
        Connection connection = JdbcUtils.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM GRE;");
        resultSet.next();
        String ch = resultSet.getString("ch");
        System.out.println(ch);
    }
}
