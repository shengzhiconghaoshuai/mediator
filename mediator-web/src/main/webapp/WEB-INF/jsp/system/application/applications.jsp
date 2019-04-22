<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf" %>
<c:forEach items="${applications }" var="application">
	<div class="checkbox" style="float: left;">
		<label>
			<input name="applicationIds" class="ace ace-checkbox-2" type="checkbox" value="${application.id }"/>
			<span class="lbl">${application.name }</span>
		</label>
	</div>
</c:forEach>