<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf" %>
<c:forEach items="${tasks }" var="task">
	<tr>
		<td class="center">
			<label>
				<input type="checkbox" class="ace" />
				<span class="lbl"></span>
			</label>
		</td>
		<td>
			<a href="#">${task['CNAME'] }</a>
		</td>
		<td>${task['ANAME'] }</td>
		<td>${task['TYPE'] }</td>
		<td>${task['SUBTYPE'] }</td>
		<td class="hidden-480"><fmt:formatDate value="${task['STARTTIME'] }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
		<td class="hidden-480"><fmt:formatDate value="${task['ENDTIME'] }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
		<td>${task['DATAID'] }</td>
		<td>${task['STATUS'] }</td>
		<td>
			<div class="visible-md visible-lg hidden-sm hidden-xs btn-group">
				<a href="${ctx }/system/application/detail/${task['']}" onclick="javascript:void(0);" style="text-decoration: none;">
					<button class="btn btn-xs btn-success">
						<i class="icon-eye-open bigger-120" title="重跑"></i>
					</button>
				</a>
				<a href="${ctx }/system/task/update/${task['TASK_ID']}" onclick="javascript:void(0);" style="text-decoration: none;">
					<button class="btn btn-xs btn-info">
						<i class="icon-edit bigger-120" title="编辑"></i>
					</button>
				</a>
				<a style="text-decoration: none;">
					<button class="btn btn-xs btn-danger">
						<i class="icon-trash bigger-120" title="删除"></i>
					</button>
				</a>
			</div>
		</td>
	</tr>
</c:forEach>