package com.weipai.service;

import java.sql.Date;
import java.util.List;
import com.weipai.model.PaylogLot;
import com.weipai.model.PayloglotExt;

public interface PaylogLotService {
	//查询所有，本周，上周，本月，上月的充值数据
	public List<PaylogLot> selectByManagerId(int managerId,int searchType,int payType,int start,int limit);
	
	public List<PayloglotExt> selectByMidTime(int managerId, int searchType, int start, int limit,Date startTime,Date endTime);
	
	
	public List<PaylogLot> selectUserPaySum(int managerId, int searchType, int start, int limit,Date startTime,Date endTime);
	
	public int selectUserPaySumCount(int managerId, int searchType, int start, int limit,Date startTime,Date endTime);
	
	public int selectByMidTimeCount(int managerId, int searchType, int start, int limit,Date startTime,Date endTime);
	
	public List<PayloglotExt> selectByAllTime(int searchType, int start, int limit,Date startTime,Date endTime);
	
	public int selectByAllTimeCount(int searchType, int start, int limit,Date startTime,Date endTime);
	//统计总的数据，分页之用
	public int selectSumByManagerId(int managerId,int searchType,int payType);
	//汇总充值数据
	public int sumByManagerId(int managerId,int sumType);
	//按自定义时间汇总充值数据
	public int sumByTime(int managerId,Date start,Date end);
	
	
	//汇总下面第二级充值数据
	public int sumSubByManagerId(int managerId,int sumType,int powerId);
	//按自定义时间汇总下面第二级充值数据
	public int sumSubByTime(int managerId,Date start,Date end);
		
	//发起提现
//	public int insertPaylogLot(PaylogLot paylogLot);
//	public void tixianUpdate(Integer managerId,int status);
//	public void tixianDone(Integer id,int status);
	
	
}
