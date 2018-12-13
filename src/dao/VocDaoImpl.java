package dao;

import tool.JdbcUtils;

import java.sql.*;

public class VocDaoImpl implements VocDao {
    @Override
    public void create(String newVocName, String primVocName) {
        /**
        * sql for create new table copy both structure and data;
        * CREATE TABLE [new_table_name] as SELECT * FROM [old_table_name];
        * */
        String sql = "CREATE TABLE "+ newVocName + " as SELECT * FROM "+primVocName +";";
        String sql2 = "ALTER TABLE "+ newVocName + " ADD times int;";
        String sql3 = "ALTER TABLE "+ newVocName + " ADD conquer int;";
        Connection con = null;
        Statement statement = null;
        try{
            con = JdbcUtils.getConnection();
            statement = con.createStatement();
            statement.executeUpdate(sql);
            statement.executeUpdate(sql2);
            statement.executeUpdate(sql3);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(statement,con);
        }
    }

    @Override
    public void delete(String vocName) {
        String sql = "DROP TABLE "+ vocName;
        Connection con = null;
        Statement statement = null;
        try {
            con = JdbcUtils.getConnection();
            statement = con.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(statement,con);
        }
    }

    /*用于判断词库里的总词量*/
    @Override
    public int getVocSize(String vocName) {
        int vocSize;
        String sql = "SELECT COUNT(*) FROM "+ vocName;
        Connection con = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try{
            con = JdbcUtils.getConnection();
            statement = con.createStatement();
            resultSet = statement.executeQuery(sql);
            resultSet.next();
            vocSize = resultSet.getInt(1);
            return vocSize;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JdbcUtils.close(resultSet,statement, con);
        }
        return -1;
    }

    /*获取需要复习的词的数量*/
    public int getReviewCount(String vocName){
        int count = -1;
        String sql = "SELECT count(id) FROM "+ vocName +" where conquer = ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            conn = JdbcUtils.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,1);
            ResultSet rs = pstmt.executeQuery();
            count = rs.getInt(1);
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(pstmt,conn);
        }

        return count;
    }

    /*获取conquer为Null的词的数量*/
    public int getNullConquerCount(String vocName){
        int count = -1;
        String sql = "SELECT count(id) FROM "+ vocName +" where conquer is null;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            conn = JdbcUtils.getConnection();
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            count = rs.getInt(1);
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(pstmt,conn);
        }
        return count;
    }
//    public static void main(String[] args) {
//        VocDaoImpl vocDao = new VocDaoImpl();
//        int gre = vocDao.getNullConquerCount("GRE_allan");
//        System.out.println("gre = " + gre);
//    }
}

