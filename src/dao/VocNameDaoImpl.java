package dao;

import model.User;
import model.VocName;
import tool.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VocNameDaoImpl implements VocNameDao {
    @Override
    public void add(VocName vocName) {
        String sql = "insert into vocaName value(?,?,?)";
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = JdbcUtils.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, vocName.getId());
            pstmt.setString(2, vocName.getName());
            pstmt.setString(3, vocName.getWordAmount());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(pstmt,con);
        }
    }

    @Override
    public void mod(VocName vocName) {
        String sql =
                "update vocaName set name=?, word_amount=? where id=?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try{
            con = JdbcUtils.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, vocName.getName());
            pstmt.setString(2, vocName.getWordAmount());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(pstmt, con);
        }
    }

    @Override
    public void del(String vid) {
        String sql = "delete from vocaName where id=?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try{
            con = JdbcUtils.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, vid);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(pstmt,con);
        }
    }

    @Override
    public VocName load(String name) {
        String sql = "select * from vocaName where name=?";
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = JdbcUtils.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                return new VocName(rs.getString(1), rs.getString(2),
                        rs.getString(3));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(rs,pstmt,con);
        }
        return null;
    }
    /*
    * 获取所有的词库名称及词库大小
    * */
    @Override
    public List<VocName> findAll() {
        String sql = "select * from vocaName";
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pstmt = null;
        try{
            con = JdbcUtils.getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            List<VocName> vocaNameList = new ArrayList<>();
            while(rs.next()) {
                vocaNameList.add(new VocName(rs.getString(1), rs.getString(2),
                        rs.getString(3)));
            }
            return vocaNameList;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(rs, pstmt,con);
        }
        return null;
    }

    //判断用户选择表后创建的表是否已存在
    public int ifVocNameExists(String vocName){
        String sql = "SELECT count(*) from sqlite_master WHERE name = '"+ vocName+ "';";
        ResultSet rs = null;
        Connection con = null;
        Statement stmt = null;
        try{
            con = JdbcUtils.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            int count = -1;
            if (rs.next()) {
                count = rs.getInt(1);
                return count;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(rs,stmt,con);
        }
        return -1;
    }

//    public static void main(String[] args) {
//        VocNameDaoImpl vocNameDao = new VocNameDaoImpl();
//        int dsf = vocNameDao.ifVocNameExists("allan_GRE");
//        System.out.println("dsf = " + dsf);
//    }
}
