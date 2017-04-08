<%@ page session="false"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<t:page>
    <jsp:attribute name="title">Welcome!</jsp:attribute>
    <jsp:body>
    	<h1>Welcome to the app!</h1>
    	<div id="messages"></div>
    	<form id="msgForm" method="POST" action="messages">
    		<input type="text" name="msg" placeholder="Your message here"/>
    		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    		<button id="btnMsg" name="submsg" type="submit" class="btn btn-success">Send</button>
    	</form>
    	<script type="text/javascript">
    		var client = new AsappClient({
    			onMessage: function (message) {
    				console.log(message);
    	        	$('#messages').append(message);
    	        },
    	        onModel: function(model) {
    	        	console.log(model);
    	        	for(i in model.messages) {
    	        		$('#messages').append(model.messages[i]);
    	        	}
    	        }
    		});
    		
    		/*
    		$('#msgForm').ajaxForm(function() {
    			console.log('Message submitted');
    		});
    		*/
    		
    		$('#msgForm').submit(function() {
    			$(this).ajaxSubmit();
    			return false;
    		});
    	</script>
    </jsp:body>
</t:page>