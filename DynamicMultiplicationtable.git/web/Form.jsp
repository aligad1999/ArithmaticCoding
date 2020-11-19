<%-- 
    Document   : Form
    Created on : Nov 11, 2020, 6:34:50 PM
    Author     : ALI FCI
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Your Multiplication Table</title>
    </head>
    <style>
        table, th, td {
            border: 1px solid gray;
        }
    </style>
    <body>
        <%
            int num = Integer.parseInt(request.getParameter("num"));
        %>
        <h1 align="left">Thanks For Using Mini-Multiplication Table</h1>

        <h2 align="left">Multiplication Table Of <%=num%> :</h2>

        <table style="width:70%">
            <tr>
                <th style="background-color:red;">Multiply</th>
                    <% for (int i = 1; i <= num; i++) {
                    %>
                <th style="background-color:blue;"><% out.println(i); %> </th> 

                <%}%>

            </tr>

            <%
                try {

                    for (int i = 1; i <= num; i++) {

            %>
            <tr>
                <th align="left" style="background-color:blue;"><% out.println(i); %> </th>

                <%for (int j = 1; j <= num; j++) {%>

                <% if (j == i) { %>
                <td style="background-color:red;"><% out.println(i * j); %> </td>
                <% } %>
                <% if (j <= num - 1) { %>
                <td><% out.println(i * j); %> </td>
                <% } %>
                <% } %>
            </tr>
            <%
                    }
                }%><%

                         
            catch (Exception e) {
                        out.println("Error");
                    }
            %>

        </table>

        <br><br>
        <a href=index.html>
            <button type="button">Back</button></a>


    </body>
</html>
