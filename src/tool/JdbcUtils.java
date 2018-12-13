package tool;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
/*
* 获取数据库连接*/
public class JdbcUtils {
    private static final String dbconfig = "dbconfig.properties";
    private static Properties prop = new Properties();
    static {
        try {
            InputStream in =
                    Thread.currentThread().getContextClassLoader().getResourceAsStream(dbconfig);
            prop.load(in);
            Class.forName(prop.getProperty("driverClassName"));
        } catch(IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(prop.getProperty("url")/*,
                    prop.getProperty("username"),
                    prop.getProperty("password")*/);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void close(Statement stmt,Connection conn){
        close(null,stmt,conn);
    }

    public static void close(ResultSet rs, Statement stmt, Connection conn){
        if( rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if( stmt != null){
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if( conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
