/**
 * Created by Administrator on 2017-07-28.
 */

function changeMessage(id,formId,tdId,c){
    var index=0;
    $(id).on('click','table td:last-child a',function(e){
        e.preventDefault();
        var that=this;
        var sel=$(this).parents('tr').children().first().text();
        if($(this).hasClass('disable')){
            var m=$(this).parents('tr').attr('mark');
            var m1=$(this).parents('tr').attr('mark1');
            var accountId = $(this).attr('title');
            var sel = this;
            if($(this).html()=="禁用"&&confirm("确定禁用账号ID "+sel+" 吗？")){
                $.ajax({
                    url:sessionStorage['basePath']+'controller/manager/changeAccountStatus',
                    data:{accountId:accountId,accountStatus:'2'},
                    success:function(data){
                    	var returns = eval('(' + data + ')');
                    	var param = returns.result;
                    	if(param>0){
                    		$(sel).html('启用').removeClass('btn-danger').addClass('btn-warning');
                            $(sel).parents('tr').children()[3].innerHTML='禁用';  
                            $(sel).parents('tr').addClass('disable0')
                    	}
                    }
                });
            }else if($(this).html()=="启用"&&confirm("确定启用账号ID "+sel+" 吗？")){
            	$.ajax({
                    url:sessionStorage['basePath']+'controller/manager/changeAccountStatus',
                    data:{accountId:accountId,accountStatus:'0'},
                    success:function(data){
                    	var returns = eval('(' + data + ')');
                    	var param = returns.result;
                    	if(param>0){
                    		$(sel).parents('tr').removeClass('disable0');
                            $(sel).html('禁用').removeClass('btn-warning').addClass('btn-danger');
                            $(sel).parents('tr').children()[3].innerHTML='正常';
                    	}
                    }
                });
            }
            return;
        }
        $(formId).fadeIn();
        var trDbl=this.parentNode.parentNode;
        index=this.parentNode.parentNode.rowIndex;
        console.log(index);
        var n=$(id+' table thead tr').children().length-c;
        //console.log(n);
        for(var i= 0,title='',content='';i<n;i++){
            title+=$(id+' table thead th:eq('+i+')').html()+',';
            content+=$(trDbl).children()[i].innerHTML+',';
        }
        //console.log(title,content);
        var titleArr=title.split(',');
        var contentArr=content.split(',');
        //console.log(titleArr,contentArr);
        for(var j= 0,html='';j<titleArr.length-1;j++){
        	if(j==0){
        		html+=`
                <div class="form-group">
                           <label >${titleArr[j]}</label>
                           <input type="text"  class="form-control" value="${contentArr[j]}" id="userId" disabled>
                           <p>请输入正确的格式</p>
                </div>
                `;
        	}else{
            html+=`
         <div class="form-group">
                    <label >${titleArr[j]}</label>
                    <input type="text"  class="form-control" value="${contentArr[j]}" disabled>
                    <p>请输入正确的格式</p>
         </div>
         `;
        }
        }
        html+=`<div class="form-group">
                    <label >充房卡</label>
                    <input type="number"  class="form-control" id="payCardNum" >
                    <p>请输入正确的房卡数量</p>
         </div>`;
        	 html+=`<div class="form-group">
             <label >重新绑定邀请码</label>
             <input type="number"  class="form-control" id="inviteCode" value="">
             </div>`;
         
        $(tdId).html(html);
        if(sessionStorage['powerId']==1){
        	$('#inviteCode').parents('.form-group').show();
        }else{
        	$('#inviteCode').parents('.form-group').hide();
        }
    });

    $(id+' .sure').click(function(){
    	if(sessionStorage['powerId']!=1){
    		if($("#payCardNum").val()<=0){
    			$("#payCardNum").removeClass('success');
        		$("#payCardNum").addClass('has-err').next().addClass('error');
        		alert("请输入正确的房卡数量！");
        		$("#payCardNum").focus();
        		return;
    		}
    	}
    	if($("#payCardNum").val()%1==0){
    		$("#payCardNum").removeClass('has-err').next().removeClass('error');
    		$("#payCardNum").addClass('success');
    	}else{
    		$("#payCardNum").removeClass('success');
    		$("#payCardNum").addClass('has-err').next().addClass('error');
    		alert("请输入正确的房卡数量！");
    		$("#payCardNum").focus();
    		return;
    	}
    	if(($("#payCardNum").val()==''||$("#payCardNum").val()==0)&&$("#inviteCode").val()==''){
    		alert("请输入正确的信息！");
    		$("#payCardNum").removeClass('success');
    		$("#payCardNum").addClass('has-err').next().addClass('error');
    		$("#payCardNum").focus();
    		return;
        }
    	if($("#payCardNum").val()==''){
    		$("#payCardNum").val(0);
        }
    	var roomCard = parseInt(sessionStorage['roomCard']);
//    	console.log(roomCard);
//    	console.log($("#payCardNum").val());
//    	console.log(($("#payCardNum").val() )> roomCard);
    	if( ($("#payCardNum").val() )> roomCard ){
    		alert("房卡不足，请先充值！(剩余房卡： "+roomCard+" )");
    		$("#payCardNum").focus();
    		return false;
    	}
    	$.ajax({	
    		   type: "POST",
    		   url: sessionStorage['basePath']+"controller/manager/updateAccount",
    		   data: "payCardNum="+$("#payCardNum").val()+"&userid="+$("#userId").val()+"&inviteCode="+$("#inviteCode").val(),
    		   success: function data(data){
    			   //成功返回之后重置userId,managerId
    			  var param  = eval('('+data+')');
    			  alert(param.msg);
    			  if(param.roomcard){
    				  sessionStorage['roomCard']=param.roomcard;
    			  }
    			  var n= parseInt($("#vipTbl tr:eq("+index+")").children()[2].innerHTML);
    			  var m= parseInt($("#payCardNum").val());
    			  console.log(index,n,m,n+m);
    			  $("#vipTbl tr:eq("+index+")").children()[2].innerHTML=n+m;
    			  if(param.bangding==1){
    				  $("#vipTbl tr:eq("+index+")").children()[5].innerHTML=$("#inviteCode").val();
    			  }
    		 }
    	});
        $(formId).hide();
//        var n=$(id+' table thead tr').children().length-c;
//        for(var i= 0,content2='';i<n-1;i++){
//            content2+=$(formId+' form div.form-group').children('input')[i].value+',';
//        }
//
//        var contentArr2=content2.split(',');
//        for(var i=0,html='';i<contentArr2.length-1;i++) {
//            var nowTr = $(id + ' table tbody tr:eq(' + (index - 1) + ')');
//            var td = nowTr.children()[i];
//            if (td.className == 'uname') {
//                $(td).find('b').html(contentArr2[i]);
//            } else {
//                $(td).html(contentArr2[i]);
//            }
//        }
       
    });
    $(formId+' .cancel').on('click',function(){
        $(formId).fadeOut();
    });
}

changeMessage('#vip','#vipDetail','#vip-message',1);