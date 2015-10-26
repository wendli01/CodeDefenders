<html>

<head>
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->

    <!-- Bootstrap -->
    <link href="${pageContext.request.contextPath}/html/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/html/css/gamestyle.css" rel="stylesheet">
</head>

<body>

  <%@ page import="gammut.*,java.io.*" %>
	<nav class="navbar navbar-inverse navbar-fixed-top">
  		<div class="container-fluid">
    		<div class="navbar-header">
      			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-collapse-1">
      			</button>
    		</div>
      		<div class= "collapse navbar-collapse" id="navbar-collapse-1">
          		<ul class="nav navbar-nav">
            		<a class="navbar-brand" href="/gammut/intro">GamMut</a>
          		</ul>
      		</div>
   		</div>
	</nav>

	<div id="splash">
		<h1>GamMut</h1>
		<h2>Gamification of Mutation Testing</h2>
    <form action="/gammut/intro" method="post">
      <% File resourcesFile = new File(getServletContext().getRealPath("/WEB-INF/resources")); %>
      <select name="sourcecode">
        <%
        for (String s : resourcesFile.list()) { 
          if (s.contains(".java")) {
            %><option value=<%=s.substring(0, s.length()-5)%>><%=s.substring(0, s.length()-5)%></option><%
          }
        } %>
      </select>
      <input type="submit" value="Change Code">
    </form>
		<button class="btn btn-default"><a href=attacker>Play as the Attacker</a></button><br>
		<button class="btn btn-default"><a href=defender>Play as the Defender</a></button>
	</div>

	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/bootstrap.min.js"></script>
</body>
</html>