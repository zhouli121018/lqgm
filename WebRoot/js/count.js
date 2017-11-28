/**
 * Created by Administrator on 2017-07-28.
 */

	
	
	
	function getPayCount(pageIndex,type,path,startTime,endTime){
		$.ajax({	
			   type: "POST",
			   url: path+"controller/manager/getPayCount?pageNum="+pageIndex+"&startTime="+startTime+"&endTime="+endTime,
			   data: "",
			   success: function data(data){
				   var returndata =  eval('(' + data + ')');
				   var arr = returndata.managers;
				   var n=0;
				   var totalPages = returndata.totalNum/10+1;
				   for(var i=0,html='';i<arr.length;i++){
				        html+=`<tr>
				                    <td>${arr[i].name}</td>
				                    <td>${arr[i].telephone}</td>
							         <td>${arr[i].weixin}</td>
							         <td>${arr[i].inviteCode}</td>
							         <td>${arr[i].password}</td>
							         <td>${arr[i].totalcards}</td>
							         <td>${arr[i].actualcard}</td>
				               </tr>`;
				    }

				    $('#countTbl tbody').html(html);
				    $("#countNumbers1").html(returndata.totalNum);
			        if(totalPages>1){
			        	var options = {
			                    currentPage: pageIndex,
			                    totalPages: totalPages,
			                    bootstrapMajorVersion: 3,
			                    onPageChanged: function(e,oldPage,newPage){
			                    	getPayCount(newPage,type,path,startTime,endTime);
			                    }
			                }

			                $('#count-pages1').bootstrapPaginator(options);
			        }else{
			        	 $('#count-pages1').html("");
			        }
			        $("td").each(function(){
			        	if($(this).html()=='undefined'){
			        		$(this).html("----");
			        	}
			          });
				}
			});
	   
	}
	
	function getUserCount(pageIndex,type,path,startTime,endTime){
		$.ajax({	
			   type: "POST",
			   url: path+"controller/manager/getUserCount?pageNum="+pageIndex+"&startTime="+startTime+"&endTime="+endTime,
			   data: "",
			   success: function data(data){
				   var returndata =  eval('(' + data + ')');
				   var arr = returndata.managers;
				   var n=0;
				   var totalPages = returndata.totalNum/10+1;
				   for(var i=0,html='';i<arr.length;i++){
				        html+=`<tr>
				                    <td>${arr[i].name}</td>
				                    <td>${arr[i].telephone}</td>
							         <td>${arr[i].weixin}</td>
							         <td>${arr[i].inviteCode}</td>
							         <td>${arr[i].password}</td>
							         <td>${arr[i].totalcards}</td>
				               </tr>`;
				    }

				    $('#userCountTbl tbody').html(html);
			        $("#countNumbers2").html(returndata.totalNum);
			        if(totalPages>1){
			        	var options = {
			                    currentPage: pageIndex,
			                    totalPages: totalPages,
			                    bootstrapMajorVersion: 3,
			                    onPageChanged: function(e,oldPage,newPage){
			                    	getUserCount(newPage,type,path,startTime,endTime);
			                    }
			                }

			                $('#count-pages2').bootstrapPaginator(options);
			        }else{
			        	 $('#count-pages2').html("");
			        }
			        
			        $("td").each(function(){
			        	if($(this).html()=='undefined'){
			        		$(this).html("----");
			        	}
			          });
				}
			});
	   
	}
	
	function getUserPayCount(pageIndex,type,path,startTime,endTime){
		$.ajax({	
			   type: "POST",
			   url: path+"controller/manager/getUserPayCount?pageNum="+pageIndex+"&startTime="+startTime+"&endTime="+endTime,
			   data: "",
			   success: function data(data){
				   var returndata =  eval('(' + data + ')');
				   var arr = returndata.managers;
				   var n=0;
				   var totalPages = returndata.totalNum/10+1;
				   for(var i=0,html='';i<arr.length;i++){
				        html+=`<tr>
				                    <td>${arr[i].uuid}</td>
				                    <td>${arr[i].managerid}</td>
							         <td>${arr[i].money}</td>
							         <td>${arr[i].paycount}</td>
				               </tr>`;
				    }

				    $('#sumTbl tbody').html(html);
			        $("#countNumbers3").html(returndata.totalNum);
			        if(totalPages>1){
			        	var options = {
			                    currentPage: pageIndex,
			                    totalPages: totalPages,
			                    bootstrapMajorVersion: 3,
			                    onPageChanged: function(e,oldPage,newPage){
			                    	getUserPayCount(newPage,type,path,startTime,endTime);
			                    }
			                }

			                $('#count-pages3').bootstrapPaginator(options);
			        }else{
			        	 $('#count-pages3').html("");
			        }
			        
			        $("td").each(function(){
			        	if($(this).html()=='undefined'){
			        		$(this).html("----");
			        	}
			          });
				}
			});
	   
	}
	
	function getProxyMoney(path,type){
		var inviteCode = $("#inviteCode").val();
		if(typeof(inviteCode)=="undefined")
			inviteCode="";
		$.ajax({	
			   type: "POST",
			   url: path+"controller/manager/getPaylogsSum?type="+type+"&inviteCode="+inviteCode,
			   data: "",
			   success: function data(data){
				   var returns =  eval('(' + data + ')');
					var total = returns.total;
					var mineone = returns.mineone;
					var minetwo = returns.minetwo;
					$("#sumtotal").html(total);
					$("#sumone").html(mineone);
					$("#sumtwo").html(minetwo);
				}
			});
	}
	
    
    $('.divide-count ul.nav li a').click(function(e){
        e.preventDefault();
        $(this).parent().addClass('active').siblings().removeClass('active');
    });

    //$.ajax({
    //    url:'2',
    //    data:{},
    //    success:function(data){
    //        console.log(data);
    //    }
    //})
