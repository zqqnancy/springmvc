<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'user.jsp' starting page</title>
  </head>
  
  <body>
    <!-- 这里form的action没写就是当前的路径 -->
    <form method="post" action="login.htm">
    	username:<input type="text" name="username"/><sf:errors path="username"/><br/>
    	password:<input type="password" name="password"/><sf:errors path="password"/><br/>
    	<input type="submit" />
    </form>
  </body>
</html>
