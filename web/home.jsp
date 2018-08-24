<%@ page import="java.sql.Connection" %>
<%@ page import="com.dbconnectionutil.org.DbConnection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="jdk.nashorn.internal.objects.Global" %>
<%@ page import="com.dbconnectionutil.org.crud" %>
<%@ page import="net.spy.memcached.MemcachedClient" %>
<%@ page import="java.net.InetSocketAddress" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.io.ObjectInputStream" %>
<%@ page import="jdk.nashorn.internal.parser.JSONParser" %>
<%@ page import="jdk.nashorn.internal.runtime.Source" %>
<%@ page import="com.dbconnectionutil.org.UserEntity" %>
<%--
  Created by IntelliJ IDEA.
  User: Yasiru Randeepa
  Date: 6/28/2018
  Time: 11:27 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Available Records</title>
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
    crud obj = new crud();
    if(mcc.get("userObjects")==null) {
        obj.getRecords();
    }
%>
<div class="container bg-light" style="padding-top: 20px; padding-bottom: 20px;">

    <div class="row">
        <div class="col-md-8">
            <h1>CRUD Application</h1>
        </div>

        <div class="col-md-4">
            <form class="form-inline my-2 my-lg-0">
                <input class="form-control mr-sm-2" id="myInput" type="search" placeholder="Search">
                <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
            </form>
        </div>
    </div>

    <hr>
    <div class="row">
        <div class="col-sm-4">

            <form role="form" action="crud_map" method="post">

                <input type="hidden" name="check" value="insert">
                <div class="form-group">
                    <label>First Name</label>
                    <input type="text" class="form-control" name="fname">
                </div>

                <div class="form-group">
                    <label>Last Name</label>
                    <input type="text" class="form-control" name="lname">
                </div>

                <div class="form-group">
                    <label>Email</label>
                    <input type="email" class="form-control" name="email">
                </div>

                <button class="btn btn-info btn-block" type="submit">Add Record</button>

            </form>
        </div>
        <div class="col-sm-8">
            <table class="table">

                <thead>
                <tr>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Email</th>
                    <th colspan="2">Actions</th>
                </tr>
                </thead>

                <tbody id="myTable">
                <%

                    try{
                        Object users = mcc.get("userObjects");
                        ArrayList<UserEntity> userList = (ArrayList)users;
                        int i = 0;

                        for (UserEntity user: userList) {
                            i+=1;



                %>
                <tr>
                    <td><%=user.getUserID()%><%=user.getFirstname()%></td>
                    <td><%=user.getLname()%></td>
                    <td><%=user.getEmail()%></td>
                    <td>
                        <%--<a href="editDetails.jsp?uid=<%=user.getUserID()%>" class="btn btn-success" role="button">Update</a>--%>

                        <a href="editDetails.jsp?index=<%=i%>" class="btn btn-success" role="button">Update</a>

                    </td>
                    <td>
                        <form method="get" action="crud_map">
                            <input type="hidden" name="id" value="<%=user.getUserID()%>">
                            <input type="hidden" name="index" value="<%=i%>">
                            <input type="hidden" name="check" value="delete">
                            <button class="btn btn-danger" type="submit">Delete</button>
                        </form>
                    </td>
                </tr>
                <%
                        }
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                %>

                </tbody>
            </table>
        </div>
    </div>
    <script>
        $(document).ready(function(){
            $("#myInput").on("keyup", function() {
                var value = $(this).val().toLowerCase();
                $("#myTable tr").filter(function() {
                    $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
                });
            });
        });
    </script>

</div>
</body>

</html>
