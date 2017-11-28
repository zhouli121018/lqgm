<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
		<title>主页</title>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta name="keywords" content="Modern Responsive web template, Bootstrap Web Templates, Flat Web Templates, Andriod Compatible web template, 
		Smartphone Compatible web template, free webdesigns for Nokia, Samsung, LG, SonyErricsson, Motorola web design" />
		<!-- Bootstrap Core CSS -->
		<link href="../css/bootstrap.min.css" rel='stylesheet' type='text/css' />
		<!-- Custom CSS -->
		<link href="../css/style.css" rel='stylesheet' type='text/css' />
		<!-- Graph CSS -->
		<link href="../css/lines.css" rel='stylesheet' type='text/css' />
		<link href="../css/font-awesome.css" rel="stylesheet">
		
		<!----webfonts--->
<!--  	<link href='http://fonts.useso.com/css?family=Roboto:400,100,300,500,700,900' rel='stylesheet' type='text/css'>
	-->	<!---//webfonts--->
		<!-- Nav CSS -->
		<link href="../css/custom.css" rel="stylesheet">
		<link href="../css/jqvmap.css" rel='stylesheet' type='text/css' />
		
		<!-- jQuery -->
		<script src="../js/jquery-1.9.1.min.js"></script>
		<script src="../js/ajaxfileupload.js"></script>
		<!-- Metis Menu Plugin JavaScript -->
		<script src="../js/metisMenu.min.js"></script>
		<script src="../js/custom.js"></script>
		<!-- Graph JavaScript -->
		<script src="../js/d3.v3.js"></script>
		<script src="../js/rickshaw.js"></script>
		<script type="text/javascript" src="../js/bootstrap.min.js"></script>
		<script type="text/javascript" src="../js/bootstrap-paginator.min.js"></script>
		
	</head>

	<body>
		<div id="wrapper">
			<div class="navbar-default sidebar" role="navigation">
				<div class="base_info">
					<span class="info_item">账号：<span id="username" class="info_items"></span></span>
					<span  class="info_item">微信号：<span id="sweixin" class="info_items"></span></span>
					<span class="info_item">QQ：<span id="sqq" class="info_items"></span></span>
					<span  class="info_item">手机号：<span id="tel"  class="info_items"></span></span>
					<span  class="info_item">绑定的玩家ID：<span id="uuid"  class="info_items"></span></span>
					<span class="info_item">
						<span id="tel"  class="info_items"><a href="#updatePwdModal"  data-toggle="modal">修改资料</a></span>
						<span id="tel"  class="info_items"><a href="#" onclick="logout()">退出登录</a></span>
					</span>
					<!-- <button  onclick="logout()">退出登录</button> -->
				</div>
				<div class="base_info" id="mobileUserInfo">
					<span class="info_item">账号：<span id="username_mobile" class="info_item"></span></span>
					<span  class="info_item">微信号：<span id="sweixin_mobile" class="info_item"></span></span>
					<span class="info_item">QQ：<span id="sqq_mobile" class="info_item"></span></span>
					<span  class="info_item">手机号：<span id="tel_mobile"  class="info_item"></span></span>
					<span  class="info_item">绑定的玩家ID：<span id="uuid_mobile"  class="info_item"></span></span>
					<span class="info_item">
						<span id="tel"  class="info_item"><a href="#updatePwdModal"  data-toggle="modal">修改资料</a></span>
						<span id="tel"  class="info_item"><a href="#" onclick="logout()">退出登录</a></span>
					</span>
				</div>
				<div class="mobile_display" onclick="userinfo()" style="top:400px; left:20px;z-index:9999;line-height:40px;text-align:center;position:fixed;width:40px;height:40px;border-radius:50%;background:rgba(0,212,0,0.5);color:#ffffff" >
				个人
				</div>
				<div class="sidebar-nav navbar-collapse">
					<ul class="nav" id="side-menu">

						<li class="tltle_nav">
							<a href="#user" onclick="selectPage('user')"><i class="fa fa-envelope nav_icon"></i>玩家管理<span class="fa arrow"></span></a>
						</li>

						<li class="tltle_nav">
							<a onclick="selectPage('account')" href="#proxy"><i class="fa fa-envelope nav_icon"></i>代理管理<span class="fa arrow"></span></a>
						</li>
						
						<li class="tltle_nav">
							<a onclick="selectPage('paylog')" href="#chargeDetail"><i class="fa fa-envelope nav_icon"></i>充值详情<span class="fa arrow"></span></a>
						</li>
						<li class="tltle_nav">
							<a onclick="selectPage('paylogsum')" href="#shareStics"><i class="fa fa-envelope nav_icon"></i>分成统计<span class="fa arrow"></span></a>
						</li>
						<li class="tltle_nav">
							<a onclick="selectPage('tixianlog')" href="#tixian"><i class="fa fa-envelope nav_icon"></i>提现记录<span class="fa arrow"></span></a>
						</li>
					</ul>
				</div>

			</div>
			<nav style="font-size:10px;height: 50px ;line-height: 50px;background: #FFFDE7;width: 100%; position:fixed;bottom:0px;z-index:9999" class="mobile_display"  id="mobile">
			</nav>

			<div id="page-wrapper">
				<div class="graphs" id="user">
					<div class="col_3">
						<div class="sub_title">会员管理
							<button  style="float: right; line-height: 30px; margin-top: 5px; background: none;border:1px solid black;border-radius:6px;margin-right:15px" onclick="searchAccount()">搜索玩家</button>
							<input id="accountUuid"  type="text"  value="请输入玩家id" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = '请输入玩家id';}" style="float: right;line-height: 30px;color:black; margin-top: 5px; margin-right: 15px;" />

						</div>

						<table class="table" style="border-top: 1px #C0C0C0 solid;margin-top: 20px;">
							<thead>
								<tr class="success">
									<th>用户id</th>
									<th>用户名</th>
									<th>剩余房卡</th>
									<th>状态</th>
									<th>创建时间</th>
									<th>
										设置
									</th>
								</tr>
							</thead>
							<tbody id="userTbody">
							</tbody>
						</table>
					</div>
					<div class="col_3">
					<ul id="userPageItems"></ul>
					</div>
				</div>

				
				<div class="graphs" id="account">
					<div class="col_3">
						<div class="sub_title">代理管理
						</div>
						<table class="table" style="border-top: 1px #C0C0C0 solid;margin-top: 20px;">
							<thead>
								<tr class="success">
									<th>账号</th>
								 	<th>编码</th> 
									<th>手机号</th>
									<th>微信号</th>
									<th>QQ</th>
									<th>类型</th>
									<th>邀请码</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody id="accountTbody">
							</tbody>
						</table>
					</div>
					<div class="col_3">
					<ul id="accountPageItems"></ul>
					</div>
				</div>
				
				<div class="graphs" id="paylog">
					<div class="col_3">
						<div class="sub_title">充值流水
						<button style="float: right; line-height: 30px; margin-top: 5px;" onclick="getPaylog(1,5)">今天充值</button>
						<button style="float: right; line-height: 30px; margin-top: 5px;" onclick="getPaylog(1,6)">昨天充值</button>
						<button style="float: right; line-height: 30px; margin-top: 5px;" onclick="getPaylog(1,1)">本周充值</button>
						<button style="float: right; line-height: 30px; margin-top: 5px;" onclick="getPaylog(1,2)">上周充值</button>
						<button style="float: right; line-height: 30px; margin-top: 5px;" onclick="getPaylog(1,3)">本月充值</button>
						<button style="float: right; line-height: 30px; margin-top: 5px;" onclick="getPaylog(1,4)">上月充值</button>
						</div>
						<table class="table" style="border-top: 1px #C0C0C0 solid;margin-top: 20px;">
							<thead>
								<tr class="success">
									<th>充值账号</th>
									<th>充值金额</th>
									<th>代理ID</th>
									<th>钻石数量</th>
									<th>充值时间</th>
								</tr>
							</thead>
							<tbody id="paylogTbody">
							</tbody>
						</table>
						<ul id="paylogPageItems">
						</ul>
					</div>

				</div>
				
				
				<div class="graphs" id="paylogsum">
					<div class="col_3">
						<div class="sub_title">充值统计
						<button style="float: right; line-height: 30px; margin-top: 5px;" onclick="getPaylogSum(5)">今天充值</button>
						<button style="float: right; line-height: 30px; margin-top: 5px;" onclick="getPaylogSum(6)">昨天充值</button>
						<button style="float: right; line-height: 30px; margin-top: 5px;" onclick="getPaylogSum(1)">本周充值</button>
						<button style="float: right; line-height: 30px; margin-top: 5px;" onclick="getPaylogSum(2)">上周充值</button>
						<button style="float: right; line-height: 30px; margin-top: 5px;" onclick="getPaylogSum(3)">本月充值</button>
						<button style="float: right; line-height: 30px; margin-top: 5px;" onclick="getPaylogSum(4)">上月充值</button>
						</div>
						<table class="table" style="border-top: 1px #C0C0C0 solid;margin-top: 20px;">
							<thead>
								<tr class="success">
									<th>充值统计</th>
									<th id="sumtotal"></th>
								</tr>
							</thead>
							<tbody id="paylogsumTbody">
							<tr class="success">
									<td>下级用户充值</td>
									<td id="sumone"></td>
								</tr>
								<tr class="success">
									<td>下级代理充值</td>
									<td id="sumtwo"></td>
								</tr>
							</tbody>
						</table>
						<div id="tixian"><button style="float: right; line-height: 30px; margin-top: 5px;" onclick="tixian()">全部提现</button>
						<span id="tixianReturn"></span>
						</div>
					</div>

				</div>
				
				<div class="graphs" id="tixianlog">
					<div class="col_3">
						<div class="sub_title">提现流水
						<button style="float: right; line-height: 30px; margin-top: 5px;" onclick="getTixianlog(1,5)">今天提现</button>
						<button style="float: right; line-height: 30px; margin-top: 5px;" onclick="getTixianlog(1,6)">昨天提现</button>
						
						<button style="float: right; line-height: 30px; margin-top: 5px;" onclick="getTixianlog(1,1)">本周提现</button>
						<button style="float: right; line-height: 30px; margin-top: 5px;" onclick="getTixianlog(1,2)">上周提现</button>
						<button style="float: right; line-height: 30px; margin-top: 5px;" onclick="getTixianlog(1,3)">本月提现</button>
						<button style="float: right; line-height: 30px; margin-top: 5px;" onclick="getTixianlog(1,4)">上月提现</button>
						</div>
						<table class="table" style="border-top: 1px #C0C0C0 solid;margin-top: 20px;">
							<thead>
								<tr class="success">
									<th>提现账号</th>
									<th>提现金额</th>
									<th>是否处理</th>
									<!--<th>钻石数量</th> -->
									<th>提现时间</th>
								</tr>
							</thead>
							<tbody id="tixianlogTbody">
							</tbody>
						</table>
						<ul id="tixianlogPageItems">
						</ul>
					</div>

				</div>
				

				<!-- Modal  增加新用户账户-->
				<!-- 修改个人信息 -->
				<div id="updatePwdModal" class="modal fade modal_style"  style="width:350px;height:400px"  tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
						<h3 id="myInfoLabel" style="color: #22BAA0;">修改资料</h3>
					</div>
					<div class="modal-body" style="padding: 30px; height: 260px;">
						<div style="clear: both;margin-top: 0px;">
							<span style="float: left;">旧密码：<input  id="oldPwd" placeholder="输入旧密码"/></span>
							
							
						</div>
						<div style="clear: both;margin-top: 40px;">
							<span style="float: left;">新密码：<input id="newOnePwd"  placeholder="输入新用户密码"/></span>
							
						</div>
						<div style="clear: both;margin-top: 80px;">
							
							<span style="float: left;">微信号：<input id="weixin"  placeholder="输入微信号"/></span>
						</div>
						
						<div style="clear: both;margin-top: 120px;">
							
							<span style="float: left;">新手机号：<input id="oneTel"  placeholder="输入新手机号"/></span>
						</div>
						<div style="clear: both;margin-top: 160px;">
							
							
							<span style="float: left;">QQ：<input id="qq"  placeholder="输入你的QQ号码"/></span>
						</div>
						<div style="clear: both;margin-top: 200px;">
							<span style="float: left;">真实姓名：<input id="newTrueName" placeholder="输入真实姓名"/></span>
							
							
						</div>
					</div>
					<div class="modal-footer">
					<div style="clear: both;margin-top: 0px;">
						<button class="btn btn-primary" onclick="saveManagerInfo()" data-dismiss="modal" aria-hidden="true">保存</button>
						<button class="btn" data-dismiss="modal" aria-hidden="true">取消</button>
						</div>
					</div>
					
				</div>
				
				
				<!-- 删除用户对话框 -->
				<div id="deleteModal" class="modal fade modal_style"  tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
						<h3 id="myModalLabel" style="color: red;">警告</h3>
					</div>
					<div class="modal-body" style="padding: 30px; height: 120px;">
						删除后账户数据将不能找回！你确认要删除吗？
					</div>
					<div class="modal-footer">
						<button class="btn" data-dismiss="modal" aria-hidden="true">取消</button>
						<button class="btn btn-primary">确认</button>
					</div>
				</div>

				<!-- 设置用户状态对话框 -->
				<div id="setModal" class="modal fade modal_style" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
						<h3 id="myModalLabel" style="color: #22BAA0;">设置</h3>
					</div>
					<div class="modal-body" style="padding: 30px; height: 120px;">
						<span>设置用户状态 </span>
						<select id="accountStatus">
							<option value="0">激活可用</option>
							<option value="1">禁用账户</option>
						</select>
					</div>
					<div class="modal-footer">
						<button class="btn" data-dismiss="modal" aria-hidden="true">取消</button>
						<button class="btn btn-primary" onclick="updateAccountStatus()"  data-dismiss="modal" aria-hidden="true">确认</button>
					</div>
				</div>

				<!-- 充值模块 -->
				<!-- 找回代理商密码 -->
				<div id="resetManagerPwd" class="modal fade modal_style"  tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
						<h3 id="resetProxyPwdLabel" style="color: #22BAA0;">找回密码</h3>
					</div>
					<div class="modal-body" style="padding: 30px; height: 60px;">
						<div style="clear: both;">
							<span style="float: left;">新密码：<input  id="resetOnePwd" placeholder="输入新密码"/></span>
							<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;新密码：<input id="resetTwoPwd"  placeholder="再次输入新密码"/></span>
						</div>
					</div>
					<div class="modal-footer">
						<button class="btn" data-dismiss="modal" aria-hidden="true">取消</button>
						<button class="btn btn-primary" data-dismiss="modal" aria-hidden="true" onclick="resetManagerPwd()">确认</button>
					</div>
				</div>
				
				
				

			</div>

		</div>

	</body>
	<!-- <script type="application/x-javascript"> -->
	<script type="text/javascript">
		var currentId = 1;
		var userId = 0;
		var managerId = 0;
		var totalProbability = parseInt("0");//奖品概率总分
		var oneProbability = parseInt("0");//某一个奖品概率
		var oneName = "ddd";//奖励名称
		var flag = 0;//判断修改之后是否保存了
		var afterUpImg;//图片上传之后返回的图片路径
		
		$(function(){
		    $("#mobileUserInfo").css("display","none");
			currentId = "<%=session.getAttribute("id")%>";
			//判断是超管才能查看全部功能
			if("<%=session.getAttribute("type")%>" != "1"){
				//非超管
				//$("#side-menu li:lt(3)").remove();
				//$("#newManagerType").append("<option value='2' style='color: #333;' selected='selected' >代理商</option>")
				
				
				var  a="<a href='#user' onclick=selectPage('user') style='width: 15%;text-align: center;display: inline-block;'>玩家管理</a>";
					  a+="<a href='#proxy' onclick=selectPage('account') style='width: 15%;text-align: center;display: inline-block;'>代理管理</a>";
					  a+="<a href='#chargeDetail' onclick=selectPage('paylog') style='width: 15%;text-align: center;display: inline-block;'>充值详情</a>";
					  a+="<a href='#shareStics' onclick=selectPage('paylogsum') style='width: 15%;text-align: center;display: inline-block;'>分成统计</a>";
					  a+="<a href='#tixian' onclick=selectPage('tixianlog') style='width: 15%;text-align: center;display: inline-block;'>提现记录</a>";
				$("#mobile").append(a);
				
				selectPage("user");
				
			}
			
			
			
			//登录管理员信息
			$("#username").text("<%=session.getAttribute("name")%>");
			$("#sweixin").text("<%=session.getAttribute("weixin")%>");
			$("#sqq").text("<%=session.getAttribute("qq")%>");
			$("#tel").text("<%=session.getAttribute("tel")%>");
			$("#uuid").text("<%=session.getAttribute("uuid")%>");
			//手机上
			$("#username_mobile").text("<%=session.getAttribute("name")%>");
			$("#sweixin_mobile").text("<%=session.getAttribute("weixin")%>");
			$("#sqq_mobile").text("<%=session.getAttribute("qq")%>");
			$("#tel_mobile").text("<%=session.getAttribute("tel")%>");
			$("#uuid_mobile").text("<%=session.getAttribute("uuid")%>");
			
		})
		init("user");
		var lastDisplayPage;

		function init(displayPage) {
			lastDisplayPage = displayPage;
			document.getElementById(lastDisplayPage).style.display = "block";
		}
		
		function getPaylog(pageIndex,type){
			$.ajax({	
				   type: "POST",
				   url: "<%=basePath%>controller/manager/getPaylogs?pageNum="+pageIndex+"&type="+type,
				   data: "",
				   success: function data(data){
					   var returns =  eval('(' + data + ')');
						var param = returns.paylogs;
						var totalPages = returns.totalNum;
						$("#paylogTbody").html("");
				        $.each(param, function (i, item) {
				        	var td = "<td>"+param[i].uuid+"</td>";
							/* td+="<td>****</td>"; */
							/*td+="<td>"+param[i].id+"</td>";*/
							td+="<td>"+param[i].money+"</td>";
							td+="<td>"+param[i].managerid+"</td>";
							td+="<td>"+param[i].paycount+"</td>";
							td+="<td>"+param[i].dateString+"</td>";

							//添加每行数据
							$("#paylogTbody").append("<tr>"+td+"</tr>");
				          }); 
				        if(totalPages>1){
				        	var options = {
				                    currentPage: pageIndex,
				                    totalPages: totalPages,
				                    bootstrapMajorVersion: 3,
				                    onPageChanged: function(e,oldPage,newPage){
				                    	getPaylog(newPage,type);
				                    }
				                }

				                $('#paylogPageItems').bootstrapPaginator(options);
				        }else{
				        	 $('#paylogPageItems').html("");
				        }
					}
				});
		}
		
		
		function getAccount(pageIndex,type){
			$.ajax({	
				   type: "POST",
				   url: "<%=basePath%>controller/manager/getManagers?pageNum="+pageIndex,
				   data: "",
				   success: function data(data){
					   var returns =  eval('(' + data + ')');
						var param = returns.managers;
						var totalPages = returns.totalNum;
						$("#accountTbody").html("");
				        $.each(param, function (i, item) {
				              var td = "<td>"+param[i].name+"</td>";
											/* td+="<td>****</td>"; */
											td+="<td>"+param[i].id+"<a href='<%=basePath%>controller/manager/proxyDetail?managerId="+param[i].id+"' target='_blank'>查看 </a></td>";
											td+="<td>"+param[i].telephone+"</td>";
											td+="<td>"+param[i].weixin+"</td>";
											td+="<td>"+param[i].qq+"</td>";
											if(param[i].powerId <5){
												td+="<td>普通代理</td>";
											}
											else if(param[i].powerId ==5){
												td+="<td>顶级代理商</td>";
											}
											if(param[i].inviteCode >0 ){
												td+="<td>"+param[i].inviteCode+"</td>";
											}
											else{
												td+="<td>临时代理</td>";
											}
											if(currentId == "1"){
												td+="/<a href='#reduceRoomCardModal' data-toggle='modal' onclick=getManagerId("+param[i].id+")>退卡 </a>";
											}
											td+="</th>";
											td+="<td><a href='#deleteModa' data-toggle='modal' onclick=deleteProxy("+param[i].id+")>删除 </a>";
											if(currentId == "1"){
												td+="&nbsp;&nbsp;/&nbsp;&nbsp;<a href='#resetManagerPwd' data-toggle='modal' onclick=getManagerId("+param[i].id+")>找回密码</a>";
											}
											td+="</td>";
							//添加每行数据
							$("#accountTbody").append("<tr>"+td+"</tr>");
				          }); 
				        
				        if(totalPages>1){
				        	var options = {
				                    currentPage: pageIndex,
				                    totalPages: totalPages,
				                    bootstrapMajorVersion: 3,
				                    onPageChanged: function(e,oldPage,newPage){
				                    	getAccount(newPage,type);
				                    }
				                }

				                $('#accountPageItems').bootstrapPaginator(options);
				        }else{
				        	 $('#accountPageItems').html("");
				        }
				        
					}
				});
		}
		
		
		function getUser(pageIndex,type){
			$.ajax({	
				   type: "POST",
				   url: "<%=basePath%>controller/manager/getAccounts?type=proxy&pageNum="+pageIndex,
				   data: "",
				   success: function data(data){
					   var returns =  eval('(' + data + ')');
						var param = returns.users;
						var totalPages = returns.totalNum;
						$("#userTbody").html("");
				        $.each(param, function (i, item) {  
				              var td = "<td>"+param[i].uuid+"</td>";
											td+="<td style='max-width:30px;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;'>"+param[i].nickname+"</td>";
											td+="<td>"+param[i].roomcard+"</td>";
											if(param[i].status =="0"){
												td+="<td>正常</td>";
											}else{
												td+="<td>注销</td>";
											}	
											td+="<td>"+param[i].createTimeCN+"</td>";
											td+="<th><a href='#setModal' data-toggle='modal'  onclick=getUserId("+param[i].id+")>设置 </a></th>";
											td+="</th>";
							//添加每行数据
							$("#userTbody").append("<tr>"+td+"</tr>");
				          }); 
				        if(totalPages>1){
				        	var options = {
				                    currentPage: pageIndex,
				                    totalPages: totalPages,
				                    bootstrapMajorVersion: 3,
				                    onPageChanged: function(e,oldPage,newPage){
				                    	getUser(newPage,0);
				                    }
				                }

				                $('#userPageItems').bootstrapPaginator(options);
				        }else{
				        	 $('#userPageItems').html("");
				        }
					}
				});
		}
		
		function getTixianlog(pageIndex,type){
			$.ajax({	
				   type: "POST",
				   url: "<%=basePath%>controller/manager/getPaylogs?pageNum="+pageIndex+"&payType=1&type="+type,
				   data: "",
				   success: function data(data){
					   var returns =  eval('(' + data + ')');
						var param = returns.paylogs;
						var totalPages = returns.totalNum;
						$("#tixianlogTbody").html("");
				        $.each(param, function (i, item) {
				        	var td = "<td>"+param[i].uuid+"</td>";
				        	td+="<td>"+param[i].money+"</td>";
							/* td+="<td>****</td>"; */
				        	if(param[i].status==1){
								td+="<td>已完成</td>";
							}else{
								td+="<td>提现失败</td>";
							}
							
							td+="<td>"+param[i].dateString+"</td>";

							//添加每行数据
							$("#tixianlogTbody").append("<tr>"+td+"</tr>");
				          }); 
				        if(totalPages>1){
				        	var options = {
				                    currentPage: pageIndex,
				                    totalPages: totalPages,
				                    bootstrapMajorVersion: 3,
				                    onPageChanged: function(e,oldPage,newPage){
				                    	getTixianlog(newPage,type);
				                    }
				                }

				                $('#tixianlogPageItems').bootstrapPaginator(options);
				        }else{
				        	 $('#tixianlogPageItems').html("");
				        }
					}
				});
		}
		
		function tixian(){
			$.ajax({	
				   type: "POST",
				   url: "<%=basePath%>controller/manager/tixian",
				   data: "",
				   success: function data(data){
					   var returns =  eval('(' + data + ')');
					   if(typeof(returns.total)!="undefined"){ 
						   $("#tixianReturn").html(returns.info);
						   }else{
							   $("#tixianReturn").html(returns.error);
						   } 
						


					}
				});
		}
		
		function getPaylogSum(type){
			$.ajax({	
				   type: "POST",
				   url: "<%=basePath%>controller/manager/getPaylogsSum?type="+type,
				   data: "",
				   success: function data(data){
					   var returns =  eval('(' + data + ')');
						var total = returns.total;
						var mineone = returns.mineone;
						var minetwo = returns.minetwo;
						$("#sumtotal").html(total);
						$("#sumone").html(mineone);
						$("#sumtwo").html(minetwo);
						$("#tixian").css("display", "none"); 
					}
				});
		}

		function selectPage(clickPage) {
			if(clickPage == "user"){
				//游戏会员管理
				$.ajax({	
				   type: "POST",
				   url: "<%=basePath%>controller/manager/getAccounts?type=proxy&pageNum=1",
				   data: "",
				   success: function data(data){
					   var returns =  eval('(' + data + ')');
						var param = returns.users;
						var totalPages = returns.totalNum;
						$("#userTbody").html("");
				        $.each(param, function (i, item) {  
				              var td = "<td>"+param[i].uuid+"</td>";
											td+="<td style='max-width:30px;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;'>"+param[i].nickname+"</td>";
											td+="<td>"+param[i].roomcard+"</td>";
											if(param[i].status =="0"){
												td+="<td>正常</td>";
											}else{
												td+="<td>注销</td>";
											}	
											td+="<td>"+param[i].createTimeCN+"</td>";
											td+="<th><a href='#setModal' data-toggle='modal'  onclick=getUserId("+param[i].id+")>设置 </a></th>";
											td+="</th>";
							//添加每行数据
							$("#userTbody").append("<tr>"+td+"</tr>");
				          }); 
				        if(totalPages>1){
				        	var options = {
				                    currentPage: 1,
				                    totalPages: totalPages,
				                    bootstrapMajorVersion: 3,
				                    onPageChanged: function(e,oldPage,newPage){
				                    	getUser(newPage,0);
				                    }
				                }

				                $('#userPageItems').bootstrapPaginator(options);
				        }else{
				        	 $('#userPageItems').html("");
				        }
					}
				});
			}
		    
		    if(clickPage == "account"){
		    	//账号管理，首先获取自己下面的所有代理商，零售商  
		    	$.ajax({	
				   type: "POST",
				   url: "<%=basePath%>controller/manager/getManagers?pageNum=1",
				   data: "",
				   success: function data(data){
					   var returns =  eval('(' + data + ')');
						var param = returns.managers;
						var totalPages = returns.totalNum;
						$("#accountTbody").html("");
				        $.each(param, function (i, item) {
				              var td = "<td>"+param[i].name+"</td>";
											/* td+="<td>****</td>"; */
											td+="<td>"+param[i].id+"<a href='<%=basePath%>controller/manager/proxyDetail?managerId="+param[i].id+"' target='_blank'>查看 </a></td>";
											td+="<td>"+param[i].telephone+"</td>";
											td+="<td>"+param[i].weixin+"</td>";
											td+="<td>"+param[i].qq+"</td>";
											if(param[i].powerId <5){
												td+="<td>普通代理</td>";
											}
											else if(param[i].powerId ==5){
												td+="<td>顶级代理商</td>";
											}
											if(param[i].inviteCode >0 ){
												td+="<td>"+param[i].inviteCode+"</td>";
											}
											else{
												td+="<td>临时代理</td>";
											}
											if(currentId == "1"){
												td+="/<a href='#reduceRoomCardModal' data-toggle='modal' onclick=getManagerId("+param[i].id+")>退卡 </a>";
											}
											td+="</th>";
											td+="<td><a href='#deleteModa' data-toggle='modal' onclick=deleteProxy("+param[i].id+")>删除 </a>";
											if(currentId == "1"){
												td+="&nbsp;&nbsp;/&nbsp;&nbsp;<a href='#resetManagerPwd' data-toggle='modal' onclick=getManagerId("+param[i].id+")>找回密码</a>";
											}
											td+="</td>";
							//添加每行数据
							$("#accountTbody").append("<tr>"+td+"</tr>");
				          }); 
				        
				        if(totalPages>1){
				        	var options = {
				                    currentPage: 1,
				                    totalPages: totalPages,
				                    bootstrapMajorVersion: 3,
				                    onPageChanged: function(e,oldPage,newPage){
				                    	getAccount(newPage,0);
				                    }
				                }

				                $('#accountPageItems').bootstrapPaginator(options);
				        }else{
				        	 $('#accountPageItems').html("");
				        }
				        
					}
				});
		    }
		    
		    if(clickPage == "paylog"){
		    	//账号管理，首先获取自己下面的所有代理商，零售商  
		    	$.ajax({	
				   type: "POST",
				   url: "<%=basePath%>controller/manager/getPaylogs?pageNum=1&payType=0",
				   data: "",
				   success: function data(data){
					   var returns =  eval('(' + data + ')');
						var param = returns.paylogs;
						var totalPages = returns.totalNum;
						$("#paylogTbody").html("");
				        $.each(param, function (i, item) {
				              var td = "<td>"+param[i].uuid+"</td>";
											/* td+="<td>****</td>"; */
											/*td+="<td>"+param[i].id+"</td>";*/
											td+="<td>"+param[i].money+"</td>";
											td+="<td>"+param[i].managerid+"</td>";
											td+="<td>"+param[i].paycount+"</td>";
											td+="<td>"+param[i].dateString+"</td>";

							//添加每行数据
							$("#paylogTbody").append("<tr>"+td+"</tr>");
				          }); 
				        
				        if(totalPages>1){
				        	var options = {
				                    currentPage: 1,
				                    totalPages: totalPages,
				                    bootstrapMajorVersion: 3,
				                    onPageChanged: function(e,oldPage,newPage){
				                    	getPaylog(newPage,0);
				                    }
				                }

				                $('#paylogPageItems').bootstrapPaginator(options);
				        }else{
				        	 $('#paylogPageItems').html("");
				        }
					}
				});
		    }
		    
		    if(clickPage == "tixianlog"){
		    	//账号管理，首先获取自己下面的所有代理商，零售商  
		    	$.ajax({	
				   type: "POST",
				   url: "<%=basePath%>controller/manager/getPaylogs?pageNum=1&&payType=1",
				   data: "",
				   success: function data(data){
					   var returns =  eval('(' + data + ')');
						var param = returns.paylogs;
						var totalPages = returns.totalNum;
						$("#tixianlogTbody").html("");
				        $.each(param, function (i, item) {
				              var td = "<td>"+param[i].uuid+"</td>";
											/* td+="<td>****</td>"; */
											td+="<td>"+param[i].money+"</td>";
											if(param[i].status==1){
												td+="<td>已完成</td>";
											}else{
												td+="<td>提现失败</td>";
											}
											
											td+="<td>"+param[i].dateString+"</td>";

							//添加每行数据
							$("#tixianlogTbody").append("<tr>"+td+"</tr>");
				          }); 
				        
				        if(totalPages>1){
				        	var options = {
				                    currentPage: 1,
				                    totalPages: totalPages,
				                    bootstrapMajorVersion: 3,
				                    onPageChanged: function(e,oldPage,newPage){
				                    	getTixianlog(newPage,0);
				                    }
				                }

				                $('#tixianlogPageItems').bootstrapPaginator(options);
				        }else{
				        	 $('#tixianlogPageItems').html("");
				        }
					}
				});
		    }
		    
		    if(clickPage == "paylogsum"){
		    	//账号管理，首先获取自己下面的所有代理商，零售商  
		    	$.ajax({	
				   type: "POST",
				   url: "<%=basePath%>controller/manager/getPaylogsSum?type=0",
				   data: "",
				   success: function data(data){
					   var returns =  eval('(' + data + ')');
						var total = returns.total;
						var mineone = returns.mineone;
						var minetwo = returns.minetwo;
						$("#sumtotal").html(total);
						$("#sumone").html(mineone);
						$("#sumtwo").html(minetwo);
						$("#tixian").css("display", "block"); 
					}
				});
		    }
		    
		    
			if(clickPage != lastDisplayPage) {
				document.getElementById(clickPage).style.display = "block";
				document.getElementById(lastDisplayPage).style.display = "none";
				lastDisplayPage = clickPage;
			}
		}
		
		function saveManager(){
			//新增代理商/零售商 保存
			$.ajax({	
				   type: "POST",
				   url: "<%=basePath%>controller/manager/addProxyAccount",
				   data: "newManagerName="+$("#newManagerName").val()+"&newManagerPwd="+$("#newManagerPwd").val()+"&newManagerTel="+$("#newManagerTel").val()+"&newManagerType="+$("#newManagerType").val(),
				   success: function data(data){
					   //成功返回之后重置userId,managerId
					  var param  = eval('('+data+')');
					  if(param.status == 0){
					  	//成功
					  		alert("增加用户成功!");
					  		$("#newManagerName").val("");
							$("#newManagerPwd").val("");
							$("#newManagerTel").val("");
							$("#newManagerType").val("");
					  }
					  else{
					  	//失败处理
					  	alert(param.error);
					  }
				   	  selectPage("account");
				   }
			});
		}
		
		function getUserId(userid){
			userId = userid;
			managerId = 0;
			$("#reduceAccountCardNum").val("");
			$("#payCardNum").val("");
		}
		function getManagerId(managerid){
			managerId = managerid;
			userId = 0;
			$("#payCardNum").val("");
			$("#reduceManagerCardNum").val("");
		}
		
		
		//充房卡信息
		function saveContactway(){
			var newContact = $("#newContact").val();
				$.ajax({	
				   type: "POST",
				   url: "<%=basePath%>controller/manager/updateContactway",
				   data: "newContact="+newContact,
				   success: function data(data){
				   var param = eval('('+data+')');
							if(param.status_code == "0"){
							  	alert("信息修改成功");
							}
							else{
							 	alert(param.error);
							}
				   	}
				});
		}
		
		//搜索玩家功能
		function searchAccount(){
			var accountUuid = $("#accountUuid").val();
			if(accountUuid != "请输入玩家id" && accountUuid != ""){
				$.ajax({	
				   type: "POST",
				   url: "<%=basePath%>controller/manager/searchAccountByUuid",
				   data: "accountUuid="+accountUuid,
				   success: function data(data){
				 			var param = eval('('+data+')');
				 			//超级管理员需要先移除列表
							if(param.status_code == "0"){
				 				$("#userTbody").html("");
							     var td = "<td>"+param.data.uuid+"</td>";
									    td+="<td>"+param.data.nickname+"</td>";
										td+="<td>"+param.data.roomcard+"</td>";
											if(param.data.status =="0"){
												td+="<td>正常</td>";
											}else{
												td+="<td>注销</td>";
											}	
											td+="<th><a href='#setModal' data-toggle='modal' onclick=getUserId("+param.data.id+")>设置 </a></th>";
											td+="<th><a href='#rechargeModal' data-toggle='modal' onclick=getUserId("+param.data.id+")>充值 </a>/<a href='#reduceAccountRoomCardModal' data-toggle='modal' onclick=getUserId("+param.data.id+")>退卡 </a></th>";
									//添加每行数据
									$("#userTbody").append("<tr>"+td+"</tr>");
							}
							else{
							 	alert(param.error);
							}
				   	}
				});
			}
			else{
				alert("请输入玩家id");
			}
		}
        
         
         //修改玩家状态 status
         function updateAccountStatus(){
         	if(confirm("确认删除该用户？")){
	         	$.ajax({	
					   type: "POST",
					   url: "<%=basePath%>controller/manager/updateAccountStatus",
					   data: "accountId="+userId+"&status="+$("#accountStatus").val(),
					   success: function data(data){
					 			var param = eval('('+data+')');
								if(param.status == "0"){
									  alert("删除成功");
									  selectPage("user");
								}
								else{
								 	alert(param.error);
								}
					   	}
					});
         	}
         } 
         
         //删除代理商
         function deleteProxy(managerid){
         if(confirm("确认删除该代理商?")){
	         	$.ajax({	
					   type: "POST",
					   url: "<%=basePath%>controller/manager/updateManagerStatus",
					   data: "manangerId="+managerid,
					   success: function data(data){
					 			var param = eval('('+data+')');
								if(param.status == "0"){
									  alert("删除成功");
									  selectPage("account");
								}
								else{
								 	alert(param.error);
								}
					   	}
					});
         	}
         }
         //充值代理商密码
         function resetManagerPwd(){
         	var resetOnePwd = $("#resetOnePwd").val();
         	var resetTwoPwd = $("#resetTwoPwd").val();
         	if(resetOnePwd == resetTwoPwd){
	         	$.ajax({	
					   type: "POST",
					   url: "<%=basePath%>controller/manager/updateManagerPwd",
					   data: "managerId="+managerId+"&newPwd="+resetOnePwd,
					   success: function data(data){
					 			var param = eval('('+data+')');
								if(param.status_code == "0"){
									  alert("修改成功");
									  selectPage("account");
									  $("#resetOnePwd").val("");
         								$("#resetTwoPwd").val("");
								}
								else{
								 	alert(param.error);
								}
					   	}
					});
         	}
         	else{
         		alert("两次密码必须一致!");
         	}
         }
         
         
         //管理员密码账号修改保存
         function saveManagerInfo(){
         		var oldPwd = $("#oldPwd").val();
         		//var oldTel = $("#oldTel").val();
         		var newOnePwd = $("#newOnePwd").val();
         		var oneTel = $("#oneTel").val();
         		var newTwoPwd = $("#newTwoPwd").val();
         		var weixin = $("#weixin").val();
         		var qq = $("#qq").val();
         		var name = $("#newTrueName").val();
         		//var twoTel = $("#twoTel").val();
         		if(true){
         			$.ajax({	
					   type: "POST",
					   url: "<%=basePath%>controller/manager/updateManagerInfo",
					   data: "oldPwd="+oldPwd+"&newPwd="+newOnePwd+"&oneTel="+oneTel+"&weixin="+weixin+"&qq="+qq+"&name="+name,
					   success: function data(data){
					 			var param = eval('('+data+')');
								if(param.status == "0"){
									  alert("资料修改成功");
									  
								}
								else{
								 	alert(param.error);
								}
								//清除数据,
								$("#oldPwd").val("");
				         		$("sweixin").val("");
				         		$("#newOnePwd").val("");
				         		$("#oneTel").val("");
				         		$("#newTwoPwd").val("");
				         		$("#qq").val("");
				         		$("#newTrueName").val("");
				         		//更新个人信息展示
				         		if(param.status == "0"){
				         			if(typeof(param.te)!="undefined"){
				         				$("#tel").text(param.tel);
				         				$("#tel_mobile").text(param.tel);
				         			}
									
				         			if(typeof(param.weixin)!="undefined"){
				         				$("#sweixin").text(param.weixin);
				         				$("#sweixin_mobile").text(param.weixin);
				         			}
									
				         			if(typeof(param.qq)!="undefined"){
				         				$("#sqq").text(param.qq);
				         				$("#sqq_mobile").text(param.qq);
				         			}
									
				         			if(typeof(param.uuid)!="undefined"){
				         				$("#uuid").text(param.uuid);
				         				$("#uuid_mobile").text(param.uuid);
				         			}
									
				         			if(typeof(param.name)!="undefined"){
				         				$("#username").text(param.name);
				         				$("#username_mobile").text(param.name);
				         			}
									
				         		}
					   	}
					});
         		}
         		else{
         			alert("两次输入的新密码或者新手机号必须相同!");
         		}
         }
         //退出登录
         function logout(){
       			  $.ajax({	
					   type: "POST",
					   url: "<%=basePath%>controller/manager/logout",
					   data: "",
					   success: function data(data){
					   			window.location = "<%=basePath%>login.jsp";
					   	}
					});
         }
         
         function userinfo(){
         	 $("#mobileUserInfo").toggle();	
         }
         
	</script>

</html>