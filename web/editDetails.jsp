<%@ page import="com.dbconnectionutil.org.DbConnection" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="com.dbconnectionutil.org.crud" %>
<%@ page import="net.spy.memcached.MemcachedClient" %>
<%@ page import="java.net.InetSocketAddress" %>
<%@ page import="com.dbconnectionutil.org.UserEntity" %>
<%@ page import="java.util.ArrayList" %>
<%--

  Created by IntelliJ IDEA.
  User: Yasiru Randeepa
  Date: 6/28/2018
  Time: 10:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Edit Details</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="bootstrap/css/bootstrap.css">
    <script src="js/jquery.js"></script>
    <script src="bootstrap/js/bootstrap.js"></script>
</head>
<body>
<%
    MemcachedClient mcc = new MemcachedClient(new
            InetSocketAddress("127.0.0.1", 11211));


    String index = request.getParameter("index");
    int selectedIndex = Integer.parseInt(index)-1;
    System.out.println(selectedIndex);

%>

<div class="container bg-faded" style="padding-top: 20px; padding-bottom: 20px;">
    <h1>Edit Record Form</h1>

    <hr>

    <div class="col-md-6">
        <form role="form" action="crud_map" method="post">

            <%

                Object users = mcc.get("userObjects");
                ArrayList<UserEntity> userList = (ArrayList<UserEntity>)users;

                UserEntity selectedUser = userList.get(selectedIndex);

            %>

            <input type="hidden" name="id" value="<%=selectedUser.getUserID()%>">
            <input type="hidden" name="index" value="<%=index%>">
            <input type="hidden" name="check" value="update">
            <div class="form-group">
                <label>First Name</label>
                <input type="text" class="form-control" name="fname" value="<%=selectedUser.getFirstname()%>">
            </div>

            <div class="form-group">
                <label>Last Name</label>
                <input type="text" class="form-control" name="lname" value="<%=selectedUser.getLname()%>">
            </div>

            <div class="form-group">
                <label>Email</label>
                <input type="email" class="form-control" name="email" value="<%=selectedUser.getEmail()%>">
            </div>

            <button class="btn btn-success btn-block" type="submit">Update Record</button>
        </form>
    </div>
</div>

</body>
</html>
