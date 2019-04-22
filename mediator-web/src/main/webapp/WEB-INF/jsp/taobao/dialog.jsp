<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf" %>
<!-- 类目映射dialog -->
<div id="addMappingDialog" class="hide" title="属性映射">
	<div class="row">
		<div class="col-xs-12">
			<div class="row">
				<fieldset>
					<label>　淘宝类目id:</label>
					<input id="tbCid" type="text" class="input-large" readonly="readonly"/>
				</fieldset>
			</div>
			<div class="space-4"></div>
			<div class="row">
				<fieldset>
					<label>淘宝类目名称:</label>
					<input id="tbCname" type="text" class="input-large" readonly="readonly"/>
				</fieldset>
			</div>
			<div class="space-4"></div>
			<div class="row">
				<fieldset>
					<label>　系统类目id:</label>
					<input id="systemCid" type="text" class="input-large"/>
				</fieldset>
			</div>
			<div class="space-4"></div>
			<div class="row">
				<fieldset>
					<label>系统类目名称:</label>
					<input id="systemCname" type="text" class="input-large"/>
				</fieldset>
			</div>
		</div><!-- /span -->
	</div><!-- /row -->
</div>
<!-- end 类目映射dialog -->

<!-- 属性映射dialog -->
<div id="addAttrDialog" class="hide" title="属性映射">
	<div class="row">
		<div class="col-xs-12">
			<div class="row">
				<fieldset>
					<label>　　　&nbsp;淘宝属性名称:</label>
					<input id="tbAttrName" type="text" class="input-large" readonly="readonly"/>
				</fieldset>
			</div>
			<div class="space-2"></div>
			<div class="row">
				<fieldset>
					<label>　　是否keyProperty:</label>
					<input id="tbKeyProp" type="text" class="input-large" readonly="readonly"/>
				</fieldset>
			</div>
			<div class="space-2"></div>
			<div class="row">
				<fieldset>
					<label>　是否salesProperty:</label>
					<input id="tbSalesProp" type="text" class="input-large" readonly="readonly"/>
				</fieldset>
			</div>
			<div class="space-2"></div>
			<div class="row">
				<fieldset>
					<label>　是否colorProperty:</label>
					<input id="tbColorProp" type="text" class="input-large" readonly="readonly"/>
				</fieldset>
			</div>
			<div class="space-2"></div>
			<div class="row">
				<fieldset>
					<label>　　　&nbsp;是否必填属性:</label>
					<input id="tbReqProp" type="text" class="input-large" readonly="readonly"/>
				</fieldset>
			</div>
			<div class="space-2"></div>
			<div class="row">
				<fieldset>
					<label>　　　&nbsp;系统属性名称:</label>
					<input id="sysAttrName" type="text" class="input-large"/>
				</fieldset>
			</div>
			<div class="space-2"></div>
			<div class="row">
				<fieldset>
					<label>　　　　&nbsp;系统属性id:</label>
					<input id="systemAttrId" type="text" class="input-large"/>
				</fieldset>
			</div>
			<div class="space-2"></div>
			<div class="row">
				<fieldset>
					<label>　　　　　　&nbsp;默认值:</label>
					<input id="defaultValue" type="text" class="input-large"/>
				</fieldset>
			</div>
		</div><!-- /span -->
	</div><!-- /row -->
</div>
<!-- end 属性映射dialog -->

<!-- 属性值映射dialog -->
<div id="addAttrValDialog" class="hide" title="属性映射">
	<div class="row">
		<div class="col-xs-12">
			<div class="row">
				<fieldset>
					<label>　&nbsp;淘宝属性值:</label>
					<input id="tbAttrVal" type="text" class="input-large" readonly="readonly"/>
				</fieldset>
			</div>
			<div class="space-2"></div>
			<div class="row">
				<fieldset>
					<label>　&nbsp;系统属性id:</label>
					<input id="sysAttrId" type="text" class="input-large"/>
				</fieldset>
			</div>
			<div class="space-2"></div>
			<div class="row">
				<fieldset>
					<label>　&nbsp;系统属性值:</label>
					<input id="systemAttrVal" type="text" class="input-large"/>
				</fieldset>
			</div>
		</div><!-- /span -->
	</div><!-- /row -->
</div>
<!-- end 属性值映射dialog -->