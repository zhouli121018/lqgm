/**
 * Created by Administrator on 2017-07-28.
 */
function getDetail(pageIndex,type,path,startTime,endTime,inviteCode){
	
		$.ajax({	
			   type: "POST",
			   url: path+"controller/manager/getPaylogs?pageNum="+pageIndex+"&type="+type+"&startTime="+startTime+"&endTime="+endTime+"&inviteCode="+inviteCode,
			   data: "",
			   success: function data(data){
				   var returns =  eval('(' + data + ')');
					var arr = returns.paylogs;
					var totalPages = returns.totalNum/10+1;
					if(returns.totalNum==0){
						alert("没有查到该代理数据！");
						return;
					}
				   for(var i=0,html='';i<arr.length;i++){
				        html+=`<tr>
							         <td>${arr[i].uuid}</td>
							         <td>${arr[i].nickName}</td>
							         <td>${arr[i].money}</td>
							         <td>${arr[i].paycount}</td>
							         <td>${arr[i].dateString}</td>
							         <td>${arr[i].status}</td>
							         <td>${arr[i].invitecode}</td>
							         <td>${arr[i].powerid}</td>
							         <td>${arr[i].invitecode1}</td>
							         <td>${arr[i].powerid1}</td>
							         <td>${arr[i].invitecode2}</td>
							         <td>${arr[i].powerid2}</td>
							         <td>${arr[i].invitecode3}</td>
							         <td>${arr[i].powerid3}</td>
				               </tr>`;
				    }

				    $('#detailTbl tbody').html(html);
			        $('#totalRecords').html(returns.totalNum);
			        if(totalPages>1){
			        	var options = {
			                    currentPage: pageIndex,
			                    totalPages: totalPages,
			                    bootstrapMajorVersion: 3,
			                    onPageChanged: function(e,oldPage,newPage){
			                    	getDetail(newPage,type,path,startTime,endTime,inviteCode);
			                    }
			                }

			                $('#detail-pages').bootstrapPaginator(options);
			        }else{
			        	 $('#detail-pages').html("");
			        }
			        $("td").each(function(){
			        	if($(this).html()=='undefined'){
			        		$(this).html("----");
			        	}
			        });
			        function power(obj){
			        	$(obj).each(function(i,dom){
				        	if($(dom).html()=='5'){
				        		$(dom).html("总代理");
				        	}else if($(dom).html()=='4'){
				        		$(dom).html("一级代理");
				        	}else if($(dom).html()=='3'){
				        		$(dom).html("二级代理");
				        	}else if($(dom).html()=='2'){
				        		$(dom).html("三级代理");
				        	}else if($(dom).html()=='1'){
				        		$(dom).html("管理员");
				        	}
				        });
			        }
			        power("td:nth-child(8)");
			        power("td:nth-child(10)");
			        power("td:nth-child(12)");
			        power("td:nth-child(14)");
			        
				}
			});
	   
	}