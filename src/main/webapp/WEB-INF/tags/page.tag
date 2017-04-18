<%@tag description="Default Page template" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@attribute name="title" required="false" %>
 
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- <meta name="viewport" content="width=device-width, initial-scale=1"> -->
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>ASAPP Chat | <c:out value="${!empty title ? title : ''}"/></title>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css" rel="stylesheet">
    <link href="/resources/css/style.css" rel="stylesheet">
    
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.1/jquery.form.min.js" integrity="sha384-tIwI8+qJdZBtYYCKwRkjxBGQVZS3gGozr3CtI+5JF/oL1JmPEHzCEnIKbDbLTCer" crossorigin="anonymous"></script>
    
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
    
    <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/0.3.4/sockjs.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
    
    <script src="/resources/js/client.js"></script>
    
</head>
<body>
    <div class="container fill">
 
        <div class="header">
            <div class="nav pull-left">ASAPP Chat</div>
            <c:if test="${ !empty pageContext.request.userPrincipal }">
	            <div class="nav pull-right">
	            	${ pageContext.request.userPrincipal.name } 
	            	<c:if test="${ !empty recip }">
	            		| <a href="/app/">Main Chat</a> |
	            	</c:if>
	            	<c:choose>
	            		<c:when test="${ empty isHistory }">
	            			<c:choose>
	            				<c:when test="${ empty recip }">
	            					<a href="/app/page/0/">History</a>
	            				</c:when>
	            				<c:otherwise>
	            					<a href="/app/page/${ recip.id }/0/">History</a>
	            				</c:otherwise>
	            			</c:choose>
	            		</c:when>
	            		<c:otherwise>
	            			<c:choose>
		            			<c:when test="${ empty recip }">
		            				<a href="/app/">Chat</a>
		            			</c:when>
		            			<c:otherwise>
		            				<a href="/app/${ recip.id }/">Chat</a>
		            			</c:otherwise>
	            			</c:choose>
	            		</c:otherwise>
	            	</c:choose> |
	            	<a href="/app/logout">
	            		<button type="button" class="btn btn-danger">Logout</button>
	            	</a>
	            </div>
            </c:if>
        </div>
        <c:if test="${!empty errors}">
	        <div class="alert alert-danger">
	        	<ul>
	        		<c:forEach var="error" items="${errors}">
	        			<li>${error}</li>
	        		</c:forEach>
	        	</ul>
	        </div>
        </c:if>
      	<jsp:doBody/>
        
    </div>
</body>
</html>