<%@ page session="false"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<t:page>
    <jsp:attribute name="title">Chat</jsp:attribute>
    <jsp:body>

	<h1>Chat</h1>

	<div id="login-box">

		<h2>Create Account</h2>

		<c:if test="${not empty error}">
			<div class="alert alert-danger">${error}</div>
		</c:if>
		<c:if test="${not empty msg}">
			<div class="alert alert-info">${msg}</div>
		</c:if>
		
		<form name='loginForm'
		  action="<c:url value='create' />" method='POST'>

			<div class="jumbotron">
				<div class="row">
					<div class="col-sm-6">
						User:
					</div>
					<div class="col-sm-6">
						<input type='text' name='username' value='' class="form-control" />
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6">
						Password:
					</div>
					<div class="col-sm-6">
						<input type='password' name='password' class="form-control" />
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6">
						Password:
					</div>
					<div class="col-sm-6">
						<input type='password' name='password2' class="form-control" />
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<button name="submit" type="submit" class="btn btn-primary">Submit</button>
					</div>
					<div class="col-sm-6">
						<a href="/login">
							<button type="button" class="btn btn-success">Login page</button>
						</a>	
					</div>
					<input type="hidden" name="${_csrf.parameterName}"
					value="${_csrf.token}" />
							
				</div>
			</div>

		</form>
	</div>

    </jsp:body>
</t:page>
