package dao;

import model.User;
import tool.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
/*
* * todo 把释放资源改成JDBCUtils.close
* */
public class UserDaoImpl implements UserDao{

    @Override
    public void add(User user) {
        String sql = "insert into user (user_id, name, password) value(?,?,?)";//todo 少了几个值
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = JdbcUtils.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user.getUid());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getPassword());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(pstmt != null){
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(con != null){
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void mod(User user) {
        String sql =
                "update user set `name`=?, `password`=?, vocName=?, quota=?, total=? " +
                        " where `user_id`=?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try{
            con = JdbcUtils.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getVocName());
            pstmt.setString(4, user.getQuota());
            pstmt.setString(5, user.getTotal());
            pstmt.setString(6, user.getUid());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(pstmt != null){
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(con != null){
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void del(String uid) {
        String sql = "delete from user where user_id=?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try{
            con = JdbcUtils.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, uid);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(pstmt != null){
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(con != null){
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public User load(String uid) {
        String sql = "select * from user where user_id=?";
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = JdbcUtils.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, uid);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                return new User(rs.getString(1), rs.getString(2),
                        rs.getString(3),rs.getString(4),rs.getString(5),
                        rs.getString(6));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(pstmt != null){
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(con != null){
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

//    public static void main(String[] args) {
//        //User{uid='1', username='allan', password='123456', vocName='allan_GRE', quota='null', total='null'}
//        UserDaoImpl userDao = new UserDaoImpl();
//        userDao.mod(new User("1","allan","123456","allan_GRE","null",
//                "null"));
//
//    }
}
