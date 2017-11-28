<%@ page language="java" import="java.util.*,com.weipai.model.Manager" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	int powerId = ((Manager)session.getAttribute("manager")).getPowerId();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-cn">
<head>
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">
    <meta http-equiv="x-ua-compatible" content="IE=edge">
    <meta charset="UTF-8">
    <title>代理信息</title>
    <link href="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.3.1/css/bootstrap-combined.min.css" rel="stylesheet">
    <link rel="stylesheet" href="../css/gm.css">

    <!--[if lt IE 9]>
    <script src="js/html5shiv.min.js"></script>
    <script src="js/respond.min.js"></script>
    <![endif]-->
</head>
<body onload="checkIsAdmin()">
<div class="container-fluid">
   <nav class="navbar navbar-default navbar-fixed-top">
      <div class="container-fluid">
      <div class="navbar-header">
          <button id="navBtn" type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">代理管理</a>
      </div>
      <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
          <ul class="nav navbar-nav" id="navlist">
              <li class="active"><a href="#info">代理信息</a></li>
              <%if(powerId==1){ %>
		         <li><a href="#agentManage">代理管理</a></li>
		      <%} %>
              <li><a href="#agent">我的代理</a></li>
              <li><a href="#vip">我的会员</a></li>
              <li><a href="#detail">账单明细</a></li>
              <li><a href="#count">统计信息</a></li>
              <li><a href="#note">提现流水</a></li>
              <%if(powerId==1){ %>
              <li><a href="#notice">公告管理</a></li>
              <li><a href="#config">系统配置</a></li>
              <%} %>
          </ul>
      </div>
      <button type="button" class="btn btn-danger logout" onclick="logout()">退出登录</button>
      </div>
  </nav>
    <div class="content">
    <div id="info" class="active">
        <h2 id="adminTitle">代理信息</h2>
        <div class="panel panel-default">
            <div class="panel-body">
                <div class="well">
                    代理须知：各位提款时候如果发生报错信息，请先检查可提现金额是否超过100。如是其他错误，请稍后再次尝试或联系上级代理。
                </div>

                <div class="general-info">
                    <div class="info-head">
                        我的基本信息
                    </div>
                    <div class="info-body table-responsive">
                        <table class="table table-striped table-bordered table-hover">
                            <tbody>
                            <tr>
                                <td>游戏账号ID：</td>
                                <td>${sessionScope.userAccount.uuid}</td>
                            </tr>
                            <tr>
                                <td>游戏账号昵称：</td>
                                <td>${sessionScope.userAccount.nickname}</td>
                            </tr>
                            <tr>
                                <td>真实姓名：</td>
                                <td>${sessionScope.manager.name}</td>
                            </tr>
                            <tr>
                                <td>手机号：</td>
                                <td>${sessionScope.manager.telephone}</td>
                            </tr>
                            <tr>
                                <td>微信：</td>
                                <td>${sessionScope.manager.weixin}</td>
                            </tr>
                            <tr>
                                <td>房卡：</td>
                                <td>${sessionScope.userAccount.roomcard}</td>
                            </tr>
                            <tr>
                                <td>邀请码：</td>
                                <td>${sessionScope.manager.inviteCode}</td>
                            </tr>
                            <tr>
                                <td>代理级别：</td>
                                <td>${sessionScope.manager.powerId==5?'总代理':(sessionScope.manager.powerId==4?'一级代理':(sessionScope.manager.powerId==3?'二级代理':(sessionScope.manager.powerId==2?'三级代理':(sessionScope.manager.powerId==1?'系统管理员':''))))}</td>
                            </tr>
                            <tr>
                                <td>上级代理邀请码：</td>
                                <td>${sessionScope.managerUp.inviteCode}</td>
                            </tr>
                            <tr>
                             	<td>代理状态：</td>
                                <td>正常</td>
                            </tr>
                            <tr>
                                <td>操作：</td>
                                <td><a href="#" onclick="changepw()">修改密码</a></td>
                                
                                
                            </tr>
                            <!-- 
                            <tr>
                                <td>最近登录时间：</td>
                                <td>2017-07-25 10:28:47</td>
                                <td>最近登录IP：</td>
                                <td>121.35.3.56</td>
                                <td>上级代理昵称：</td>
                                <td>1</td>
                            </tr>
                            <tr>
                                <td>地址：</td>
                                <td colspan="5">浙江台州</td>
                            </tr>
                             -->
                            </tbody>
                        </table>
                    </div>
                </div>
                <div id="generalInfo" class="general-info">
                    <div class="info-head">
                        金额信息
                    </div>
                    <div class="info-body money">
                        <table class="table table-striped table-bordered table-hover">
                            <tbody>
                            <tr>
                                <td>可用金额：</td>
                                <td>${sessionScope.total}</td>
                            </tr>
                            <tr>
                                <td>累计收益：</td>
                                <td>未统计</td>
                            </tr>
                            <tr>
                                <td>下属用户收益：</td>
                                <td>${sessionScope.mineone}</td>
                            </tr>
                            <tr>
                                <td>下属代理收益：</td>
                                <td>${sessionScope.minetwo}</td>
                            </tr>
                            
                            <tr>
                                <td>转账金额：</td>
                                <td>
                                    <div class="input-group">
                                        <input id="tixianmoney" type="number" class="form-control" value="${sessionScope.total}" disabled="disabled"/>
                                  <span class="input-group-btn">
                                  <input type="button" class="btn btn-primary"  onclick="tixian(this)" value="提现"/>
                                  </span>
                                    </div>
                                </td>
                            </tr>

                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
		<div id="add-message">
            <form name="addAgentForm">
                <button type="button" class="close"><span>&times;</span></button>
                <h4>修改代理密码</h4>
                <div id="addAgentMessage">
                <div class="form-group">
		        <label>旧密码</label>
		        <input id="oldPwd" type="password" class="form-control" value="">
				</div>
				<div class="form-group">
		        <label>新密码</label>
		        <input id="newOnePwd" type="password" class="form-control" value="">
		        </div>
				<div class="form-group">
		        <label>再次输入新密码</label>
		        <input id="newTwoPwd" type="password" class="form-control" value="">
				</div>
                </div>
                <input type="button"  class="btn  btn-success sureAdd" onclick="saveManagerInfo()" value="修改"/>
                <input type="button" class="btn  btn-danger cancel" value="取 消"/>

            </form>
        </div>
    </div>
   </div>
</div>

<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
<script src="../js/jquery-1.11.3.js"></script>
<script src="../js/bootstrap.js"></script>
<script src="../js/bootstrap-paginator.js"></script>
<script src="../js/bootlint.js"></script>
<script src="../js/gm.js"></script>
<script>
$("#pw-message").on('click','.cancel',function(){
	$("#pw-message").hide();
})
function saveManagerInfo(){
	var oldPwd = $("#oldPwd").val();
		var newOnePwd = $("#newOnePwd").val();
		var newTwoPwd = $("#newTwoPwd").val();
		var twoTel = $("#twoTel").val();
		if(newOnePwd ==newTwoPwd){
			$.ajax({	
		   type: "POST",
		   url: "<%=basePath%>controller/manager/updateManagerPassword",
		   data: "oldPwd="+oldPwd+"&newPwd="+newOnePwd,
		   success: function data(data){
		 			var param = eval('('+data+')');
					if(param.status == "0"){
						  alert("资料修改成功");
						  
					}
					else{
					 	alert(param.error);
					}
		   	}
		});
		}
		else{
			alert("两次输入的新密码或者新手机号必须相同!");
		}
}

function changepw(){
	$('#add-message').fadeIn();
}

function checkIsAdmin(){
	$('#add-message').fadeOut();
	var isAdmin = ${sessionScope.manager.powerId==1};
	if(isAdmin){
	$("#generalInfo").html("");
	$("#adminTitle").html("系统管理员信息");
	}
}

function logout(){
	  $.ajax({	
	   type: "POST",
	   url: "<%=basePath%>controller/manager/logout",
	   data: "",
	   success: function data(data){
	        sessionStorage.clear();
	   			window.location = "<%=basePath%>login.jsp";
	   	}
	});
}

function tixian(o){
	if($("#tixianmoney").val()>0){
		$.ajax({	
			   type: "POST",
			   url: "<%=basePath%>controller/manager/tixian",
			   data: "",
			   success: function data(data){
				   var returns =  eval('(' + data + ')');
				   if(typeof(returns.total)!="undefined"){ 
					   $("#tixianmoney").val('0');
					   alert(returns.info);
					   }else{
						   alert(returns.error);
					   } 
				}
			});
		$(o).prop('disabled',true);
		var sel=o;
		var timer=setTimeout(function(){
			$(sel).prop('disabled',false);
		},3000)
	}
}
</script>
</body>
</html>