<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello Person by name
</h1>

<P>  	name: ${person.name} <br />
		age: ${person.age}
</P>
</body>
</html>
