<%@ page language="java" import="java.util.*,com.weipai.model.Manager" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	int powerId = ((Manager)session.getAttribute("manager")).getPowerId();
	int rootManager = ((Manager)session.getAttribute("manager")).getRootManager();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="zh-cn">
<head>
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">
    <meta http-equiv="x-ua-compatible" content="IE=edge">
    <meta charset="UTF-8">
    <title>我的代理</title>
    <link href="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.3.1/css/bootstrap-combined.min.css" rel="stylesheet">
    <link rel="stylesheet" href="../css/jquery.treegrid.css">
    <link rel="stylesheet" href="../css/gm.css">
    <!--[if lt IE 9]>
    <script src="../js/html5shiv.min.js"></script>
    <script src="../js/respond.min.js"></script>
    <![endif]-->
    <script>
       sessionStorage['powerId']=<%=powerId%>;
    </script>
</head>
<body>
<div class="container-fluid">

  <nav class="navbar navbar-default navbar-fixed-top">
      <div class="container-fluid">
      <div class="navbar-header">
          <button id="navBtn" type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">
              代理管理
          </a>
      </div>
      <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
          <ul class="nav navbar-nav" id="navlist">
              <li><a href="#info">代理信息</a></li>
              <%if(powerId==1){ %>
               <li  class="active"><a href="#agentManage">代理管理</a></li>
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
    <div id="agentManage" class="active">
        <h2>我的代理</h2>
        
        <div class="panel panel-default">
            <div class="panel-body">
            	<form class="form-inline form-btn">
            		<input type="text" autofocus class="search" id="inputName" placeholder="请输入代理真实姓名"/>
                    <input type="number" class="search" id="inputInviteCode" placeholder="请输入代理邀请码查询"/>
                    <input type="number" class="search" id="inputUuid" placeholder="请输入用户ID查询"/>
                    <input type="button" class="btn btn-default" onclick="" value="查询" id="search"/>
                </form>
                <div class="table-responsive">
                    <table class="table table-bordered table-striped table-hover mainTbl tree">
                        <thead>
                        <tr>
                            <th>姓名</th>
                            <th>代理编码</th>
                            <th>手机号</th>
                            <th>微信</th>
                            <th>QQ号码</th>
                            <th>邀请码</th>
                            <th>游戏用户ID</th>
                            <th>分成比例</th>
                            <th>代理状态</th>
                            <th>是否开代理</th>
                            <th>代理级别</th>
                            <th>编辑</th>
                        </tr>
                        </thead>
                        <tbody id="tbody">
                        
                        </tbody>
                    </table>
                </div>
            </div>


        </div>
        <div id="agentDetail">
            <form>
                <button type="button" class="close"><span>&times;</span></button>
                <h4>代理信息</h4>
                <div id="agent-message">
                	<div class="form-group">
                           <label>姓名</label>
                           <input id="name" type="text" class="form-control" value="">
                           <p>请输入2-4位中文姓名</p>
                	</div>
                	<div class="form-group">
                           <input id="mid" style="display:none" value="">
                	</div>
         			<div class="form-group">
                    	<label>手机号</label>
                    	<input id="telephone" type="text" class="form-control" value="">
                    	<p>手机号码格式不正确，请重新输入(11位)</p>
        			</div>
         			<div class="form-group">
                   	 	<label>微信</label>
                    	<input id="weixin" type="text" class="form-control" value="">
         			</div>
         			<div class="form-group">
                    	<label>QQ号码</label>
                    	<input id="QQ" type="text" class="form-control" value="">
         			</div>
         			<div class="form-group">
                    	<label>邀请码</label>
                    	<input id="inviteCode" type="text" class="form-control" onchange="validCode()" value="">
                    	<p id="validInfo"></p>
    		        	<input type="hidden" id="checkCanSubmit" value="true">
         			</div>
         			<div class="form-group">
                    	<label>游戏用户ID</label>
                    	<input id="uuid" type="number" onchange="validUuid()" class="form-control" value="">
         			</div>
         			<div class="form-group">
                    	<label>分成比例</label>
                    	<input id="rebate" type="text" class="form-control" value="">
                    	<p>请输入正确的格式，如(0.8:0.2)</p>
         			</div>
         			<div class="form-group">
                    	<h5>代理状态</h5>
                    	<select id="status">
	                    	<option value="0">正常</option>
	                    	<option value="2">禁用</option>
                    	</select>
         			</div>
                	<div class="form-group">
                           <label>重新绑定上级代理邀请码</label>
                           <input id="parentInviteCode" type="text" class="form-control" value="" placeholder="请输入上级代理邀请码">
                           <p>请输入正确的格式</p>
               		</div>
               		<div class="form-group" id="rootManager">
        		       <label>是否能开代理</label> <br>
        		       <label> <input type="radio" name="rootManager" value="1" title="1">是</label>&nbsp;&nbsp;
        	           <label> <input type="radio" name="rootManager" value="0" title="0">否</label>
        			</div>
         			<div class="form-group">
                    	<h5>代理级别</h5>
                    	<select id="powerId" truevalue="总代理"><option value="5">总代理</option><option value="4">一级代理</option><option value="3">二级代理</option><option value="2">三级代理</option></select>
         			</div>
                </div>
                <input type="button"  class="btn  btn-success sure"  value="修 改"/>
                <input type="button" class="btn  btn-danger cancel" value="取 消"/>
            </form>
        </div>
         <div id="charge">
                <form>
                    <button type="button" class="close"><span>&times;</span></button>
                    <h4>代理充值信息</h4>
                    <div class="chargeDetail">
                        <div class="form-group">
                            <label>代理姓名：</label>
                            <input type="text"  disabled class="uname form-control">
                        </div>
                        <div class="form-group">
                            <label>代理编号：</label>
                            <input type="text" name="num" disabled class="form-control">
                            <input type="hidden" name="num"  class="form-control">
                        </div>
                        <div class="form-group">
                            <label>充房卡：</label>
                            <input type="number" name="payCardNum"  class="form-control">
                            <p>请输入正确的房卡数量</p>
                        </div>
                    </div>
                    <input type="button" class="btn  btn-success sure" value="确 定"/>
                    <input type="button" class="btn  btn-danger cancel" value="取 消"/>
                </form>
            </div>
    </div>


   </div>
</div>
<script src="../js/jquery-1.11.3.js"></script>
<script src="../js/bootstrap.js"></script>
<script src="../js/bootstrap-paginator.js"></script>
<script src="../js/bootlint.js"></script>
<script type="text/javascript" src="../js/jquery.treegrid.min.js"></script>
<script src="../js/gm.js"></script>
<script type="text/javascript">
//var canSubmit = true;

var managerUpId=0;
$('#search').click(function(){
	if($('#inputInviteCode').val()==''&& $('#inputUuid').val()=='' && $('#inputName').val()==''){
		alert('请输入代理邀请码或用户ID查询！');
		return;
	}
	var uuid=$('#inputUuid').val();
	var inviteCode=$('#inputInviteCode').val();
	var name=$('#inputName').val();
	console.log(uuid,inviteCode);
	$.ajax({
		   url: "<%=basePath%>controller/manager/selectManagerByInviteCode",
		   data: {uuid:uuid,inviteCode:inviteCode,name:name},
		   success: function data(data){
			       $('#tbody').html("");
		 			var param = JSON.parse(data);
					console.log(param);
					console.log(param.status);
					if(param.status==1){
						var obj = param.manager;
						managerUpId = obj.managerUpId;
						var powerIdStr='';
						var rootManagerStr='';
						var statusStr='';
						if(obj.powerId==5){
							powerIdStr="总代理";
						}else if(obj.powerId==4){
							powerIdStr="一级代理";
						}else if(obj.powerId==3){
							powerIdStr="二级代理";
						}else if(obj.powerId==2){
							powerIdStr="三级代理";
						}
						if(obj.status==0){
							statusStr="正常";
						}else{
							statusStr="禁用";
						}
						if(obj.rootManager==1){
							rootManagerStr="是";
						}else{
							rootManagerStr="否";
						}
						var rebateStr = "";
						if(obj.rebate){
							rebateStr=obj.rebate;
						}else{
							rebateStr="";
						}
						var html="<tr>"+"<td>"+obj.name+"</td>"+
						"<td>"+obj.id+"</td>"+
						"<td>"+obj.telephone+"</td>"+
						"<td>"+obj.weixin+"</td>"+
						"<td>"+obj.qq+"</td>"+
						"<td>"+obj.inviteCode+"</td>"+
						"<td>"+obj.password+"</td>"+
						"<td>"+rebateStr+"</td>"+
						"<td>"+statusStr+"</td>"+
						"<td>"+rootManagerStr+"</td>"+
						"<td>"+powerIdStr+"</td>"+
						"<td><a href='"+obj.id+"' class='btn btn-sm btn-warning'>修改</a> <a href='"+obj.id+"' class='btn btn-sm btn-success charge'>充房卡</a> <a href='"+obj.id+"' class='btn btn-sm btn-primary resetPwd'>密码重置</a></td>"+
						"</tr>";
						console.log(html);
						$('#tbody').html(html);
					}
					
		   }
	})
})

function changeMessage(id,formId,tdId,c){
    var index=0;
    var str;
    var beforeCode;
    $(id).on('click','table td:last-child a',function(e){
        e.preventDefault();
        var that=this;

        var sel=$(this).parents('tr').children().first().text();
        var num=$(this).parents('tr').children()[1].innerHTML;
        if($(this).hasClass('delete')){
            if(confirm("确定删除"+sel+" 项吗？")){
                $(that).parents('tr').remove();
                deleteAgent(this);
            }
            return;
        }else if($(this).hasClass('resetPwd')){
        	var mid = $(this).parents('tr').children()[1].innerHTML;
        	console.log(mid);
            var str1=prompt('请输入新密码：');
            if(str1==null){
                return;
            }
            var str2=prompt('请再次输入新密码：');
            if(str1!=str2){
                alert('两次输入密码不一致！请重新输入！');
                return;
            }else if((str1!=null)&&str1==str2&&confirm("确定修改密码？")){
            		//var mid = $(this).parents('tr').attr('mark');
            		$.ajax({	
            			   type: "POST",
            			   url: sessionStorage['basePath']+"controller/manager/updateManagerPasswordAgentManager?newPwd="+str1+"&mid="+mid,
            			   data: "",
            			   success: function data(data){
            				var returns =  eval('(' + data + ')');
            				alert(returns.msg);
            			   }
            		});
            }
            return;

        }
        if($(this).hasClass('charge')){
            $("#charge").fadeIn();
        	$("#charge form .uname").val(sel);
        	$("#charge form [name='num']").val(num);
        	return;
        }
        $(formId).fadeIn();
        var name=$(this).parents('tr').find('td')[0].innerHTML;
        var mid=$(this).parents('tr').find('td')[1].innerHTML;
        var telephone=$(this).parents('tr').find('td')[2].innerHTML;
        var weixin=$(this).parents('tr').find('td')[3].innerHTML;
        var QQ=$(this).parents('tr').find('td')[4].innerHTML;
        var inviteCode=$(this).parents('tr').find('td')[5].innerHTML;
        var uuid=$(this).parents('tr').find('td')[6].innerHTML;
        var rebate=$(this).parents('tr').find('td')[7].innerHTML;
        var status=$(this).parents('tr').find('td')[8].innerHTML;
        var rootManager=$(this).parents('tr').find('td')[9].innerHTML;
        var powerId = $(this).parents('tr').find('td')[10].innerHTML;
        console.log(name);
        $('#name').val(name);
        $('#mid').val(mid);
        $('#telephone').val(telephone);
        $('#weixin').val(weixin);
        $('#QQ').val(QQ);
        $('#inviteCode').val(inviteCode);
        $('#uuid').val(uuid);
        $('#rebate').val(rebate);
        if(status=="正常"){
        	$('#status').val('0');
        }else{
        	$('#status').val('2');
        }
        if(rootManager=='是'){
        	$("#rootManager input").first().prop('checked',true);
        }else{
        	$("#rootManager input").last().prop('checked',true);
        }
        console.log(powerId);
        getParentInviteById(managerUpId,powerId);
    });
    
    $(formId+' .sure').click(function(){
    	saveManagerInfo();
    });
    
    $(formId+' .cancel').on('click',function(){
        $(formId).fadeOut();
    });
}
//changeMessage('#vip','#vipDetail','#vip-message',1);
changeMessage('#agentManage','#agentDetail','#agent-message',1);

$("#charge .cancel").click(function(){
	$("#charge").hide();
})
$("#charge .sure").click(function(){
	var isAgent = 0;
	if(sessionStorage['powerId']!=1){
		isAgent = 1;
		var roomCard = parseInt(sessionStorage['roomCard']);
		if( parseInt($("#charge form [name='payCardNum']").val()) > roomCard ){
			alert("房卡不足，请先充值！(剩余房卡： "+roomCard+" )");
			$("#charge form [name='payCardNum']").focus();
			return;
		}
		if($("#charge form [name='payCardNum']").val()<=0){
			$("#charge form [name='payCardNum']").removeClass('success');
			$("#charge form [name='payCardNum']").addClass('has-err').next().addClass('error');
			alert("请输入正确的房卡数量！");
			$("#charge form [name='payCardNum']").focus();
			return;
		}
	}
//	if($("#charge form [name='payCardNum']").val()==''){
//		$("#charge form [name='payCardNum']").val('0');
//	}
	console.log($("#charge form [name='payCardNum']").val()=='');
	if($("#charge form [name='payCardNum']").val()==''||$("#charge form [name='payCardNum']").val()%1!=0){
		$("#charge form [name='payCardNum']").removeClass('success');
		$("#charge form [name='payCardNum']").addClass('has-err').next().addClass('error');
		alert("请输入正确的房卡数量！");
		$("#charge form [name='payCardNum']").focus();
		return;
	}else{
		$("#charge form [name='payCardNum']").removeClass('has-err').next().removeClass('error');
		$("#charge form [name='payCardNum']").addClass('success');
	}
	
	var str = $("#charge form").serialize();
	console.log(str);
	$("#charge").hide();
	$("#charge form [name='payCardNum']").val("");
	$.ajax({	
		   type: "POST",
		   url: sessionStorage['basePath']+"controller/manager/updateAgentAccount",
		   data: str+"&isAgent="+isAgent,
		   success: function data(data){
			   //成功返回之后重置userId,managerId
			  var param  = eval('('+data+')');
			  if(param.roomcard){
				  sessionStorage['roomCard']=param.roomcard;
			  };
			  alert(param.msg);
		 }
	});
})



function deleteAgent(obj){
	var delId=$(obj).parents('tr').children()[1].innerHTML;
    //console.log(delId);
    $.ajax({
	   url: "<%=basePath%>controller/manager/deleteProxyAccount",
	   data: "id="+delId,
	   success: function data(data){
	 			var param = eval('('+data+')');
				console.log(param);
	   	}
    })
}

function validCode(){
	var inviteCode = $("#inviteCode").val();
	$.ajax({	
		   type: "POST",
		   url: "<%=basePath%>controller/manager/inviteCodeValidAgentManager?inviteCode="+inviteCode,
		   data: "",
		   success: function data(data){
			   var returns =  eval('(' + data + ')');
				var total = returns.valid;
				if(total){
				    $("#checkCanSubmit").val(true);
				}
				else{
					$("#checkCanSubmit").val(false);
					alert('该邀请码不可用');
				}
			}
		});
	return true;
}
function validUuid(){
	var uuid = $("#uuid").val();
	$.ajax({	
		   type: "POST",
		   url: "<%=basePath%>controller/manager/validUuid?uuid="+uuid,
		   data: "",
		   success: function data(data){
			   var returns =  eval('(' + data + ')');
				var total = returns.valid;
				//console.log(total);
				if(total){
					//return true;
					//$("#validInfo").html("该邀请码可用");
				    $("#checkCanSubmit").val(true);
				    $("#uuid").addClass('success').removeClass('has-err');
				    return true;
				}
				else{
					//$("#validInfo").html("该邀请码不可用");
					$("#checkCanSubmit").val(false);
					$("#uuid").addClass('has-err').removeClass('success');
					alert('此用户ID不存在,请重新输入');
					return false;
					//return false;
				}
			}
	});
	return true;
}
function getParentInvite(){
	var uuid = $("#uuid").val();
	$.ajax({	
		   type: "POST",
		   url: "<%=basePath%>controller/manager/getParentInvite?uuid="+uuid,
		   data: "",
		   success: function data(data){
			    var returns =  eval('(' + data + ')');
			    //console.log(returns);
				var total = returns.inviteCode;
				var powerId = returns.powerId;
				$("#parentInviteCode").val(total);
				if(powerId==5){
					var html=`<option value="0">--请选择--</option><option value="4">一级代理</option><option value="3">二级代理</option><option value="2">三级代理</option>`;
					$("#powerId").html(html);
				}else if(powerId==4){
					var html=`<option value="0">--请选择--</option><option value="3">二级代理</option><option value="2">三级代理</option>`;
					$("#powerId").html(html);
				}else if(powerId==3){
					var html=`<option value="0">--请选择--</option><option value="2">三级代理</option>`;
					$("#powerId").html(html);
				}else  if(powerId==1){
					var html=`<option value="0">--请选择--</option><option value="5">总代理</option><option value="4">一级代理</option><option value="3">二级代理</option><option value="2">三级代理</option>`;
					$("#powerId").html(html);
					
				}}
				
		});
}

function getParentInviteById(id,power){
	$.ajax({	
		   type: "POST",
		   url: "<%=basePath%>controller/manager/getParentInviteById?id="+id,
		   data: "",
		   success: function data(data){
			    var returns =  eval('(' + data + ')');
			    //console.log(returns);
				var total = returns.inviteCode;
				var powerId = returns.powerId;
				$("#parentInviteCode").val(total);
				if(powerId==5){
					var html=`<option value="0">--请选择--</option><option value="4">一级代理</option><option value="3">二级代理</option><option value="2">三级代理</option>`;
					$("#powerId").html(html);
				}else if(powerId==4){
					var html=`<option value="0">--请选择--</option><option value="3">二级代理</option><option value="2">三级代理</option>`;
					$("#powerId").html(html);
				}else if(powerId==3){
					var html=`<option value="0">--请选择--</option><option value="2">三级代理</option>`;
					$("#powerId").html(html);
				}else  if(powerId==1){
					var html=`<option value="0">--请选择--</option><option value="5">总代理</option><option value="4">一级代理</option><option value="3">二级代理</option><option value="2">三级代理</option>`;
					$("#powerId").html(html);
					
				}
		   
				if(power=="总代理"){
			    	$('#powerId').val('5');
			    }else if(power=="一级代理"){
			    	$('#powerId').val('4');
			    }else if(power=="二级代理"){
			    	$('#powerId').val('3');
			    }else if(power=="三级代理"){
			    	$('#powerId').val('2');
			    }
		   }			
		});
}

function saveManagerInfo(){
		var mid = $("#mid").val();
		var weixin = $("#weixin").val();
		var qq = $("#QQ").val();
		var name = $("#name").val();
		var telephone = $("#telephone").val();
		var uuid = $("#uuid").val();
		var rebate = $("#rebate").val();
		var powerId = $("#powerId").val();
		var status = $('#status').val();
		var inviteCode = $("#inviteCode").val();
		var parentInviteCode = $("#parentInviteCode").val();
		if(typeof(mid)=="undefined")
			mid="";
		var rootManager = $("input[name='rootManager']:checked").val();
		var isCheckOk=true;
		function checkRequired(id){
			if($(id).val().length==0){
				$(id).focus();
				$(id).removeClass('success');
	            $(id).addClass('has-err').next().addClass('error');
				isCheckOk=false;
				return false;
			}else{
				$(id).removeClass('has-err').next().removeClass('error');
	            $(id).addClass('success');
				return true;
			}
		}
		checkRequired("#name");
		if(!checkRequired("#name")){
			return;
		}
// 		var nreg=/^([\u4e00-\u9fa5]){2,4}$/;
// 		if(!nreg.test($("#name").val())){
// 				$("#name").focus();
// 	        	$("#name").removeClass("success");
// 	        	$("#name").addClass('has-err').next().addClass('error');
// 	        	isCheckOk=false;
// 	            return;
// 		}
		
		var reg=/^1[34578]\d{9}$/;
        if(!reg.test($("#telephone").val())){
        	$("#telephone").focus();
        	$("#telephone").removeClass("success");
        	$("#telephone").addClass('has-err').next().addClass('error');
        	isCheckOk=false;
            return;
        }
        checkRequired("#weixin");
        if(!checkRequired("#weixin")){
			return;
		};
        checkRequired("#inviteCode");
        if(!checkRequired("#inviteCode")){
			return;
		};
        checkRequired("#uuid");
        if(!checkRequired("#uuid")){
			return;
		};
		var rebetreg=/^0\.\d{1,2}:0\.\d{1,2}$/;
		if($("#rebate").val()!=""&& (!rebetreg.test($("#rebate").val()))){
				$("#rebate").focus();
	        	$("#rebate").removeClass("success");
	        	$("#rebate").addClass('has-err').next().addClass('error');
	        	isCheckOk=false;
	            return;
		}else{
			$("#rebate").removeClass("has-err").next().removeClass('error');
        	$("#rebate").addClass('success');
		}
		
		if($("#powerId").val()=="0"){
			isCheckOk=false;
			alert("请选择代理级别！");
			return;
		}
        //checkRequired("#parentInviteCode");
        //if(!checkRequired("#parentInviteCode")){
		//	return;
		//};
		
		var checkCanSubmit =  isCheckOk;
		//console.log($("#agent .checkCanSubmit").val());
		//var canSubmit = validCode();
		if(checkCanSubmit){
			$('#agentDetail').fadeOut();
			console.log("mid="+mid+"&weixin="+weixin+"&qq="+qq+"&name="+name+"&telephone="+telephone+"&uuid="+uuid+"&powerId="+powerId+"&inviteCode="+inviteCode+"&parentInviteCode="+parentInviteCode+"&rootManager="+rootManager+"&rebate="+rebate+"&status="+status);
			
			$.ajax({	
				   type: "POST",
				   url: "<%=basePath%>controller/manager/updateManagerInfo",
				   data: "mid="+mid+"&weixin="+weixin+"&qq="+qq+"&name="+name+"&telephone="+telephone+"&uuid="+uuid+"&powerId="+powerId+"&inviteCode="+inviteCode+"&parentInviteCode="+parentInviteCode+"&rootManager="+rootManager+"&rebate="+rebate+"&status="+status,
				   success: function data(data){
				 			var param = eval('('+data+')');
							if(param.status == "0"){
								  alert("资料提交成功");
								  var powerIdStr='';
	          						var rootManagerStr='';
	          						var statusStr='';
	          						if(powerId==5){
	          							powerIdStr="皇冠代理";
	          						}else if(powerId==4){
	          							powerIdStr="钻石代理";
	          						}else if(powerId==3){
	          							powerIdStr="铂金代理";
	          						}else if(powerId==2){
	          							powerIdStr="黄金代理";
	          						}
	          						if(status==0){
	          							statusStr="正常";
	          						}else{
	          							statusStr="禁用";
	          						}
	          						if(rootManager==1){
	          							rootManagerStr="是";
	          						}else{
	          							rootManagerStr="否";
	          						}
	          						var html="<tr>"+"<td>"+name+"</td>"+
	          						"<td>"+mid+"</td>"+
	          						"<td>"+telephone+"</td>"+
	          						"<td>"+weixin+"</td>"+
	          						"<td>"+qq+"</td>"+
	          						"<td>"+inviteCode+"</td>"+
	          						"<td>"+uuid+"</td>"+
	          						"<td>"+rebate+"</td>"+
	          						"<td>"+statusStr+"</td>"+
	          						"<td>"+rootManagerStr+"</td>"+
	          						"<td>"+powerIdStr+"</td>"+
	          						"<td><a href='#' class='btn btn-sm btn-warning'>修改</a> <a href='#' class='btn btn-sm btn-success charge'>充房卡</a> <a href='#' class='btn btn-sm btn-primary resetPwd'>密码重置</a></td>"+
	          						"</tr>";
	          						console.log(html);
	          						$('#tbody').html(html);
							}else{
							 	alert(param.error);
							}
		         		//更新个人信息展示	
							$("#agentDetail").hide();
							$("#add-message").hide();
				   	}
				});
		
		}	else{
			alert("邀请码不可用！");
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


//
</script>
</body>
</html>