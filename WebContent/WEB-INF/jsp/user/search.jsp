<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<style type="text/css">
	/*添加box的样式*/
	.easyui-textbox{border:1px solid #DDDDDD;vertical-align:middle; }
	/* 高级搜索下拉框样式*/
	.gradeSearchBox{border:1px solid #DDDDDD;width:85px;height:20px;clip:rect(0px,81px,19px,0px);overflow:hidden;} 
	.gradeSelectSearchBox{position:relative;left:1px;top:1px;font-size:15px;width:81px;line-height:20px;border:0px;color:#909993;} 
</style>
<div id="userSearchDlg">
	<form id="userSearch" method="post">
		<table>
			<tr>
				<th>条件</th>
				<th>字段名</th>
				<th>条件</th>
				<th>值</th>
			</tr>
			<tr>
				<td>
					<div class="gradeSearchBox">
						<select name="searchAnds" class="gradeSelectSearchBox"> 
							<option value="and">并且</option>
							<option value="or">或者</option>
						</select>
					</div> 
				</td>
				<td>
					<div class="gradeSearchBox">
						<select name="searchColumnNames" class="gradeSelectSearchBox">
							<option value="name">用户名</option>
							<option value="id">用户编号</option>
							<option value="registerDate">注册日期</option>
						</select>
					</div> 
				</td>
				<td>
					<div class="gradeSearchBox">
						<select name="searchConditions" class="gradeSelectSearchBox">
							<option value="=">等于</option>
							<option value="<>">不等于</option>
							<option value="<">小于</option>
							<option value=">">大于</option>
							<option value="like">模糊</option>
						</select>
					</div> 
				</td>
					<td><input id="searchVals" class="easyui-validatebox" style="height: 19px; width: 160px;" required="true" name="searchVals" size="18"> <a style="display: none;" href="javascript:void(0);" onclick="userSearchRemove(this);">删除</a>
				</td>
			</tr>
		</table>
	</form>
</div>
