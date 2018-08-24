package com.dbconnectionutil.org;
import com.dbconnectionutil.org.DbConnection;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class crud extends HttpServlet{

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    int row = 0;

    MemcachedClient mcc;

    public crud() throws IOException{
        mcc = new MemcachedClient (new
                InetSocketAddress("127.0.0.1", 11211));
    }

    protected void doPost(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException
    {

        String fname = request.getParameter("fname");
        String lname = request.getParameter("lname");
        String email = request.getParameter("email");

        String action = request.getParameter("check");
        if(action.equals("insert")){
            int row = this.insertData(fname, lname, email);
            if(row>0){
                this.getRecords();
                response.sendRedirect("home.jsp");
            }
        }else if(action.equals("update")){
            String uid = request.getParameter("id");
            int id = Integer.parseInt(uid);

            String index = request.getParameter("index");
            int selectedIndex = Integer.parseInt(index);

            int row = this.updateData(fname, lname, email, id, selectedIndex);
            if(row>0){
                this.getRecords();
                response.sendRedirect("home.jsp");
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        String uid = request.getParameter("id");
        String index = request.getParameter("index");

        int id = Integer.parseInt(uid);
        int user_index = Integer.parseInt(index);

        String action = request.getParameter("check");

        try {

            if(action.equals("delete")){
                int row = this.deleteRecord(id, user_index);
                if(row>0){
                    this.getRecords();
                    response.sendRedirect("home.jsp");
                }
            }else{
                System.out.println("Error in Delete");
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    public int insertData(String fname, String lname, String email){
        // reading the user input
        String query = "INSERT INTO newtable(fname,lname,email) values(?,?,?)";
        try{
            int id=0;

            conn = DbConnection.getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1,fname);
            ps.setString(2,lname);
            ps.setString(3,email);

            row = ps.executeUpdate();

            String query_id = "SELECT MAX(uid) from newtable";
            ResultSet rs = this.executeQuery(query_id);
            while (rs.next()){
                id = rs.getInt("uid");
            }

            Object users = mcc.get("userObjects");
            ArrayList<UserEntity> userList = (ArrayList)users;
            UserEntity user = new UserEntity(id, fname, lname, email);
            userList.add(user);
            mcc.set("userObjects",900,userList);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return row;
    }

    public int updateData(String fname, String lname, String email, int uid, int index){

        String query = "UPDATE newtable set fname = ?, lname = ?, email = ? where uid = ?";

        try{
            conn = DbConnection.getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1,fname);
            ps.setString(2,lname);
            ps.setString(3,email);
            ps.setInt(4,uid);

            row = ps.executeUpdate();

            Object users = mcc.get("userObjects");
            ArrayList<UserEntity> userList = (ArrayList<UserEntity>) users;
            UserEntity updatedUser = new UserEntity(uid, fname, lname, email);
            userList.set(index, updatedUser);
            mcc.set("userObjects", 900, userList);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return row;
    }

    public void getRecords(){
        try{
            ArrayList obj = new ArrayList();
            String query = "SELECT * FROM newtable";
            ResultSet resultSet = this.executeQuery(query);
            while (resultSet.next()){
                UserEntity user = new UserEntity(resultSet.getInt("uid"),resultSet.getString("fname"),resultSet.getString("lname"),resultSet.getString("email"));
                obj.add(user);
            }
            mcc.set("userObjects", 900, obj);
            System.out.println(obj);
            mcc = new MemcachedClient (new
                    InetSocketAddress("127.0.0.1", 11211));
            // Connecting to Memcached server on localhost
            if(mcc.get("userObjects")==null) {
                mcc.set("userObjects", 900, obj);
            }
            mcc.set("userObjects", 900, obj);


        }catch(Exception ex){
            System.out.println(ex);
        }

    }

    public ResultSet getRecord(int id){
        String query = "SELECT * FROM newtable where uid = '"+id+"'";
        ResultSet resultSet = this.executeQuery(query);
        return resultSet;
    }

    public int deleteRecord(int id, int index){
        try{
            String query = "DELETE FROM newtable where uid=?";
            conn = DbConnection.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1,id);

            row = ps.executeUpdate();

            Object users = mcc.get("userObjects");
            ArrayList<UserEntity> userList = (ArrayList)users;
            userList.remove(index);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return row;
    }

    private ResultSet executeQuery(String query){
        try{
            conn = DbConnection.getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return rs;
    }

}