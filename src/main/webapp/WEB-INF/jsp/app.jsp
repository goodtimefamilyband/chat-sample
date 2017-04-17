<%@ page session="false"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<t:page>
    <jsp:attribute name="title">Welcome!</jsp:attribute>
    <jsp:body>
    	<h1>Welcome to the app!</h1>
    	
    	<div class="app-wrapper">
	    	<div id="messages">
	    	</div>
	    	<div id="app-right">
	    		<div id="app-right-inner">
			    	<button id="btnDm" class="btn btn-primary">Direct Messages</button>
			    	<div id="notes">
			    		<div class="note-placeholder">No new messages right now</div>
			    	</div>
			    </div>
	    	</div>
	    	
    	</div>
    	
    	<form id="msgForm" method="POST" action="messages">
		    <input id="csrf" type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	    	<c:choose>
	    		
		    	<c:when test="${ empty isHistory }">
		    		<input id="txtMsg" type="text" class="txt-inline" name="msg" placeholder="Your message here"/>
		    		<button id="btnMsg" name="submsg" type="submit" class="btn btn-success">Send</button>
		    	</c:when>
		    	
		    	<c:otherwise>
		    		<div class="pager">
		    			<a href="../0/">First</a>
		    			<c:if test="${ page > 0 }">
		    				<a href="../${ page - 1 }/">Previous</a>
		    			</c:if>
		    			<c:if test="${ page < lastPage }">
		    				<a href="../${ page + 1 }/">Next</a>
		    			</c:if>
		    			<a href="../${ lastPage }/">Last</a>
		    		</div>
		    	</c:otherwise>
		    	
	    	</c:choose>
    	</form>
    	
    	<script src="/resources/js/app_common.js"></script>
    	
    	<c:if test="${ !empty appscript }">
	    <script src="/resources/js/${ appscript }.js"></script>
	    </c:if>
	    
    </jsp:body>
</t:page>