<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="cn.edu.hj.bean.User"%>
<%@page import="java.util.Map.Entry"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>userlist</title>
  </head>
  
  <body>
  	<a href="add.htm">Add</a>&nbsp;&nbsp;${loginUser.username }&nbsp;${movie }<br/>
  	<%Map<String,User> users = (Map<String,User>)request.getAttribute("users");
  		if(users != null){
  			for(User user : users.values()){
  	%>  
  			<%=user%>
  			&nbsp;<a href="<%=user.getUsername()%>.htm">详细</a>
  			&nbsp;<a href="update/<%=user.getUsername()%>.htm">修改</a>
  			&nbsp;<a href="delete/<%=user.getUsername()%>.htm">删除</a><br/>
  	<%  }
  			}
  	%>
  	<a href="login.htm">Login</a><br/>
  </body>
</html>
