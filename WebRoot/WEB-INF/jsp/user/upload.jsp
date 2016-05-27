<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'user.jsp' starting page</title>
  </head>
  
  <body>
    <form method="post" action="uploads.htm" enctype="multipart/form-data">
    	<input type="file" name="photos"><br/>
    	<input type="file" name="photos"><br/>
    	<input type="file" name="photos"><br/>
    	<input type="submit" />${message }
    </form>
  </body>
</html>
