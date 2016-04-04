<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<!DOCTYPE html>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello world!  
</h1>

<div id="button_wrapper_div" style="width: 350px; margin-left: auto; margin-right: auto; border: 2px solid black; padding: 5px;">
	<span style="font-weight: bold">Press a button to Navigate</span>
	<br />
	<br />
	<button class="btn_age_class" id="age_in_days_btn" onclick="window.location='person_age_in_days'" type="button">Person Age in Days</button>
	<br />
	<br />
	<button id="name_btn" onclick="window.location='person/Mike'" type="button">Person Name</button>
	<br />
	<br />
	<button id="name_404_btn" onclick="window.location='person/paul'" type="button">Person Name 404</button>
	<br />
	<br />
	<button id="person_btn" onclick="window.location='person'" type="button">Person</button>
</div>
</body>
</html>
