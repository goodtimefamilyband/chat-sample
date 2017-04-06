<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>Logout</title>
</head>
<body>
	<form id="logoutForm" method="POST">
		  <input type="hidden" name="${_csrf.parameterName}"
			value="${_csrf.token}" />
	</form>
	<script type="text/javascript">
		document.getElementById('logoutForm').submit();
	</script>
</body>
</html>