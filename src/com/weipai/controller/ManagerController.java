package com.weipai.controller;

import java.awt.print.Paper;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weipai.common.Params;
import com.weipai.controller.base.BaseController;
import com.weipai.model.Account;
import com.weipai.model.ContactWay;
import com.weipai.model.ExchangeLotExt;
import com.weipai.model.Gmconfig;
import com.weipai.model.Manager;
import com.weipai.model.ManagerNode;
import com.weipai.model.NoticeTable;
import com.weipai.model.Paylog;
import com.weipai.model.PaylogExt;
import com.weipai.model.PayloglotExt;
import com.weipai.model.PrizeRule;
import com.weipai.model.Roomcardlog;
import com.weipai.service.AccountService;
import com.weipai.service.ContactWayService;
import com.weipai.service.ExchangeLotService;
import com.weipai.service.GmconfigService;
import com.weipai.service.ManagerService;
import com.weipai.service.NoticeTableService;
import com.weipai.service.PaylogLotService;
import com.weipai.service.PaylogService;
import com.weipai.service.PrizeRuleService;
import com.weipai.service.RoomcardlogService;
import com.weipai.utils.StringUtil;

@Controller
@RequestMapping("/controller/manager")
public class ManagerController extends BaseController {

	@Autowired
	private ManagerService managerService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private NoticeTableService noticeTableService;
	@Autowired
	private PrizeRuleService prizeRuleService;
	@Autowired
	private ContactWayService contactWayService;
	

	@Autowired
	private PaylogService paylogService;
	
	@Autowired
	private GmconfigService gmconfigService;
	
	@Autowired
	private PaylogLotService paylogLotService;
	
	@Autowired
	private ExchangeLotService exchangeLotService;
	
	@Autowired
	private RoomcardlogService roomcardlogService;
	
	public static Map<Integer,List<ManagerNode>> managerCache = new HashMap<Integer,List<ManagerNode>>();
	public static Map<Integer,ManagerNode> managerNodeCache = new HashMap<Integer,ManagerNode>();
	public static Map<Integer,Long> cacheTimestamp = new HashMap<Integer,Long>();
	public static Map<Integer,Map<Integer,String>> pathCache = new HashMap<Integer,Map<Integer,String>>();
	public static Map<Integer,Integer> tixianDayLog = new ConcurrentHashMap<Integer,Integer>();

	/**
	 * 后台登录
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/login")
	public void login(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String yqm = request.getParameter("yqm");
		JSONObject json = new JSONObject();
		String kaptchaExpected = (String) request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY); 
		boolean kaptchaRight = kaptchaExpected.equalsIgnoreCase(yqm);
		if (kaptchaRight&&StringUtil.isNotEmpty(username) && StringUtil.isNotEmpty(password)) {//
			Manager manager =null;
			manager = managerService.selectManagerByUsername(username);
//			manager = managerService.selectManagerByInvoteCode(Integer.parseInt(username));
			Account account = new Account();
			if(manager!=null&&manager.getPowerId()>1)
			account = accountService.selectByManagerId(manager.getId());
			System.out.println(StringUtil.MD5(password));
			
			if (manager != null
					&& StringUtil.MD5(password).equals(manager.getPassword())) {
				json.put("mess", "7");
				//登录成功 用户信息放入缓存
				session.setAttribute("manager", manager);
				if(account!=null)
				session.setAttribute("userAccount", account);
				json.put("roomCard", account.getRoomcard());
				if(manager.getManagerUpId()!=null&&manager.getManagerUpId()>0){
					Manager managerUp = managerService.selectByPrimaryKey(manager.getManagerUpId());
					session.setAttribute("managerUp", managerUp);
				}
				if(manager.getPowerId() == 1){
					//如果是超管登录  则需要返回最新公告
//					NoticeTable notice = noticeTableService.selectRecentlyObject(new HashMap<String,Object>()).get(0);
//					ContactWay contactWay = contactWayService.selectByPrimaryKey(1);
//					if(notice != null){
//						session.setAttribute("notice", notice.getContent()+"");
//						session.setAttribute("contactway", contactWay.getContent()+"");
//					}
					json.put("mess", "0");
				}
				
				
				int powerId = manager.getPowerId();
				int type = 0;
				if(manager.getId()!=1){
					double n1 = 0,n2 = 0;
					if(manager.getRebate()!=null&&!"".equals(manager.getRebate())){
						String[] nums = manager.getRebate().split(":|;");
						n1 = Double.parseDouble(nums[0]);
						n2 = Double.parseDouble(nums[1]);
					}else{
						n1 = Params.getPercentage1(powerId);
						n2 = Params.getPercentage2(powerId);
					}
				int mineone = paylogService.sumByManagerId(manager.getId(), type);
				int minetwo = paylogService.sumSubByManagerId(manager.getId(), type,powerId);
				String oneString = "￥"+mineone+"("+100*n1+"%)";
				String twoString = "￥"+minetwo+"("+100*n2+"%)";
				DecimalFormat    df   = new DecimalFormat("######0.00");   
				String total = df.format(mineone*n1+minetwo*n2);
				session.setAttribute("mineone", oneString);
				session.setAttribute("minetwo", twoString);
				session.setAttribute("total", total);
				}else{
//					manager = managerService.selectByPrimaryKey(manager.getId());
//					int mineone = paylogService.sumByManagerId(manager.getId(), type);
//					int minetwo = paylogService.sumSubByManagerId(manager.getId(), type,powerId);
//					String oneString = "￥"+mineone+"("+100*Params.getPercentage1(manager.getPowerId())+"%)";
//					String twoString = "￥"+minetwo+"("+100*Params.getPercentage2(manager.getPowerId())+"%)";
//					DecimalFormat    df   = new DecimalFormat("######0.00");   
//					String total = df.format(mineone*Params.getPercentage1(manager.getPowerId())+minetwo*Params.getPercentage2(manager.getPowerId()));
//					session.setAttribute("mineone", oneString);
//					session.setAttribute("minetwo", twoString);
//					session.setAttribute("total", total);
				}
				
				
			} else {
				json.put("mess", "1");
			}
		} else {
			json.put("mess", "2");
		}
		returnMessage(response, json);
	}
	
	@RequestMapping("/getExchange")
	public void getExchange(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
		String pageNumber = request.getParameter("pageNum");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String uuid = request.getParameter("uuid");
		
		int pageNum = 1;
		if(pageNumber!=null){
			pageNum = Integer.parseInt(pageNumber);
		}
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		Map<String,Object> map = new HashMap<String, Object>();
		Manager manager = (Manager) session.getAttribute("manager");
		if(manager != null){
			int size = 0;
			int startNum = (pageNum-1)*Params.pageNumber;
			map.put("pageNumber", Params.pageNumber);
			map.put("startNum", startNum);
			try {
				if(startTime!=null&&!"".equals(startTime)){
				  Date	startdate = new Date(sdf.parse(startTime).getTime());
					map.put("startTime", startdate);
				}
				
				if(endTime!=null&&!"".equals(endTime)){
				  Date  enddate = new Date(sdf.parse(endTime).getTime());
					map.put("endTime", enddate);
				}
				
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if(uuid!=null&&!"".equals(uuid)){
				map.put("uuid", uuid);
			}
			
			List<ExchangeLotExt> exchanges = exchangeLotService.selectByMidTime(map);
			size = 	exchangeLotService.selectCount(map);
		
			json.put("totalNum", size);
			SimpleDateFormat s =   new SimpleDateFormat( "yyyy年MM月dd日 HH:mm:ss" );
			for (ExchangeLotExt m : exchanges) {
			    String time = s.format(m.getCreatetime());
			    m.setDatestring(time);
				array.add(m);
			}
			json.put("exchanges", array);
			returnMessage(response, json.toString());
		}
	}
	
	
	@RequestMapping("/proxyDetail")
	public String proxyDetail(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String managerId = request.getParameter("managerId");
		JSONObject json = new JSONObject();
		if (StringUtil.isNotEmpty(managerId)) {//
			Manager manager =null;
			manager = managerService.selectByPrimaryKey(Integer.parseInt(managerId));

			Account account = accountService.selectByManagerId(manager.getId());
			
			if (manager != null) {
				//登录成功 用户信息放入缓存
//				session.setAttribute("manager", manager);
				session.setAttribute("weixin", manager.getWeixin());
				if(account!=null)
				session.setAttribute("uuid", account.getUuid());
				else
					session.setAttribute("uuid", null);
				session.setAttribute("qq", manager.getQq());
				session.setAttribute("tel", manager.getTelephone());
				session.setAttribute("name", manager.getName());
				session.setAttribute("type", manager.getPowerId());
				session.setAttribute("id", manager.getId());
				session.setAttribute("proxy", manager);
			}
		} 
		return "proxyDetail";
	}
	
	/**
	 * 超管修改代理商密码
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getAllConfig")
	public void getAllConfig(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		List<Gmconfig> gmconfigs = gmconfigService.selectAll();
		for (Gmconfig gmconfig : gmconfigs) {
			array.add(gmconfig);
		}
		json.put("gmconfigs", array);
		returnMessage(response,json);
	}
	
	@RequestMapping("/addConfig")
	public void addConfig(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();

		String key0 = request.getParameter("key");
		String value0 = request.getParameter("value");
		String properties = request.getParameter("properties");
		String label = request.getParameter("label");
		JSONObject json = new JSONObject();
		Gmconfig addinfo = new Gmconfig();
		addinfo.setKey(key0);
		addinfo.setValue(value0);
		addinfo.setLabel(label);
		addinfo.setProperties(properties);
		addinfo.setCreatetime(new java.util.Date());
		
		int id0 = gmconfigService.insert(addinfo);
		int id1 = addinfo.getId();
		System.out.println("测试新增的id： "+id0);
		System.out.println("测试新增的id after： "+id1);
		json.put("id", id1);
		
		returnMessage(response, json);
	}
	
	@RequestMapping("/updateConfig")
	public void updateConfig(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		String id = request.getParameter("id");
		String key1 = request.getParameter("key");
		String value1 = request.getParameter("value");
		String properties = request.getParameter("properties");
		String label = request.getParameter("label");
		JSONObject json = new JSONObject();
		
		Gmconfig updateinfo = new Gmconfig();
		updateinfo.setKey(key1);
		updateinfo.setValue(value1);
		updateinfo.setLabel(label);
		updateinfo.setProperties(properties);
		updateinfo.setCreatetime(new java.util.Date());
		int updateId = gmconfigService.updateByPrimaryKey(updateinfo);
		System.out.println("测试updateId"+updateId);
		json.put("updateId", updateId);
		returnMessage(response, json);
	}
	
	@RequestMapping("/deleteConfig")
	public void deleteConfig(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		String id = request.getParameter("id");
		JSONObject json = new JSONObject();
		int delId = gmconfigService.deleteByPrimaryKey(Integer.parseInt(id));
		System.out.println("测试delId"+delId);
		json.put("delId", delId);
		returnMessage(response, json);

	}
	
	
	
	@RequestMapping("/updateManagerPwd")
	public void updateManagerPwd(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String managerId = request.getParameter("managerId");
		String newPwd = request.getParameter("newPwd");
		JSONObject json = new JSONObject();
		Manager manager = (Manager) session.getAttribute("manager");
		if(manager != null && manager.getPowerId() == 1){
			//只有超管才能找回代理商密码
			if(StringUtil.isInteger(managerId, 1000, 2) && StringUtil.isNotEmpty(newPwd)){
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("id",Integer.parseInt(managerId));
				map.put("password", StringUtil.MD5(newPwd));
				try {
					int i = managerService.updateByMap(map);
					if(i == 1){
						json.put("status_code","0");
					}
					else{
						json.put("status_code", "1");
						json.put("error", "传入参数有误!");
					}
				} catch (Exception e) {
					e.printStackTrace();
					json.put("status_code", "1");
					json.put("error", "信息修改异常!");
				}
			}
			else{
				json.put("status_code", "1");
				json.put("error", "传入参数有误!");
			}
		}
		else{
			json.put("status_code", "1");
			json.put("error", "只能通过超级管理员才能找回密码!");
		}
		returnMessage(response, json);
	}
	
	@RequestMapping("/zhuanzheng")
	public void zhuanzheng(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		String inviteCode = request.getParameter("inviteCode");
		String managerId = request.getParameter("managerId");
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("id", managerId);
		map.put("inviteCode", inviteCode);
		int result = managerService.updateByMap(map);
		//修改绑定用户的introducerId
		if(result>0){
		Account account = accountService.selectByManagerId(Integer.parseInt(managerId));
//		account.setIntroducer(0);
		map.clear();
		map.put("introducer", 0);
		map.put("id", account.getId());
		result = accountService.updateAccountStatus(map);
		if(result>0){
			json.put("status_code", "0");	
		}else{
			json.put("error", "修改绑定用户数据失败！");	
		}
		}else{
			json.put("error", "修改代理数据失败！");	
		}
		returnMessage(response,json);
		
	}
	
	
	@RequestMapping("/biangeng")
	public void biangeng(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		String inviteCode = request.getParameter("inviteCode");
		String managerId = request.getParameter("managerId");
		String parentInviteCode = request.getParameter("parentInviteCode");
		String userUuid = request.getParameter("userUuid");
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("id", managerId);
		if(inviteCode!=null&&!"".equals(inviteCode))
		map.put("inviteCode", inviteCode);
		if(parentInviteCode!=null&&!"".equals(parentInviteCode)){
			int pInviteCode = Integer.parseInt(parentInviteCode);
				Manager manager = managerService.selectManagerByInvoteCode(pInviteCode);
				map.put("managerUpId", manager.getId());
		}
		int result = managerService.updateByMap(map);
		//修改绑定用户的introducerId
		if(result>0){
		Account account = accountService.selectByManagerId(Integer.parseInt(managerId));
//		account.setIntroducer(0);
		map.clear();
		if(userUuid!=null&&!"".equals(userUuid)){
			int uuid = Integer.parseInt(userUuid);
				map.put("uuid", uuid);
		}
		map.put("id", account.getId());
		result = accountService.updateAccountStatus(map);
		if(result>0){
			json.put("status_code", "0");	
		}else{
			json.put("error", "修改绑定用户Id失败！");	
		}
		}else{
			json.put("error", "修改代理数据失败！");	
		}
		returnMessage(response,json);
		
	}
	
	
	@RequestMapping("/updateManagerPassword ")
	public void updateManagerPassword(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		String newPwd = request.getParameter("newPwd");
		String oldPwd = request.getParameter("oldPwd");
		boolean pwd =false;
		if(session != null){
			Manager manager = (Manager) session.getAttribute("manager");
			if(manager != null){
				Map<String,Object> map = new HashMap<String, Object>();
				if(StringUtil.isNotEmpty(newPwd)){
					if(StringUtil.isNotEmpty(oldPwd)){
						if(manager.getPassword().equals(StringUtil.MD5(oldPwd))){
							map.put("password", StringUtil.MD5(newPwd));
							pwd = true;
						}
						else{
							json.put("status", "1");
							json.put("error", "旧密码输入错误!");
						}
					}
					else{
						json.put("status", "1");
						json.put("error", "修改密码时，必须输入旧密码!");
					}
				}
				if(!map.isEmpty()){
					map.put("id", manager.getId());
					managerService.updateByMap(map);
					//更新session里面的manager信息
					if(pwd){
						manager.setPassword(StringUtil.MD5(newPwd));
					}
					session.setAttribute("manager",manager);
					json.put("status", "0");
				}
				else{
					json.put("status", "1");
					json.put("error", "传入参数有误");
					
				}
			}
			else{
				json.put("status", "1");
				json.put("error", "请先登录");
				
			}
		}
		returnMessage(response,json);
	}
	
	
	@RequestMapping("/updateManagerPasswordAgentManager ")
	public void updateManagerPasswordAgentManager(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		String id = request.getParameter("mid");
		String newPwd = request.getParameter("newPwd");
		if(session != null){
			Manager manager = (Manager) session.getAttribute("manager");
			if(manager != null){
				if(manager.getPowerId()==1){
					Map<String,Object> map = new HashMap<String, Object>();
					if(StringUtil.isNotEmpty(newPwd)){
						map.put("password", StringUtil.MD5(newPwd));
						map.put("id", id);
						int res = managerService.updateByMap(map);
						if(res>0){
							json.put("status", "1");
							json.put("msg", "修改密码成功！");
						}else{
							json.put("status", "0");
							json.put("msg", "修改密码失败！");
						}
					}
				}else{
					json.put("status", "0");
					json.put("error", "修改密码请联系管理员！");
					
				}
			}
			
		}
		returnMessage(response,json);
	}
	
	
	/**
	 *后台管理员修改个人信息(密码/手机号码)
	 * @param request
	 * @param response
	 */
	@RequestMapping("/updateManagerInfo ")
	public void updateManagerInfo(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();

		String mid = request.getParameter("mid");
		String weixin = request.getParameter("weixin");
		String qq = request.getParameter("qq");
		String name = request.getParameter("name");
		String rebate = request.getParameter("rebate");
		String rootManager = request.getParameter("rootManager");
		String telephone = request.getParameter("telephone");
		String uuid = request.getParameter("uuid");
		String status = request.getParameter("status");
		String powerId = request.getParameter("powerId");
		String inviteCode = request.getParameter("inviteCode");
		String parentInviteCode = request.getParameter("parentInviteCode");
		if(session != null){
			Manager manager = (Manager) session.getAttribute("manager");
			if(manager != null){
				if(manager.getPowerId()==1||manager.getRootManager()==1){//目前只有超级管理员和有可开代理权限才具有创建和修改代理的权限
					Map<String,Object> map = new HashMap<String, Object>();
					Manager edit = new Manager();
					
					if(StringUtil.isPhone(telephone)){
						edit.setTelephone(telephone);
						map.put("telephone", telephone);
					}
					if(StringUtil.isNotEmpty(weixin)){
						edit.setWeixin(weixin);
						map.put("weixin", weixin);
					}
					if(StringUtil.isNotEmpty(qq)){
						edit.setQq(qq);
						map.put("qq", qq);
					}
					if(StringUtil.isNotEmpty(name)){
						edit.setName(name);
						map.put("name", name);
					}
					if(StringUtil.isNotEmpty(powerId)){
						edit.setPowerId(Integer.parseInt(powerId));
						map.put("powerId", Integer.parseInt(powerId));
					}
					if(StringUtil.isNotEmpty(inviteCode)){
						edit.setInviteCode(Integer.parseInt(inviteCode));
						map.put("inviteCode", Integer.parseInt(inviteCode));
					}
					
					if(StringUtil.isNotEmpty(rootManager)){
						edit.setRootManager(Integer.parseInt(rootManager));
						map.put("rootManager", Integer.parseInt(rootManager));
					}
					if(StringUtil.isNotEmpty(status)){
						edit.setStatus(status);
						map.put("status", status);
					}
					if(manager.getPowerId()>1&&manager.getRootManager()==1){
						edit.setManagerUpId(manager.getId());
						map.put("managerUpId", manager.getId());
					}
					if(StringUtil.isNotEmpty(parentInviteCode)){
						int pinvite = Integer.parseInt(parentInviteCode);
						Manager p = managerService.selectManagerByInvoteCode(pinvite);
						edit.setManagerUpId(p.getId());
						map.put("managerUpId", p.getId());
					}
					if(StringUtil.isNotEmpty(rebate)){
						//加入校验逻辑
						double n1 = 0.0,n2 = 0.0,m1 = 0.0,m2 = 0.0;
						if(manager.getRebate()!=null&&!"".equals(manager.getRebate())){
						String[] nums = manager.getRebate().split(":|;");
							n1 = Double.parseDouble(nums[0]);
							n2 = Double.parseDouble(nums[1]);
						}else{
							n1 = Params.getPercentage1(manager.getPowerId());
							n2 = Params.getPercentage2(manager.getPowerId());
						}
						String[] nums2 = rebate.split(":|;");
						m1 = Double.parseDouble(nums2[0]);
						m2 = Double.parseDouble(nums2[1]);
						if(m1<=n1&&m2<=n2){
						edit.setRebate(rebate);
						map.put("rebate", rebate);
						}
					}
					int result = 0;
						if(StringUtil.isNotEmpty(mid)){
							edit.setId(Integer.parseInt(mid));
							map.put("id", Integer.parseInt(mid));
							result = managerService.updateByMap(map);
						}else{
							edit.setPassword("e10adc3949ba59abbe56e057f20f883e");
							result = managerService.saveSelective(edit);
							System.out.println(result);
							json.put("managerId", result);
						}
						if(result>0){
						for(Entry<Integer,Long> cacheentry:cacheTimestamp.entrySet()){
							long value = cacheentry.getValue();
							cacheentry.setValue(value-1000*60*5);
						}	
						}
						//重新绑定用户的ID信息
						if(result>0&&StringUtil.isNotEmpty(uuid)){
							int iuuid = Integer.parseInt(uuid);
							Account managerAccount = accountService.selectByUuid(iuuid);
							int managerUpId = edit.getId();
							Map<String,Object> pmap = new HashMap<String,Object>();
							pmap.put("id", managerAccount.getId());
							pmap.put("managerId", managerUpId);
							accountService.updateAccountStatus(pmap);
						}
						json.put("status", "0");
				}else{
					json.put("status", "1");
					json.put("error", "您没有开代理权限，请联系管理员！");
				}
			}
			else{
				json.put("status", "1");
				json.put("error", "请先登录");
				
			}
		}
		returnMessage(response,json);
	}
	
	/**
	 * 获取游戏服务器相关信息  开放数量，在线人数，总用户等信息
	 * 在本地取的参数有： 1:会员()总数  2：售出房卡总数   3：房间总数  4:玩家总数 5：总房卡数(玩家房卡总数额/代理商房卡总数)
	 * //6:今天创建房间总数 
	 * //7:今日售出(今天充值-今天退卡)
	 * 	//8:本周售出(本周充值-本周退卡)
	 * //9:今日新增用户
	 * 
	 * 
	 * 到游戏服务器取的参数有： 1：当前在线房间总数   2：当前在线人数
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getAllGameInfos")
	public void getAllGameInfos(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		Manager manager = (Manager) session.getAttribute("manager");
		if(manager != null && manager.getPowerId() == 1 ){
			json = managerService.getAllGameInfos();
		}
		returnMessage(response, json);
	}
	
	
	/**
	 * 获取所有玩家
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getAccounts")
	public void getAccounts(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		String pageNumber = request.getParameter("pageNum");
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String nickName = request.getParameter("nickName");
		
		System.out.println(startTime+"==============="+endTime);
		int pageNum = 1;
		if(pageNumber!=null){
			pageNum = Integer.parseInt(pageNumber);
		}
		Manager manager = (Manager) session.getAttribute("proxy");
		if(manager==null||request.getParameter("pageType")==null)
		manager = (Manager) session.getAttribute("manager");
		
//		String searchType = request.getParameter("type");
		if(manager != null){
			Map<String , Object> map = new HashMap<String, Object>();
			try {
			if(startTime!=null&&!"".equals(startTime)&&!"null".equals(startTime)){
				
					Date startdate = new Date(sdf.parse(startTime).getTime());
					map.put("startTime", startdate);
			}
			
			if(endTime!=null&&!"".equals(endTime)&&!"null".equals(endTime)){
				
				Date enddate = new Date(sdf.parse(endTime).getTime());
				map.put("endTime", enddate);
		}
			
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(nickName!=null&&!"".equals(nickName)&&!"null".equals(nickName)){
				
				map.put("nickname", nickName);
		}
			int size = 0;
			int startNum = (pageNum-1)*Params.pageNumber;
			map.put("startNum", startNum);
			map.put("pageNumber", Params.pageNumber);
			if(manager.getPowerId()!=1){//不是系统管理员需要限定访问查
				map.put("managerUpId", manager.getId());
				size = accountService.selectSzieByManagerId(map);
			}else{
				size = accountService.selectSzieByManagerId(map);
			}
			//获取所有玩家   
			List<Account> accounts = accountService.selectAllAccount(map);
			for (Account account : accounts) {
				array.add(account);
			}
			//获取所有玩家   
			int totalNum = (int) Math.ceil(size/(double)Params.pageNumber);
			json.put("totalNum", size);
		}
		json.put("users", array);
		
		System.out.println("getUsers============"+json.toJSONString());
		returnMessage(response, json);
	}
	
//	@RequestMapping("/getAllNotice")
//	public void getAllNotice(HttpServletRequest request, HttpServletResponse response){
//		HttpSession session = request.getSession();
//		int managerId  = ((Manager) session.getAttribute("manager")).getId();
//		Map<String,Object> params = new HashMap<String,Object>();
//		JSONObject json = new JSONObject();
//		JSONArray array = new JSONArray();
//		List<NoticeTable> notices = noticeTableService.selectRecentlyObject(params);;
//		for (NoticeTable account : notices) {
//			array.add(account);
//		}
//		json.put("notices", array);
//		returnMessage(response,json);
//	}
	
	@RequestMapping("/getAllNotice")
	public void getAllNotice(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		String pageNumber = request.getParameter("pageNum");
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		int pageNum = 1;
		if(pageNumber!=null){
			pageNum = Integer.parseInt(pageNumber);
		}
		int powerId  = ((Manager) session.getAttribute("manager")).getPowerId();
	    if(powerId==1){
	    	Map<String,Object> map = new HashMap<String,Object>();
	    	int startNum = (pageNum-1)*Params.pageNumber;
			map.put("pageNumber", Params.pageNumber);
			map.put("startNum", startNum);
			List<NoticeTable> notices = noticeTableService.selectAllNotice(map);
			int size=0;
			size = noticeTableService.selectAllNoticeCount();
			for (NoticeTable account : notices) {
				array.add(account);
			}
			json.put("notices", array);
			json.put("size", size);
			returnMessage(response,json);
	    }
	}
	
	@RequestMapping("/deleteNotece")
	public void deleteNotece(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		String noticeId = request.getParameter("noticeId");
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		int powerId  = ((Manager) session.getAttribute("manager")).getPowerId();
	    if(powerId==1){
			int res = noticeTableService.deleteByPrimaryKey(Integer.parseInt(noticeId));
			json.put("res", res);
			returnMessage(response,json);
	    }
	}
	
	@RequestMapping("/changeNoticeStatus")
	public void changeNoticeStatus(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		String noticeId = request.getParameter("noticeId");
		String newStatus = request.getParameter("newStatus");
		JSONObject json = new JSONObject();
		int powerId  = ((Manager) session.getAttribute("manager")).getPowerId();
	    if(powerId==1){
	    	NoticeTable record = new NoticeTable();
	    	record.setId(Integer.parseInt(noticeId));
	    	record.setStatus(Integer.parseInt(newStatus));
			int res = noticeTableService.updateStatusByPrimaryKey(record);
			json.put("res", res);
			returnMessage(response,json);
	    }
	}
	
	/**
	 * 获取所有经销商 代理商
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getManagers")
	public void getManagers(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		String pageNumber = request.getParameter("pageNum");
		int pageNum = 1;
		if(pageNumber!=null){
			pageNum = Integer.parseInt(pageNumber);
		}
		Manager manager = (Manager) session.getAttribute("proxy");
		if(manager==null||request.getParameter("pageType")==null)
		manager = (Manager) session.getAttribute("manager");
		if(manager != null){
			Map<String , Integer> map = new HashMap<String, Integer>();
			
			int size = 0;
			int startNum = (pageNum-1)*Params.pageNumber;
			map.put("pageNumber", Params.pageNumber);
			if(manager.getPowerId() != 1){//一般代理
				map.put("startNum", startNum);
				//普通代理商只能获取自己下面的
				map.put("managerUpId", manager.getId());
				size = managerService.selectSizeByManagerId(manager.getId());
			}else{
				//超级管理员可以获取所有
				map.put("startNum", startNum);
				size = managerService.selectSizeByManagerId(0);
			}
			//获取所有玩家   
			int totalNum = (int) Math.ceil(size/(double)Params.pageNumber);
			json.put("totalNum", totalNum);
			List<Manager> managers = managerService.selectObjectsByMap(map);
			for (Manager m : managers) {
				if(m.getWeixin()==null)
					m.setWeixin("");
				if(m.getQq()==null)
					m.setQq("");
				array.add(m);
			}
			json.put("managers", array);
			
			System.out.println("getManagers============"+json.toJSONString());
			returnMessage(response, json);
		}
	}
	
	@RequestMapping("/getManagers2")
	public void getManagers2(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		String searchType = request.getParameter("searchType");
		int stype = 0;
		if(searchType!=null&&!"".equals(searchType))
			stype = Integer.parseInt(searchType);
		String pageNumber = request.getParameter("pageNum");
		int pageNum = 1;
		if(pageNumber!=null){
			pageNum = Integer.parseInt(pageNumber);
		}
		Manager manager = (Manager) session.getAttribute("manager");
		if(manager != null){
			Map<String , Object> map = new HashMap<String, Object>();
			int size = 0;
			int startNum = (pageNum-1)*Params.pageNumber;
			map.put("pageNumber", Params.pageNumber);
			map.put("startNum", startNum);
			List<ManagerNode> managerTree = null;
			Map<Integer,String> parentPath = null;
			if(cacheTimestamp.containsKey(stype)){
				long cacheTime = cacheTimestamp.get(stype);
				long now = new java.util.Date().getTime();
				if((now-cacheTime)<(1000*60*5)){//直接取缓存
					managerTree = managerCache.get(stype);
					parentPath = pathCache.get(stype);
				}
			}
			if(managerTree==null){
			List<Manager> managers = managerService.selectManagerInfoByMap(map,stype);
			size = managers.size();//需要加载全部的树
				managerTree = new ArrayList<ManagerNode>();
				List<Manager> waitting2Tree = new ArrayList<Manager>();
				parentPath = new HashMap<Integer,String>();
				for(Manager curManager:managers){
					if(curManager.getManagerUpId()!=null&&curManager.getManagerUpId()>1){//为存在父节点的代理
						waitting2Tree.add(curManager);
					}else{//已是根节点，直接放入树中
						ManagerNode node1 = new ManagerNode(curManager);
						managerNodeCache.put(curManager.getId(), node1);
						managerTree.add(node1);
						parentPath.put(curManager.getId(), ""+(managerTree.size()-1));
					}
				}
				int allSize = waitting2Tree.size();
				int iterNum = allSize;
				for(int k=0;k<5;k++){
				for(int i=0;i<allSize;i++){
					if(iterNum==0)
						break;
					if(waitting2Tree.size()>i&&waitting2Tree.get(i)!=null){//存在且不为空对象
						int mid = waitting2Tree.get(i).getManagerUpId();
						if(parentPath.containsKey(mid)){
						String path = parentPath.get(mid);
						String[] paths = path.split("-");
						List<ManagerNode> curNode = managerTree;
						for(String curPath:paths){
							int curPathI = Integer.parseInt(curPath);
							curNode = curNode.get(curPathI).getChildagent();
						}
						ManagerNode node1 = new ManagerNode(waitting2Tree.get(i));
						managerNodeCache.put(waitting2Tree.get(i).getId(), node1);
						curNode.add(node1);//加入到子树列表
						parentPath.put(waitting2Tree.get(i).getId(), path+"-"+(curNode.size()-1));
						waitting2Tree.set(i, null);
						iterNum--;
						if(iterNum==0)
							break;
						}
					}
				}
				}
				cacheTimestamp.put(stype, new java.util.Date().getTime());
				managerCache.put(stype, managerTree);
				pathCache.put(stype, parentPath);
				
			}
				if(manager.getPowerId() == 1){//超级管理员是所有的,每次查询一页记录
				size = managerTree.size();
				for(int i=0;i<size;i++){
					if(i>=startNum&&i<(startNum+Params.pageNumber))
					array.add(managerTree.get(i));
				}
				}else{
					String path = parentPath.get(manager.getId());
					String[] paths = path.split("-");
					List<ManagerNode> curNode = managerTree;
					for(String curPath:paths){
						int curPathI = Integer.parseInt(curPath);
						curNode = curNode.get(curPathI).getChildagent();
					}
					size = curNode.size();
					for (ManagerNode m : curNode){
						array.add(m);
					}
					
				}

				System.out.println("getManagers+++============"+array.toJSONString());
//			}
			//获取所有玩家   
//			int totalNum = (int) Math.ceil(size/(double)Params.pageNumber);
			json.put("totalNum", size);
			
			json.put("managers", array);
			
//			System.out.println("getManagers============"+json.toJSONString());
			returnMessage(response, json.toJSONString());
		}
	}
	
	
	@RequestMapping("/getManagersStats")
	public void getManagersStats(HttpServletRequest request, HttpServletResponse response) {
		 Map<Integer,List<ManagerNode>> managerCache = new HashMap<Integer,List<ManagerNode>>();
		 Map<Integer,ManagerNode> managerNodeCache = new HashMap<Integer,ManagerNode>();
		 Map<Integer,Long> cacheTimestamp = new HashMap<Integer,Long>();
		 Map<Integer,Map<Integer,String>> pathCache = new HashMap<Integer,Map<Integer,String>>();
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		String searchType = request.getParameter("searchType");
		int stype = 0;
		if(searchType!=null&&!"".equals(searchType))
			stype = Integer.parseInt(searchType);
		String pageNumber = request.getParameter("pageNum");
		int pageNum = 1;
		if(pageNumber!=null){
			pageNum = Integer.parseInt(pageNumber);
		}
		Manager manager = (Manager) session.getAttribute("manager");
		if(manager != null){
			Map<String , Object> map = new HashMap<String, Object>();
			int size = 0;
			int startNum = (pageNum-1)*Params.pageNumber;
			map.put("pageNumber", Params.pageNumber);
			map.put("startNum", startNum);
			List<ManagerNode> managerTree = null;
			Map<Integer,String> parentPath = null;
			if(cacheTimestamp.containsKey(stype)){
				long cacheTime = cacheTimestamp.get(stype);
				long now = new java.util.Date().getTime();
				if((now-cacheTime)<(1000*60*5)){//直接取缓存
					managerTree = managerCache.get(stype);
					parentPath = pathCache.get(stype);
				}
			}
			if(managerTree==null){
			List<Manager> managers = managerService.selectManagerInfoByMapStats(map,stype);
			size = managers.size();//需要加载全部的树
				managerTree = new ArrayList<ManagerNode>();
				List<Manager> waitting2Tree = new ArrayList<Manager>();
				parentPath = new HashMap<Integer,String>();
				for(Manager curManager:managers){
					if(curManager.getManagerUpId()!=null&&curManager.getManagerUpId()>1){//为存在父节点的代理
						waitting2Tree.add(curManager);
					}else{//已是根节点，直接放入树中
						ManagerNode node1 = new ManagerNode(curManager);
						managerNodeCache.put(curManager.getId(), node1);
						managerTree.add(node1);
						parentPath.put(curManager.getId(), ""+(managerTree.size()-1));
					}
				}
				int allSize = waitting2Tree.size();
				int iterNum = allSize;
				for(int k=0;k<5;k++){
				for(int i=0;i<allSize;i++){
					if(iterNum==0)
						break;
					if(waitting2Tree.size()>i&&waitting2Tree.get(i)!=null){//存在且不为空对象
						int mid = waitting2Tree.get(i).getManagerUpId();
						if(parentPath.containsKey(mid)){
						String path = parentPath.get(mid);
						String[] paths = path.split("-");
						List<ManagerNode> curNode = managerTree;
						for(String curPath:paths){
							int curPathI = Integer.parseInt(curPath);
							curNode = curNode.get(curPathI).getChildagent();
						}
						ManagerNode node1 = new ManagerNode(waitting2Tree.get(i));
						managerNodeCache.put(waitting2Tree.get(i).getId(), node1);
						curNode.add(node1);//加入到子树列表
						parentPath.put(waitting2Tree.get(i).getId(), path+"-"+(curNode.size()-1));
						waitting2Tree.set(i, null);
						iterNum--;
						if(iterNum==0)
							break;
						}
					}
				}
				}
				cacheTimestamp.put(stype, new java.util.Date().getTime());
				managerCache.put(stype, managerTree);
				pathCache.put(stype, parentPath);
				
			}
				if(manager.getPowerId() == 1){//超级管理员是所有的,每次查询一页记录
				size = managerTree.size();
				for(int i=0;i<size;i++){
					if(i>=startNum&&i<(startNum+Params.pageNumber))
					array.add(managerTree.get(i));
				}
				}else{
					String path = parentPath.get(manager.getId());
					String[] paths = path.split("-");
					List<ManagerNode> curNode = managerTree;
					for(String curPath:paths){
						int curPathI = Integer.parseInt(curPath);
						curNode = curNode.get(curPathI).getChildagent();
					}
					size = curNode.size();
					for (ManagerNode m : curNode){
						array.add(m);
					}
					
				}

				System.out.println("getManagers+++============"+array.toJSONString());
//			}
			//获取所有玩家   
//			int totalNum = (int) Math.ceil(size/(double)Params.pageNumber);
			json.put("totalNum", size);
			
			json.put("managers", array);
			
//			System.out.println("getManagers============"+json.toJSONString());
			returnMessage(response, json.toJSONString());
		}
	}
	
	
	@RequestMapping("/getPayCount")
	public void getPayCount(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String searchType = request.getParameter("searchType");
		int stype = 0;
		if(searchType!=null&&!"".equals(searchType))
			stype = Integer.parseInt(searchType);

		String pageNumber = request.getParameter("pageNum");
		int pageNum = 1;
		if(pageNumber!=null){
			pageNum = Integer.parseInt(pageNumber);
		}
		Manager manager = (Manager) session.getAttribute("manager");
		if(manager != null){
			Map<String , Object> map = new HashMap<String, Object>();
			
			int size = 0;
			int startNum = (pageNum-1)*Params.pageNumber;
			map.put("pageNumber", Params.pageNumber);
			map.put("startNum", startNum);
			
			try {
				if(startTime!=null&&!"".equals(startTime)){
					Date startdate = new Date(sdf.parse(startTime).getTime());
					map.put("startTime", startdate);
				}
				
				if(endTime!=null&&!"".equals(endTime)){
					Date enddate = new Date(sdf.parse(endTime).getTime());
					map.put("endTime", enddate);
				}
				
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			List<Manager> managers = null;
			if(manager.getPowerId() != 1){//一般代理,无需分页
				//普通代理商只能获取自己下面的
				map.put("managerId", manager.getId());
				size = managerService.selectManagerInfoByMapCount(map,stype);
				managers = managerService.selectManagerInfoByMap(map,stype);
			}else{
				//超级管理员可以获取所有，所以需要分页
				size = managerService.selectManagerInfoByMapCount(map,stype);
				managers = managerService.selectManagerInfoByMap(map,stype);
			}
			System.out.println("getPayCount+++============"+size);
			for (int i=startNum;i<(startNum+Params.pageNumber)&&i<managers.size();i++) {
				Manager m = managers.get(i);
				array.add(m);
			}
			//获取所有玩家   
//			int totalNum = (int) Math.ceil(size/(double)Params.pageNumber);
			json.put("totalNum", size);
			
			json.put("managers", array);
			
//			System.out.println("getManagers============"+json.toJSONString());
			returnMessage(response, json.toJSONString());
		}
	}
	
	
	@RequestMapping("/getUserPayCount")
	public void getUserPayCount(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String searchType = request.getParameter("searchType");
		Date startdate = null,enddate = null;
		int stype = 0;
		int managerId = 0;
		if(searchType!=null&&!"".equals(searchType))
			stype = Integer.parseInt(searchType);

		String pageNumber = request.getParameter("pageNum");
		int pageNum = 1;
		if(pageNumber!=null){
			pageNum = Integer.parseInt(pageNumber);
		}
		Manager manager = (Manager) session.getAttribute("manager");
		if(manager != null){
			Map<String , Object> map = new HashMap<String, Object>();
			
			int size = 0;
			int startNum = (pageNum-1)*Params.pageNumber;
			map.put("pageNumber", Params.pageNumber);
			map.put("startNum", startNum);
			
			try {
				if(startTime!=null&&!"".equals(startTime)){
					startdate = new Date(sdf.parse(startTime).getTime());
					map.put("startTime", startdate);
				}
				
				if(endTime!=null&&!"".equals(endTime)){
					enddate = new Date(sdf.parse(endTime).getTime());
					map.put("endTime", enddate);
				}
				
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			List<Paylog> payLogs = null;
			if(manager.getPowerId() != 1){//一般代理,无需分页
				managerId = manager.getId();
			}
				size = paylogService.selectUserPaySumCount(managerId, stype, startNum, Params.pageNumber, startdate, enddate);
				payLogs = paylogService.selectUserPaySum(managerId, stype, startNum, Params.pageNumber, startdate, enddate);
			
			System.out.println("getPayCount+++============"+size);
			for (int i=0;i<payLogs.size();i++) {
				Paylog m = payLogs.get(i);
				array.add(m);
			}
			//获取所有玩家   
//			int totalNum = (int) Math.ceil(size/(double)Params.pageNumber);
			json.put("totalNum", size);
			
			json.put("managers", array);
			
//			System.out.println("getManagers============"+json.toJSONString());
			returnMessage(response, json.toJSONString());
		}
	}
	
	@RequestMapping("/getUserCount")
	public void getUserCount(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String pageNumber = request.getParameter("pageNum");
		int pageNum = 1;
		if(pageNumber!=null){
			pageNum = Integer.parseInt(pageNumber);
		}
		Manager manager = (Manager) session.getAttribute("manager");
		if(manager != null){
			Map<String , Object> map = new HashMap<String, Object>();
			
			int size = 0;
			int startNum = (pageNum-1)*Params.pageNumber;
			map.put("pageNumber", Params.pageNumber);
			map.put("startNum",startNum);
			
			try {
				if(startTime!=null&&!"".equals(startTime)){
					Date startdate = new Date(sdf.parse(startTime).getTime());
					map.put("startTime", startdate);
				}
				
				if(endTime!=null&&!"".equals(endTime)){
					Date enddate = new Date(sdf.parse(endTime).getTime());
					map.put("endTime", enddate);
				}
				
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			List<Manager> managers = null;
			if(manager.getPowerId() != 1){//一般代理,无需分页
				//普通代理商只能获取自己下面的
				map.put("managerId", manager.getId());
				size = managerService.selectManagerNewUserByMapCount(map);
				managers = managerService.selectManagerNewUserByMap(map);
			}else{
				//超级管理员可以获取所有，所以需要分页
				size = managerService.selectManagerNewUserByMapCount(map);
				managers = managerService.selectManagerNewUserByMap(map);
			}
			System.out.println("getUserCount+++============"+size);
			for (int i=0;i<10&&i<managers.size();i++) {
				Manager m = managers.get(i);
				array.add(m);
			}
			//获取所有玩家   
//			int totalNum = (int) Math.ceil(size/(double)Params.pageNumber);
			json.put("totalNum", size);
			
			json.put("managers", array);
			
//			System.out.println("getManagers============"+json.toJSONString());
			returnMessage(response, json.toJSONString());
		}
	}
	
	
	
	@RequestMapping("/getPaylogs")
	public void getPaylogs(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String pageNumber = request.getParameter("pageNum");
		String searchType = request.getParameter("type");
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String inviteCode = request.getParameter("inviteCode");
		System.out.println(startTime+"==============="+endTime);
		int pageNum = 1,type = 0,payType = 0;
		if(pageNumber!=null){
			pageNum = Integer.parseInt(pageNumber);
		}
		if(searchType!=null){
			type = Integer.parseInt(searchType);
		}
		JSONObject json = new JSONObject();
		
		JSONArray array = new JSONArray();
		Manager manager = (Manager) session.getAttribute("proxy");
		if(manager==null||request.getParameter("pageType")==null)
		manager = (Manager) session.getAttribute("manager");
		if(manager != null){
			int startNum = (pageNum-1)*Params.pageNumber;
			int limit = Params.pageNumber;
			int size = 0;
			Date startdate = null;
			Date enddate = null;
			try {
				if(startTime!=null&&!"".equals(startTime)){
					startdate = new Date(sdf.parse(startTime).getTime());
				}
				
				if(endTime!=null&&!"".equals(endTime)){
					enddate = new Date(sdf.parse(endTime).getTime());
				}
				
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			int inputManagerId = 0;
			if(inviteCode!=null&&!"".equals(inviteCode)){
				int inputInviteCode = Integer.parseInt(inviteCode);
				Manager inputManager = managerService.selectManagerByInvoteCode(inputInviteCode);
				if(inputManager!=null){
					inputManagerId = inputManager.getId();
					int inputManagerUpId = 0;
					if(inputManager.getManagerUpId()!=null&&inputManager.getManagerUpId()>1)
						inputManagerUpId = inputManager.getManagerUpId();
					if(manager.getPowerId()>1&&manager.getId().intValue()!=inputManagerUpId&&manager.getId().intValue()!=inputManagerId){//不是系统管理员且,不是自己下属，且不是本人
						json.put("paylogs", null);
						json.put("totalNum", 0);
						returnMessage(response, json.toString());
						return;
					}
				}
			}
			if(inputManagerId>0){
				size = paylogService.selectByMidTimeCount(inputManagerId, 0,startNum,limit,startdate,enddate);
			}else{
			if(manager.getPowerId()>1)
			size = paylogService.selectByMidTimeCount(manager.getId(), 0,startNum,limit,startdate,enddate);
			else
				size = paylogService.selectByAllTimeCount(0,startNum,limit,startdate,enddate);
			}
//			int totalNum = (int) Math.ceil(size/(double)Params.pageNumber);
			json.put("totalNum", size);
			//获取所有玩家   
			SimpleDateFormat time=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss"); 
			List<PaylogExt> paylogs =null;
			if(inputManagerId>0){
				paylogs = paylogService.selectByMidTime(inputManagerId, 0,startNum , limit,startdate,enddate);
			}else{
			if(manager.getPowerId()>1)
			paylogs = paylogService.selectByMidTime(manager.getId(), 0,startNum , limit,startdate,enddate);
			else
				paylogs = paylogService.selectByAllTime(type,startNum , limit,startdate,enddate);
			}
			for(PaylogExt n:paylogs){
				n.setNickName(accountService.selectByUuid(n.getUuid()).getNickname());
			}
			for (PaylogExt m : paylogs) {
				m.setDateString(time.format(m.getPaytime()));
				array.add(m);
			}
			
			json.put("paylogs", array);
			System.out.println("返回的字符串==========="+json.toString());
			returnMessage(response, json.toString());
		}
	}
	
	@RequestMapping("/getPaylogLots")
	public void getPaylogLots(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String pageNumber = request.getParameter("pageNum");
		String searchType = request.getParameter("type");
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String inviteCode = request.getParameter("inviteCode");
		System.out.println(startTime+"==============="+endTime);
		int pageNum = 1,type = 0,payType = 0;
		if(pageNumber!=null){
			pageNum = Integer.parseInt(pageNumber);
		}
		if(searchType!=null){
			type = Integer.parseInt(searchType);
		}
		JSONObject json = new JSONObject();
		
		JSONArray array = new JSONArray();
		Manager manager = (Manager) session.getAttribute("proxy");
		if(manager==null||request.getParameter("pageType")==null)
		manager = (Manager) session.getAttribute("manager");
		if(manager != null){
			int startNum = (pageNum-1)*Params.pageNumber;
			int limit = Params.pageNumber;
			int size = 0;
			Date startdate = null;
			Date enddate = null;
			try {
				if(startTime!=null&&!"".equals(startTime)){
					startdate = new Date(sdf.parse(startTime).getTime());
				}
				
				if(endTime!=null&&!"".equals(endTime)){
					enddate = new Date(sdf.parse(endTime).getTime());
				}
				
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			int inputManagerId = 0;
			if(inviteCode!=null&&!"".equals(inviteCode)){
				int inputInviteCode = Integer.parseInt(inviteCode);
				Manager inputManager = managerService.selectManagerByInvoteCode(inputInviteCode);
				if(inputManager!=null){
					inputManagerId = inputManager.getId();
					int inputManagerUpId = 0;
					if(inputManager.getManagerUpId()!=null&&inputManager.getManagerUpId()>1)
						inputManagerUpId = inputManager.getManagerUpId();
					if(manager.getPowerId()>1&&manager.getId().intValue()!=inputManagerUpId&&manager.getId().intValue()!=inputManagerId){//不是系统管理员且,不是自己下属，且不是本人
						json.put("paylogLots", null);
						json.put("totalNum", 0);
						returnMessage(response, json.toString());
						return;
					}
				}
			}
			if(inputManagerId>0){
				size = paylogLotService.selectByMidTimeCount(inputManagerId, 0,startNum,limit,startdate,enddate);
			}else{
			if(manager.getPowerId()>1)
			size = paylogLotService.selectByMidTimeCount(manager.getId(), 0,startNum,limit,startdate,enddate);
			else
				size = paylogLotService.selectByAllTimeCount(0,startNum,limit,startdate,enddate);
			}
			int totalNum = (int) Math.ceil(size/(double)Params.pageNumber);
			json.put("totalNum", size);
			//获取所有玩家   
			SimpleDateFormat time=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss"); 
			List<PayloglotExt> paylogLots =null;
			if(inputManagerId>0){
				paylogLots = paylogLotService.selectByMidTime(inputManagerId, 0,startNum , limit,startdate,enddate);
			}else{
			if(manager.getPowerId()>1)
			paylogLots = paylogLotService.selectByMidTime(manager.getId(), 0,startNum , limit,startdate,enddate);
			else
				paylogLots = paylogLotService.selectByAllTime(type,startNum , limit,startdate,enddate);
			}
			for (PayloglotExt m : paylogLots) {
				m.setDateString(time.format(m.getPaytime()));
				array.add(m);
			}
			
			json.put("paylogLots", array);
			System.out.println("返回的字符串==========="+json.toString());
			returnMessage(response, json.toString());
		}
	}
	
	@RequestMapping("/withdrawlogs")
	public void withdrawlogs(HttpServletRequest request, HttpServletResponse response) {
		
		
		HttpSession session = request.getSession();
		String pageNumber = request.getParameter("pageNum");
		String searchType = request.getParameter("type");
		String inpayType = request.getParameter("payType");
		String inviteCode = request.getParameter("inviteCode");
		int pageNum = 1,type = 0,payType = 0;
		if(pageNumber!=null){
			pageNum = Integer.parseInt(pageNumber);
		}
		if(searchType!=null){
			type = Integer.parseInt(searchType);
		}
		if(inpayType!=null){
			payType = Integer.parseInt(inpayType);
		}
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		Manager manager = (Manager) session.getAttribute("manager");
		if(manager != null){
			int inputManagerId = 0,size=0;
			if(inviteCode!=null&&!"".equals(inviteCode)){
				int inputInviteCode = Integer.parseInt(inviteCode);
				Manager inputManager = managerService.selectManagerByInvoteCode(inputInviteCode);
				int inputUpId = 0;
				if(inputManager!=null){
					inputManagerId = inputManager.getId();
					if(inputManager.getManagerUpId()!=null&&inputManager.getManagerUpId()>1)
						inputUpId = inputManager.getManagerUpId();
					if(manager.getPowerId()>1&&(inputUpId!=manager.getId().intValue())&&inputManagerId!=manager.getId().intValue()){//不为系统管理员且不是所属代理
						json.put("paylogs", null);
						json.put("totalNum", 0);
						returnMessage(response, json.toString());
						return;
					}
				}
			}
			if(inputManagerId>0){
				size = paylogService.selectSumByManagerId(inputManagerId, type,payType);
			}else{
				size = paylogService.selectSumByManagerId(manager.getId(), type,payType);
			}
			
			json.put("totalNum", size);
			//获取所有玩家   
			int startNum = (pageNum-1)*Params.pageNumber;
			int limit = Params.pageNumber;
			SimpleDateFormat time=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss"); 
			List<Paylog> paylogs = null;
			if(inputManagerId>0)
			paylogs = paylogService.selectByManagerId(inputManagerId, type,payType,startNum , limit);
			else
				paylogs = paylogService.selectByManagerId(manager.getId(), type,payType,startNum , limit);
			
			for(Paylog n : paylogs){
				int searchManagerId = managerService.selectManagerByInvoteCode(n.getManagerid()).getId();
				String nick = accountService.selectByManagerId(searchManagerId).getNickname(); 
				if(nick==null){
					nick="0";
				}
				n.setNickName(nick);
			}
			for (Paylog m : paylogs) {
				m.setDateString(time.format(m.getPaytime()));
				int mid = m.getManagerid();
				if(managerNodeCache.containsKey(mid))
				m.setManagerid(managerNodeCache.get(mid).getInviteCode());
				array.add(m);
			}
			json.put("paylogs", array);
			System.out.println("返回的字符串==========="+json.toString());
			returnMessage(response, json.toString());
		}
		
		
		
	}
	
	
	@RequestMapping("/getPaylogsSum")
	public void getPaylogsSum(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String searchType = request.getParameter("type");
		String managerIdIn = request.getParameter("inviteCode");
		Manager managerin = null;
		int type = 0,managerId=0;
		if(searchType!=null){
			type = Integer.parseInt(searchType);
		}
		if(managerIdIn!=null&&!"".equals(managerIdIn)){
			int inviteCode = Integer.parseInt(managerIdIn);
			managerin = managerService.selectManagerByInvoteCode(inviteCode);
			if(managerin!=null)
				managerId = managerin.getId();
		}
		JSONObject json = new JSONObject();
		double mineone = 0.0, minetwo = 0.0;
		
		Manager manager = (Manager) session.getAttribute("proxy");
		if(manager==null)
		manager = (Manager) session.getAttribute("manager");
		if(manager != null){
			//获取所有玩家   
			int powerId = manager.getPowerId();
			int mid = manager.getId();
			String mdebate = manager.getRebate();
			if(manager.getPowerId()!=1&&managerId==0){//一般代理查询
			
			}else if(managerId>0){
				powerId = managerin.getPowerId();
				mid = managerin.getId();
				mdebate = manager.getRebate();
			}
			
			double n1 = 0,n2 = 0;
			if(mdebate!=null&&!"".equals(mdebate)){
				String[] nums = mdebate.split(":|;");
				n1 = Double.parseDouble(nums[0]);
				n2 = Double.parseDouble(nums[1]);
			}else{
				n1 = Params.getPercentage1(powerId);
				n2 = Params.getPercentage2(powerId);
				
			}
			mineone = paylogService.sumByManagerId(mid, type);
			minetwo = paylogService.sumSubByManagerId(mid, type,powerId);
			String oneString = "￥"+mineone+"("+100*n1+"%)";
			String twoString = "￥"+minetwo+"("+100*n2+"%)";
			DecimalFormat    df   = new DecimalFormat("######0.00");   
			String total = "分成："+df.format(mineone*n1+minetwo*n2);
			json.put("mineone", oneString);
			json.put("minetwo", twoString);
			json.put("total", total);

			returnMessage(response, json.toString());
		}
	}
	
	@RequestMapping("/tixian")
	public void tixian(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		Manager manager = (Manager) session.getAttribute("manager");
		synchronized(this){
		if(manager != null){
			//获取所有玩家   
			int mineone = paylogService.sumByManagerId(manager.getId(), 0);
			int minetwo = 0;
			int powerId = manager.getPowerId();
			double n1 = 0,n2 = 0;
			if(manager.getRebate()!=null&&!"".equals(manager.getRebate())){
				String[] nums = manager.getRebate().split(":|;");
				n1 = Double.parseDouble(nums[0]);
				n2 = Double.parseDouble(nums[1]);
			}else{
				n1 = Params.getPercentage1(powerId);
				n2 = Params.getPercentage2(powerId);
			}
				minetwo = paylogService.sumSubByManagerId(manager.getId(), 0,powerId);
			double total = mineone*n1+minetwo*n2;
			if(total>0){
				Account acc = accountService.selectByManagerId(manager.getId());
				Paylog lastpaylog = paylogService.selectByUuidAndStatus(acc.getUuid());
	            if(lastpaylog!=null){
	            	json.put("error", "您的提现正在处理中~~~请留意你的微信转账记录！");
	            	returnMessage(response, json.toString());
	            	return;
	            }
	            Calendar today = Calendar.getInstance();
				int day = today.get(Calendar.DAY_OF_YEAR);
				int managerId = manager.getId();
	            if(tixianDayLog.containsKey(managerId)&&tixianDayLog.get(managerId)==0){
					json.put("error", "一天最多只能提现两次！如有问题，请联系系统管理员");
					returnMessage(response, json.toString());
					return;
				}
			    Paylog paylog = new Paylog();
	            paylog.setManagerid(manager.getId());
	            paylog.setMoney(total);
	            paylog.setPaycount(0);
	            paylog.setPaytime(new java.util.Date());
	            paylog.setPaytype(1);//0是用户充值，1是代理提现
	            paylog.setStatus(0);//0为正常状态，1为已经处理的状态，2是提现失败的状态
	            paylog.setUuid(acc.getUuid());
	            int result = paylogService.insertPaylog(paylog);
	            
	           
	            if(result>0){
	            	Account account = accountService.selectByManagerId(manager.getId());
	            	String ip = getIpAddress(request);
	            	TixianCallback cb = new TixianCallback();
	            	cb.setPaylog(paylog);
	            	cb.setPaylogService(paylogService);
	            	cb.setManagerId(manager.getId());
	            	if(manager.getManagerUpId()<1)//如果没有父级代理，则所有的可提现的都被封死
	            		powerId = 5;
	            	cb.setTixianStatus(powerId);
	            	//
//	            	WithdrawCash.doTransfer(account.getOpenid(), manager.getName(), (float)total, "这是一次转账请求", ip, cb);
//	            	cb.onSuccess("", "", "");
	            	
	            	json.put("total", total);
	            	DecimalFormat    df   = new DecimalFormat("######0.00"); 
	            	json.put("info", "你的提现人民币"+df.format(total)+"元的请求已经发出！请留意你的微信转账记录！");
	            	System.out.println("返回的字符串==========="+json.toString());
	            	if(tixianDayLog.containsKey(managerId)&&tixianDayLog.get(managerId)==day){
	            		tixianDayLog.put(managerId, 0);
	            	}else{
	            		tixianDayLog.put(managerId, day);
	            	}
	            }else{
	            	json.put("error", "提现请求失败！");
	            	System.out.println("返回的字符串==========="+json.toString());
	            }
			}else{
				json.put("error", "提现失败!");
			}
//			}else{
//				json.put("error", "你可以提现的余额不足100!");
//			}
			returnMessage(response, json.toString());
		}
		}
	}
	
	@RequestMapping("/changeStatus")
	public void changeStatus(HttpServletRequest request, HttpServletResponse response){
		JSONObject json = new JSONObject();
		String paylogId = request.getParameter("paylogId");
		if(paylogId!=null&&!"".equals(paylogId)){//不为空
			Paylog paylog = new Paylog();
			paylog.setId(Integer.parseInt(paylogId));
			paylog.setStatus(1);
			paylogService.updateByPrimaryKey(paylog);
			returnMessage(response,json);
		}
	}
	

	@RequestMapping("/changeAccountStatus")
	public void changeAccountStatus(HttpServletRequest request, HttpServletResponse response){
		JSONObject json = new JSONObject();
		String accountId = request.getParameter("accountId");
		String accountStatus = request.getParameter("accountStatus");
		if(accountId!=null&&!"".equals(accountId)){//不为空
			Account account = new Account();
			account.setId(Integer.parseInt(accountId));
			account.setStatus(accountStatus);
			int result = accountService.updateStatus(account);
			for(Entry<Integer,Long> cacheentry:cacheTimestamp.entrySet()){
				long value = cacheentry.getValue();
				cacheentry.setValue(value-1000*60*5);
			}	
			json.put("result", result);
			returnMessage(response,json);
		}
	}
	
	
	/**
	 * 查询自己下面所有的用户
	 * @param request
	 * @param response
	 */
	@RequestMapping("/selectAllProxyByMyself")
	public void selectAllProxyByMyself(HttpServletRequest request, HttpServletResponse response){
		
	}
	
	/**
	 * 查询自己下面所有的充值记录
	 * @param request
	 * @param response
	 */
	@RequestMapping("/selectAllLogByMyself")
	public void selectAllLogByMyself(HttpServletRequest request, HttpServletResponse response){
		
	}
	/**
	 *超级管理员可以为所有用户充值房卡
	 *零售商只为玩家充值
	 *代理商只为零售商充值
	 * @param request
	 * @param response
	 */
	@RequestMapping("/updateAgentAccount")
	public void updateAgentAccount(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		if(session!=null){
			Manager manager = (Manager) session.getAttribute("manager");
			int managerId = manager.getId();
			int agentNum = Integer.parseInt(request.getParameter("num"));
			Account a = accountService.selectByManagerId(agentNum);
			int uuid = a.getUuid();
			String paycardnum = request.getParameter("payCardNum");
			String isAgent = request.getParameter("isAgent");
			JSONObject json = new JSONObject();
			Map<String,Integer> chargeAgent = new HashMap<String,Integer>();
			chargeAgent.put("managerid", agentNum);
			chargeAgent.put("roomcard", Integer.parseInt(paycardnum));
			
			Roomcardlog record = new Roomcardlog();
			record.setRoomcard(Integer.parseInt(paycardnum));
			record.setCreatetime(new java.util.Date());
			record.setManagerid(managerId);
			record.setAccountid(uuid);
			if(manager.getPowerId()==1){
				accountService.updateRoomCardByManagerId(chargeAgent);
				if(Integer.parseInt(paycardnum)!=0){
					roomcardlogService.insert(record);
				}
				json.put("status", "0");
				json.put("msg","操作成功！");
			}else{
				Account account = (Account) session.getAttribute("userAccount");
				Map<String,Integer> mapparamup = new HashMap<String,Integer>();
				int before = account.getRoomcard();
				mapparamup.put("id", account.getId());
				System.out.println("up之前的房卡："+before);
				mapparamup.put("roomcard", -Integer.parseInt(paycardnum));
				if(before-Integer.parseInt(paycardnum) >= 0){
					account.setRoomcard(before-Integer.parseInt(paycardnum));
					session.setAttribute("userAccount", account);
					accountService.updateRoomCard(mapparamup);
					json.put("roomcard", before-Integer.parseInt(paycardnum));
					accountService.updateRoomCardByManagerId(chargeAgent);
					if(Integer.parseInt(paycardnum)!=0){
						roomcardlogService.insert(record);
					}
					json.put("status", "0");
					json.put("msg", "操作成功！");
				}else{
					json.put("status", "1");
					json.put("msg", "传入参数有误");
				}
			}
				
			returnMessage(response,json);

		}
	}
	
	@RequestMapping("/updateAccount")
	public void updateAccount(HttpServletRequest request, HttpServletResponse response){
		int userid = Integer.parseInt(request.getParameter("userid"));
		String inviteCode = request.getParameter("inviteCode");
		String paycardnum = request.getParameter("payCardNum");
		System.out.println("1===="+userid);
		System.out.println("2===="+inviteCode);
		System.out.println("3===="+paycardnum);
		JSONObject json = new JSONObject();
		if(paycardnum==null&&inviteCode==null){
			System.out.println("传入参数有误");
			json.put("status", "1");
			json.put("msg", "传入参数有误");
		}
		if(StringUtil.isInteger(paycardnum, 0, 0)){
			int payCardNum = Integer.parseInt(paycardnum);
			HttpSession session = request.getSession();
			if(session != null){
				Manager manager = (Manager) session.getAttribute("manager");
				int managerId = manager.getId();
				Account account = accountService.selectByUuid(userid);
				Map<String,Integer> mapparam2 = new HashMap<String,Integer>();
				mapparam2.put("id", account.getId());
				mapparam2.put("roomcard", Integer.parseInt(paycardnum));
				Roomcardlog record = new Roomcardlog();
				record.setAccountid(userid);
				record.setRoomcard(payCardNum);
				record.setCreatetime(new java.util.Date());
				if(manager.getPowerId()==1){
					record.setManagerid(managerId);
					accountService.updateRoomCard(mapparam2);
					if(payCardNum!=0){
						roomcardlogService.insert(record);
					}
					if(userid != 0 ){
						//说明是为玩家充值
						//managerService.updateAccountRoomCard(userid, manager, payCardNum);
						
					}
					if(StringUtil.isInteger(inviteCode, 0, 0)){
						int ininviteCode = Integer.parseInt(inviteCode);
						//说明是为代理商或者经销商充值
						Manager bangding = managerService.selectManagerByInvoteCode(ininviteCode);
						if(bangding!=null){
							int managerUpId = bangding.getId();
							Map<String,Object> mapparam = new HashMap<String,Object>();
							Account account1 = accountService.selectByUuid(userid);
							mapparam.put("id", account1.getId());
							mapparam.put("managerUpId", managerUpId);
							accountService.updateAccountStatus(mapparam);
							json.put("bangding", 1);
						}
					}
					json.put("status", "0");
					json.put("msg", "操作成功！");
				}
				else{
					Account accountup = (Account) session.getAttribute("userAccount");
					Map<String,Integer> mapparamup = new HashMap<String,Integer>();
					int before = accountup.getRoomcard();
					mapparamup.put("id", accountup.getId());
					mapparamup.put("roomcard", -Integer.parseInt(paycardnum));
					if(before-Integer.parseInt(paycardnum) >= 0){
						record.setManagerid(managerId);
						accountup.setRoomcard(before-Integer.parseInt(paycardnum));
						session.setAttribute("userAccount", accountup);
						accountService.updateRoomCard(mapparamup);
						json.put("roomcard", before-Integer.parseInt(paycardnum));
						accountService.updateRoomCard(mapparam2);
						if(payCardNum!=0){
							roomcardlogService.insert(record);
						}
						json.put("status", "0");
						json.put("msg", "操作成功！");
					}else{
						json.put("status", "1");
						json.put("msg", "传入参数有误");
					}
				}
			}
			else{
				System.out.println("未建立链接就进行充值的非法操作");
				json.put("status", "1");
				json.put("msg", "未建立链接就进行充值的非法操作");	
			}
		}
		returnMessage(response,json);
	}
	
	/**
	 * 为代理商或者零售商用户充值房卡
	 * @param request
	 * @param response
	 */
	/*@RequestMapping("/addActualCardToProxy")
	public void addActualCardToProxy(HttpServletRequest request, HttpServletResponse response){
		int id = Integer.parseInt(request.getParameter("id"));
		int addCardNum = Integer.parseInt(request.getParameter("addCardNum"));
	}*/
	/**
	 * 设置用户状态
	 * @param request
	 * @param response
	 */
	/*@RequestMapping("/setAccountStatus")
	public void setAccountStatus(HttpServletRequest request, HttpServletResponse response){
		int uuid = Integer.parseInt(request.getParameter("uuid"));
		int status = Integer.parseInt(request.getParameter("status"));
	}*/
	
	/**
	 * 删除代理商用户
	 * @param request
	 * @param response
	 */
	@RequestMapping("/deleteProxyAccount")
	public void deleteProxyAccount(HttpServletRequest request, HttpServletResponse response){
		int id = Integer.parseInt(request.getParameter("id"));
		int result = managerService.deleteByPrimaryKey(id);
		if(result>0)
		managerService.updateByPrimaryKeyUpdateChild(id);
		JSONObject json = new JSONObject();
		if(result > 0){
			for(Entry<Integer,Long> cacheentry:cacheTimestamp.entrySet()){
				long value = cacheentry.getValue();
				cacheentry.setValue(value-1000*60*5);
			}	
			
			json.put("mess", "删除代理商用户成功");
			System.out.println("删除代理商用户成功");
		}else{
			json.put("mess", "删除代理商用户失败");
		}
		returnMessage(response,json);
	}
	
	
	
	
	@RequestMapping("/validUuid")
	public void validUuid(HttpServletRequest request, HttpServletResponse response){
		JSONObject json = new JSONObject();
		String code = request.getParameter("uuid");
		if(code!=null&&!"".equals(code)){//不为空
			int inviteCode = Integer.parseInt(code);
			Account result = accountService.selectByUuid(inviteCode);
			if(result != null){
				json.put("valid", true);
			}else{
				json.put("valid", false);
			}
			returnMessage(response,json);
		}
	}
	
	
	@RequestMapping("/inviteCodeValid")
	public void inviteCodeValid(HttpServletRequest request, HttpServletResponse response){
		JSONObject json = new JSONObject();
		String code = request.getParameter("inviteCode");
		String mid = request.getParameter("id");
		int id = 0;
		if(mid!=null&&!"".equals(mid))
			id = Integer.parseInt(mid);
		if(code!=null&&!"".equals(code)){//不为空
		int inviteCode = Integer.parseInt(code);
		Manager result = managerService.selectManagerByInvoteCode(inviteCode);
		
		
		if(result !=null&&result.getId()!=id){
			json.put("valid", false);
		}else{
			json.put("valid", true);
		}
		}
		else{
			json.put("valid", true);
		}
		returnMessage(response,json);
	}
	
	@RequestMapping("/inviteCodeValidAgentManager")
	public void inviteCodeValidAgentManager(HttpServletRequest request, HttpServletResponse response){
		JSONObject json = new JSONObject();
		String code = request.getParameter("inviteCode");
		if(code!=null&&!"".equals(code)){//不为空
			int inviteCode = Integer.parseInt(code);
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("inviteCode",inviteCode);
			Manager result = managerService.selectManagerByInviteCode(map);
			if(result !=null){
				json.put("valid", false);
			}else{
				json.put("valid", true);
			}
		}
		returnMessage(response,json);
	}
	
	@RequestMapping("/getParentInvite")
	public void getParentInvite(HttpServletRequest request, HttpServletResponse response){
		JSONObject json = new JSONObject();
		String iuuid = request.getParameter("uuid");
		Manager result = null;
		int uuid = 0;
		if(iuuid!=null&&!"".equals(uuid))
			uuid = Integer.parseInt(iuuid);
		if(uuid>10000){//不为空
			Account account = accountService.selectByUuid(uuid);
			Integer managerUpId = account.getManagerUpId();
			if(managerUpId!=null&&managerUpId.intValue()>0)
				result = managerService.selectByPrimaryKey(managerUpId.intValue());
		
		if(result !=null){
			json.put("inviteCode", result.getInviteCode());
			json.put("powerId", result.getPowerId());
		}else{
			json.put("inviteCode", "");
		}
		}
		else{
			json.put("inviteCode", "");
		}
		returnMessage(response,json);
	}
	
	/**
	 * 获取所有奖品信息
	 * @param request
	 * @param response
	 */
	@RequestMapping("/prizesInfo")
	public void prizesInfo(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		JSONArray array = new JSONArray();
		if(session != null){
			Manager manager = (Manager) session.getAttribute("manager");
			if(manager != null){
				try {
					array = managerService.getPrizesInfo();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		returnMessage(response,array);
	}	
	
	/**
	 * 抽奖状态信息，
	 * @param request
	 * @param response
	 */
	@RequestMapping("/prizesStatus")
	public void prizesStatus(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		String type = request.getParameter("type");//0 表示得到状态  1表示修改状态
		if(session != null){
			Manager manager = (Manager) session.getAttribute("manager");
			if(manager != null){
				PrizeRule prizeRule = prizeRuleService.selectByPrimaryKey(1);
				if(type.equals("0")){
					String status  = prizeRule.getStatus();
					json.put("status", status);
				}
				else if(type.equals("1")){
					String status = request.getParameter("status");
					prizeRule.setStatus(status);
					try {
						int i = prizeRuleService.updateByPrimaryKey(prizeRule);
						if(i > 0){
							json.put("status", "0");
						}
						else{
							json.put("status", "1");
							json.put("error", "开启/关闭失败");
						}
					} catch (Exception e) {
						e.printStackTrace();
						json.put("status", "修改状态异常");
					}
				}
			}
		}
		returnMessage(response,json);
	}
	
	/**
	 * 修改单个奖品信息概率，等信息
	 * @param request
	 * @param response
	 */
	@RequestMapping("/updatePrizeInfo")
	public void updatePrizeInfo(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		String probability = request.getParameter("probability");
		String oneName = request.getParameter("oneName");
		String afterUpImg = request.getParameter("afterUpImg");
		String count = request.getParameter("count");
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		JSONObject json = new JSONObject();
		if(session != null){
			Manager manager = (Manager) session.getAttribute("manager");
			if(manager != null && StringUtil.isInteger(count, 0, 0)){
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("id",Integer.parseInt(id));
				map.put("probability", Integer.parseInt(probability));
				map.put("prizeName", oneName);
				map.put("prizecount", Integer.parseInt(count));
				map.put("imageUrl", afterUpImg);
				map.put("type", type);
				try {
					json = managerService.updatePrizeInfo(map);
				} catch (Exception e) {
					e.printStackTrace();
					json.put("status_code", "1");
					json.put("error", "异常情况");
				}
			}
			else{
				json.put("status_code", "1");
				json.put("error", "请传入正确参数");
			}
		}
		returnMessage(response,json);
	}	
	

	/**
	 * 发送公告，通知游戏后台，发送公告
	 * @param request
	 * @param response
	 */
	@RequestMapping("/sendNotice")
	public void sendNotice(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		if(session != null){
			Manager manager = (Manager) session.getAttribute("manager");
			if(manager != null){
				String notice = request.getParameter("notice");
				String type = request.getParameter("type");
				String inviteCode = request.getParameter("inviteCode");
				if(notice!=null&&!"".equals(notice)&&type!=null&&!"".equals(type)){
				try {
					NoticeTable noticeTable = new NoticeTable();
					int noticeType = Integer.parseInt(type);
					noticeTable.setType(noticeType);
					noticeTable.setContent(notice);
					if(inviteCode!=null&&!"".equals(inviteCode)){
						Manager managerInput = managerService.selectManagerByInvoteCode(Integer.parseInt(inviteCode));
						if(managerInput!=null)
						noticeTable.setManagerId(managerInput.getId());
					}
					noticeTableService.insertSelective(noticeTable);
					json.put("status_code", "0");
			        json.put("info", "保存成功");
				} catch (Exception e) {
					e.printStackTrace();
					json.put("status_code", "1");
			        json.put("error", "异常情况");
				}
				}
			}
		}
		returnMessage(response,json);
	}
	/**
	 * 修改奖品规则
	 * @param request
	 * @param response
	 */
	@RequestMapping("/updatePrizeRule")
	public void updatePrizeRule(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		String prizeRule = request.getParameter("prizeRule");
		String prizePerDay = request.getParameter("prizePerDay");
		if(session != null){
			Manager manager = (Manager) session.getAttribute("manager");
			if(manager != null && manager.getPowerId() == 1){
				if(StringUtil.isNotEmpty(prizeRule) && StringUtil.isInteger(prizePerDay, 100, 0)){
					PrizeRule prizerule = new PrizeRule();
					prizerule.setId(1);
					prizerule.setContent(prizeRule);
					prizerule.setPrecount(Integer.parseInt(prizePerDay));
					try {
						prizeRuleService.updateByPrimaryKeySelective(prizerule);
						json.put("status_code", "0");
					} catch (Exception e) {
						e.printStackTrace();
						json.put("status_code", "1");
						json.put("error", "信息修改异常");
					}
				}
				else{
					json.put("status_code", "1");
					json.put("error", "抽奖次数和规则不能为空");
				}
			}
			else{
				json.put("status_code", "1");
				json.put("error", "只有超级管理员才能发布信息");
			}
		}
		returnMessage(response,json);
	}
	
	/**
	 * 修改充卡联系信息
	 * @param request
	 * @param response
	 */
	@RequestMapping("/updateContactway ")
	public void updateContactway(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		String newContact = request.getParameter("newContact");
		System.out.println(newContact);
		if(session != null){
			Manager manager = (Manager) session.getAttribute("manager");
			if(manager != null && manager.getPowerId() == 1){
				if(StringUtil.isNotEmpty(newContact)){
					ContactWay contactWay  = new ContactWay();
					contactWay.setId(1);
					contactWay.setContent(newContact);
					try {
						contactWayService.updateByPrimaryKeySelective(contactWay);
						json.put("status_code", "0");
					} catch (Exception e) {
						e.printStackTrace();
						json.put("status_code", "1");
						json.put("error", "修改出现异常");
					}
				}
				else{
					json.put("status_code", "1");
					json.put("error", "保存信息不能为空");
				}
			}
			else{
				System.out.println("只有超级管理员才能修改");
			}
		}
		returnMessage(response,json);
	}
	
	/**
	 *只能根据准确的uuid搜索到玩家，
	 * @param request
	 * @param response
	 */
	@RequestMapping("/searchAccountByUuid ")
	public void searchAccountByUuid(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		String accountUuid = request.getParameter("accountUuid");
		if(session != null){
			Manager manager = (Manager) session.getAttribute("manager");
			if(manager != null){
				if(StringUtil.isNotEmpty(accountUuid) && StringUtil.isInteger(accountUuid, 0, 0)){
					try {
						int managerUpId = 0;
						Account account = accountService.selectByUuid(Integer.parseInt(accountUuid));
						if(account!=null&&account.getManagerUpId()!=null&&account.getManagerUpId()>1){
						managerUpId = account.getManagerUpId();
						Manager up = managerService.selectByPrimaryKey(managerUpId);
						account.setManagerUpId(up.getInviteCode());
						}
						if(manager.getPowerId()>1&&managerUpId!=manager.getId().intValue()){//不为超级管理员且不是自己下面的用户
							account = null;
						}
						if(account != null){
							json.put("status_code", "0");
							json.put("data", account);
							json.put("type", manager.getPowerId());
						}
						else{
							json.put("status_code", "1");
							json.put("error", "用户不存在");
						}
					} catch (Exception e) {
						e.printStackTrace();
						json.put("status_code", "1");
						json.put("error", "异常情况");
					}
				}
				else{
					json.put("status_code", "1");
					json.put("error", "错误的玩家id");
				}
			}
			else{
				System.out.println("管理员未登录，请登录");
			}
		}
		returnMessage(response,json);
	}
	/**
	 *只能根据准确的手机号码搜索到代理商
	 * @param request
	 * @param response
	 */
	@RequestMapping("/searchManagerByTel ")
	public void searchManagerByTel(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		JSONArray array = new JSONArray();
		String managerTel = request.getParameter("managerTel");
		if(session != null){
			Manager manager = (Manager) session.getAttribute("manager");
			if(manager != null){
				if(StringUtil.isNotEmpty(managerTel) && StringUtil.isPhone(managerTel)){
					try {
						Map<String,Object> map = new HashMap<String, Object>();
						map.put("telephone", managerTel);
						if(manager.getId() != 1){
							map.put("managerUpId", manager.getId());
						}
						List<Manager> searchManager = managerService.selectManagerByTel(map);
						if(!searchManager.isEmpty()){
							for (Manager m : searchManager) {
								array.add(m);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		returnMessage(response,array);
	}
	
	/**
	 *超管给代理商减房卡
	 * @param request
	 * @param response
	 */
	@RequestMapping("/reduceManagerRoomCard ")
	public void reduceManagerRoomCard(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		String reducedManagerId = request.getParameter("managerId");
		String cardNumb = request.getParameter("cardNumb");
		if(session != null){
			Manager manager = (Manager) session.getAttribute("manager");
			if(manager != null && manager.getId() == 1){
				if(StringUtil.isNotEmpty(reducedManagerId) && StringUtil.isNotEmpty(cardNumb)
						&& StringUtil.isInteger(cardNumb, 10000, 0) && StringUtil.isInteger(reducedManagerId, 0, 0) ){
					try {
						//判断代理商的房卡是否够减
						int reducedmanagerId = Integer.parseInt(reducedManagerId);
						Manager  mana = managerService.selectByPrimaryKey(reducedmanagerId);
						int cardnumb  =  0 - Integer.parseInt(cardNumb);
						int actualCard = mana.getActualcard();
						if(actualCard >= Math.abs(cardnumb)){
							json = managerService.updateManagerRoomCard(reducedmanagerId, manager, cardnumb);
							Manager searchManager = managerService.selectByPrimaryKey(reducedmanagerId);
							json.put("data", searchManager);
						}
						else{
							json.put("status", "1");
							json.put("error", "该代理商房卡不够减");
						}
					} catch (Exception e) {
						e.printStackTrace();
						json.put("status", "1");
						json.put("error", "异常情况");
					}
				}
				else{
					json.put("status", "1");
					json.put("error", "传入参数有误");
				}
			}
			else{
				json.put("status", "1");
				json.put("error", "只有超管才能减房卡");
			}
		}
		returnMessage(response,json);
	}
	
	/**
	 *超管给玩家减房卡
	 * @param request
	 * @param response
	 */
	@RequestMapping("/reduceAccountRoomCard ")
	public void reduceAccountRoomCard(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		String reducedAccountId = request.getParameter("accountId");
		String cardNumb = request.getParameter("cardNumb");
		if(session != null){
			Manager manager = (Manager) session.getAttribute("manager");
			if(manager != null && manager.getId() == 1){
				if(StringUtil.isNotEmpty(reducedAccountId) && StringUtil.isNotEmpty(cardNumb)
						&& StringUtil.isInteger(cardNumb, 10000, 0) && StringUtil.isInteger(reducedAccountId, 0, 0) ){
					try {
						//判断玩家的房卡是否够减
						int reducedaccountId = Integer.parseInt(reducedAccountId);
						Account  acc = accountService.selectByPrimaryKey(reducedaccountId);
						int cardnumb  =  0 - Integer.parseInt(cardNumb);
						int roomCard = acc.getRoomcard();
						if(roomCard >= Math.abs(cardnumb)){
							json = managerService.updateAccountRoomCard(reducedaccountId, manager, cardnumb);
							if(json.get("status").equals("0")){
								acc.setTotalcard(acc.getTotalcard()+cardnumb);
								acc.setRoomcard(acc.getRoomcard() + cardnumb);
								json.put("data", acc);
							}
						}
						else{
							json.put("status", "1");
							json.put("error", "该玩家房卡不够减");
						}
					} catch (Exception e) {
						e.printStackTrace();
						json.put("status", "1");
						json.put("error", "异常情况");
					}
				}
				else{
					json.put("status", "1");
					json.put("error", "传入参数有误");
				}
			}
			else{
				json.put("status", "1");
				json.put("error", "只有超管才能减房卡");
			}
		}
		returnMessage(response,json);
	}
	
	/**
	 *修改玩家状态(删除玩家)
	 * @param request
	 * @param response
	 */
	@RequestMapping("/updateAccountStatus ")
	public void updateAccountStatus(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		String id = request.getParameter("accountId");
		String status = request.getParameter("status");
		if(session != null){
			Manager manager = (Manager) session.getAttribute("manager");
			if(manager != null && manager.getId() == 1){
				if(StringUtil.isNotEmpty(id) && StringUtil.isNotEmpty(status)){
					try {
						Map<String,Object> map = new HashMap<String, Object>();
						map.put("id", id);
						map.put("status", status);
						
						int i = accountService.updateAccountStatus(map);
						if(i >= 1){
							json.put("status", "0");
						}
						else{
							json.put("status", "1");
							json.put("error", "信息修改有误");
						}
					} catch (Exception e) {
						e.printStackTrace();
						json.put("status", "1");
						json.put("error", "信息修改有误");
					}
				}
				else{
					json.put("status", "1");
					json.put("error", "传入参数有误");
				}
			}
			else{
				json.put("status", "1");
				json.put("error", "只有超管才能删除玩家");
			}
		}
		returnMessage(response,json);
	}
	
	/**
	 *超管给玩家减房卡
	 * @param request
	 * @param response
	 */
	@RequestMapping("/updateManagerStatus ")
	public void updateManagerStatus(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		String manangerId = request.getParameter("manangerId");
		if(session != null){
			Manager manager = (Manager) session.getAttribute("manager");
			if(manager != null){
				if(StringUtil.isNotEmpty(manangerId)){
					int managerid = Integer.parseInt(manangerId);
					try {
						Manager updatedManager = managerService.selectByPrimaryKey(managerid);
						if(manager.getId() == 1 || updatedManager.getManagerUpId() == manager.getId()){
							//超级管理员不用判断被删除的代理是否在其下面
							int i = managerService.updateManagerStatus(updatedManager,manager.getId());
							//管理员操作日志记录
							
							if(i >= 1){
								json.put("status", "0");
							}
							else{
								json.put("status", "1");
								json.put("error", "删除失败");
							}
						}
						else{
							json.put("status", "1");
							json.put("error", "自己删除自己下面的代理商");
						}
					} catch (Exception e) {
						e.printStackTrace();
						json.put("status", "1");
						json.put("error", "信息修改出现异常");
					}
				}
				else{
					json.put("status", "1");
					json.put("error", "传入参数有误");
				}
			}
			else{
				json.put("status", "1");
				json.put("error", "请先登录");
			}
		}
		returnMessage(response,json);
	}
	
	
	/**
	 *后台管理员获取自己的房卡流水信息
	 * @param request
	 * @param response
	 */
	@RequestMapping("/roomCardWaterCourse ")
	public void roomCardWaterCourse(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		JSONArray array = new JSONArray();
		if(session != null){
			Manager manager = (Manager) session.getAttribute("manager");
			if(manager != null){
				array  = managerService.roomCardWaterCourse(manager.getId());
			}
		}
		returnMessage(response,array);
	}
	
	/**
	 *后台管理员获取自己的操作流水信息
	 * @param request
	 * @param response
	 */
	@RequestMapping("/operationWaterCourse ")
	public void operationWaterCourse(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		JSONArray array = new JSONArray();
		if(session != null){
			Manager manager = (Manager) session.getAttribute("manager");
			if(manager != null){
				array = managerService.operationWaterCourse(manager.getId());
			}
		}
		returnMessage(response,array);
	}
	
	
	
	
	public final static String getIpAddress(HttpServletRequest request){  
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址  
  
        String ip = request.getHeader("X-Forwarded-For");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("Proxy-Client-IP");  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("WL-Proxy-Client-IP");  

            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("HTTP_CLIENT_IP");  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getRemoteAddr();  
            }  
        } else if (ip.length() > 15) {  
            String[] ips = ip.split(",");  
            for (int index = 0; index < ips.length; index++) {  
                String strIp = (String) ips[index];  
                if (!("unknown".equalsIgnoreCase(strIp))) {  
                    ip = strIp;  
                    break;  
                }  
            }  
        }  
        return ip;  
    }
	
	
	@RequestMapping("/selectManagerByInviteCode ")
	public void selectManagerByInviteCode(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		String uuid = request.getParameter("uuid");
		String inviteCode = request.getParameter("inviteCode");
		String name = request.getParameter("name");
		if(session != null){
			Manager manager = (Manager) session.getAttribute("manager");
			if(manager != null){
				if(manager.getPowerId()==1||manager.getPowerId()==5){
					Map<String,Object> map = new HashMap<String, Object>();
					if(StringUtil.isInteger(uuid, 0, 0) ){
						map.put("uuid", Integer.parseInt(uuid));
					}
					if(StringUtil.isInteger(inviteCode, 0, 0) ){
						map.put("inviteCode", Integer.parseInt(inviteCode));
					}
					if(StringUtil.isNotEmpty(name) ){
						map.put("name", name);
					}
					Manager res = managerService.selectManagerByInviteCode(map);
					if(res!=null){
						json.put("manager", res);
						json.put("status", 1);
					}
				}
			}
		}
		returnMessage(response,json);
	}
	
	
	@RequestMapping("/getParentInviteById ")
	public void getParentInviteById(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		String id = request.getParameter("id");
		if(session != null){
			Manager manager = (Manager) session.getAttribute("manager");
			if(manager != null){
				
					Map<String,Object> map = new HashMap<String, Object>();
					if(StringUtil.isInteger(id, 0, 0) ){
						Manager res = managerService.selectByPrimaryKey(Integer.parseInt(id));
						if(res!=null){
							json.put("inviteCode", res.getInviteCode());
							json.put("powerId", res.getPowerId());
							json.put("status", 1);
						}
					}
			}
		}
		returnMessage(response,json);
	}
	
	
	/**
	 * 退出登录
	 * @param request
	 * @param response
	 */
	@RequestMapping("/logout ")
	public void logout(HttpServletRequest request, HttpServletResponse response){
		
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		if(session != null){
			session.invalidate();
		}
		
	}
}
