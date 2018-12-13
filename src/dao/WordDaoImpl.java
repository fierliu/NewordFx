package dao;

import model.UserWord;
import tool.JdbcUtils;

import java.sql.*;

/*
* 用于背词过程中对词的操作，对象是UserWord
* */
public class WordDaoImpl implements WordDaoInterface{
    @Override
    public void update(UserWord userWord, String vocName) {
        Connection con = null;
        PreparedStatement pstmt = null;
        String sql = "UPDATE "+ vocName + " SET Remark=?, times=?, Conquer=? where id=?";
        try{
            con = JdbcUtils.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,userWord.getRemark());
            pstmt.setInt(2,userWord.getTimes());
            pstmt.setInt(3,userWord.getConquer());
            pstmt.setInt(4,userWord.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
           JdbcUtils.close(pstmt,con);
        }
    }

    @Override
    public void delete(Integer wid, String vocName) {
        String sql = "DELETE FROM "+ vocName+ " WHERE id=?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try{
            con = JdbcUtils.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, wid);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
           JdbcUtils.close(pstmt,con);
        }
    }

    /*
    * 用于获取复习单词*/
    @Override
    public UserWord getByRandom(String vocName) {
        //显示复习单词
        String sqlNew = "SELECT * FROM "+ vocName+ "  WHERE Conquer = 1 ORDER BY RANDOM() LIMIT 1;";
        ResultSet rs = null;
        Connection con = null;
        Statement stmt = null;
        try {
            con = JdbcUtils.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(sqlNew);
            if(rs.next()){
                if(rs.getString(1) != null) {//复习单词背完的情况
                    return new UserWord(rs.getInt(1), rs.getString(2),
                            rs.getString(3), rs.getString(4),
                            rs.getString(5), rs.getInt(6),
                            rs.getInt(7));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JdbcUtils.close(rs,stmt,con);
        }

        return null;
    }

    /*用于获取新词*/
    public UserWord getByRandomNew(String vocName){
        //显示新词
        ResultSet rs = null;
        String sql = "select * from " + vocName + " WHERE conquer is null ORDER BY random() LIMIT 1;";
        Connection con = null;
        Statement stmt = null;
        try{
            con = JdbcUtils.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            if(rs.next()){
                return new UserWord(rs.getInt(1),rs.getString(2),
                        rs.getString(3),rs.getString(4),
                        rs.getString(5),rs.getInt(6),
                        rs.getInt(7));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(rs,stmt,con);
        }
        return null;
    }

    @Override
    public UserWord getByAscend(String vocName) {
        return null;
    }

    @Override
    public UserWord getByDescend(String vocName) {
        return null;
    }

    @Override
    public UserWord getByWordId(Integer wid, String vocName) {
        ResultSet rs = null;
        String sql = "select * from " + vocName + " WHERE ID = "+ wid+ ";";
        Connection con = null;
        Statement stmt = null;
        try {
            con = JdbcUtils.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            if(rs.next()){
                return new UserWord(rs.getInt(1),rs.getString(2),
                        rs.getString(3),rs.getString(4),
                        rs.getString(5),rs.getInt(6),
                        rs.getInt(7));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JdbcUtils.close(rs,stmt,con);
        }
        return null;
    }

    /*用于将词的conquer - 1之前的查询*/
    public void updateConquer(String vocName){
        String sql = "UPDATE "+ vocName +" set  conquer = conquer -1 WHERE conquer >1;";
        Connection con = null;
        Statement stmt = null;
        try {
            con = JdbcUtils.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
           JdbcUtils.close(stmt,con);
        }
    }

//    public static void main(String[] args) {
//        new WordDaoImpl().updateConquer("allan_GRE");
//    }
}
