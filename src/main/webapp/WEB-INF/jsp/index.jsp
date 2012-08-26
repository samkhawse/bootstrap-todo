<!doctype html>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.util.Map"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
  <head>
    <meta charset="utf-8">
    <title>Spring MVC and Hibernate Template</title>

    <meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link href="http://twitter.github.com/bootstrap/assets/css/bootstrap.css" rel="stylesheet">
    <link href="http://twitter.github.com/bootstrap/assets/css/bootstrap-responsive.css" rel="stylesheet">
    <link rel="stylesheet" href="http://twitter.github.com/bootstrap/assets/js/google-code-prettify/prettify.css">

    <style type="text/css">

    </style>
    <!-- /// -->
    <script type="text/javascript">
      <!--
      function appname() {
          return location.hostname.substring(0,location.hostname.indexOf("."));
      }
      // -->
    </script>
  </head>

  <body>
    <div class="navbar navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container" align="center">
          <a href="/" class="brand">ToDo list</a>
        </div>
      </div>
    </div>

    <div class="container" id="getting-started">
    	<div>
    		<hr>
    	</div>
  		<div align="center">
  			<form:form method="post" action="validate" commandName="user">
  				<form:label path="userName">User Name</form:label>
  				<form:input path="userName"/>
  				<form:label path="passwd">Password</form:label>
  				<form:password path="passwd"/>
  				<input type="submit" value="Login" class="btn"/>
  			</form:form>
  		</div>
  		
  		<div align="center">
  		  	<%
  		  		//String message = (String)request.getAttribute("message");
  		  		out.println("message received: "+(String)request.getAttribute("message"));
				out.println("<br>");  		  	
				Map<String, String[]> m = request.getParameterMap();
  				for (Map.Entry<String, String[]> entry: m.entrySet()){
  					out.println(entry.getKey());
  					out.println("<br>");
  						for(String s : entry.getValue()){
  							out.println(s);
  							out.println("<br>");
  						}
  				}
  			%>
  		</div>
  	</div>
    <script src="http://twitter.github.com/bootstrap/assets/js/jquery.js"></script>
    <script src="http://twitter.github.com/bootstrap/assets/js/bootstrap-modal.js"></script>
    <script src="http://twitter.github.com/bootstrap/assets/js/bootstrap-tab.js"></script>
  </body>
</html>