package com.weipai.service.impl;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.weipai.mapper.PaylogMapper;
import com.weipai.model.Paylog;
import com.weipai.model.PaylogExt;
import com.weipai.service.PaylogService;
import com.weipai.utils.DateUtil;
@Service
@Transactional(propagation=Propagation.REQUIRED,rollbackFor=RuntimeException.class)
public class PaylogServiceImpl implements PaylogService{
	@Resource
	PaylogMapper paylogMapper;
	@Override
	public List<Paylog> selectByManagerId(int managerId, int searchType,int payType, int start, int limit) {
		//0是查询全部1是查本周2是查上周3是查本月4是查上月
		Date startTime = null;
		Date endTime = null;
		System.out.println("searchType============="+searchType);
		System.out.println("payType============="+payType);
		if(searchType==0){//查询全部
			startTime = new Date(0L);
			endTime = new Date((new java.util.Date()).getTime()+86400000);
			System.out.println(startTime.toLocaleString());
			System.out.println(endTime.toLocaleString());
		}else if(searchType==1){//本周
			startTime = new Date(DateUtil.getTimesWeekmorning().getTime());
			endTime = new Date(DateUtil.getTimesWeeknight().getTime());
			System.out.println(startTime.toLocaleString());
			System.out.println(endTime.toLocaleString());
		}else if(searchType==2){//上周
			startTime = new Date(DateUtil.getTimesLastWeekmorning().getTime());
			endTime = new Date(DateUtil.getTimesLastWeeknight().getTime());
			System.out.println(startTime.toLocaleString());
			System.out.println(endTime.toLocaleString());
		}else if(searchType==3){//本月
			startTime = new Date(DateUtil.getTimesMonthmorning().getTime());
			endTime = new Date(DateUtil.getTimesMonthnight().getTime());
			System.out.println(startTime.toLocaleString());
			System.out.println(endTime.toLocaleString());
		}else if(searchType==4){//上月
			startTime = new Date(DateUtil.getTimesLastMonthmorning().getTime());
			endTime = new Date(DateUtil.getTimesLastMonthnight().getTime());
			System.out.println(startTime.toLocaleString());
			System.out.println(endTime.toLocaleString());
		}else if(searchType==5){//今天
			startTime = new Date(DateUtil.getTodayStart().getTime());
			endTime = new Date(DateUtil.getTodayEnd().getTime());
		}else if(searchType==6){//昨天
			startTime = new Date(DateUtil.getYesterdayStart().getTime());
			endTime = new Date(DateUtil.getYesterdayEnd().getTime());
		}
		Map<String,Object> params = new HashMap<String,Object>();
		if(managerId>2)
		params.put("managerId", managerId);
		params.put("startNum", start);
		params.put("payType", payType);
		params.put("pageNumber", limit);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		return paylogMapper.selectByManagerId(params);
	}
	
	
	public List<PaylogExt> selectByMidTime(int managerId, int searchType, int start, int limit,Date startTime,Date endTime) {
		//0是查询全部1是查本周2是查上周3是查本月4是查上月
		System.out.println("searchType============="+searchType);
		if(startTime==null&&endTime==null){
		if(searchType==0){//查询全部
			startTime = new Date(0L);
			endTime = new Date((new java.util.Date()).getTime()+86400000);
			System.out.println(startTime.toLocaleString());
			System.out.println(endTime.toLocaleString());
		}else if(searchType==1){//本周
			startTime = new Date(DateUtil.getTimesWeekmorning().getTime());
			endTime = new Date(DateUtil.getTimesWeeknight().getTime());
			System.out.println(startTime.toLocaleString());
			System.out.println(endTime.toLocaleString());
		}else if(searchType==2){//上周
			startTime = new Date(DateUtil.getTimesLastWeekmorning().getTime());
			endTime = new Date(DateUtil.getTimesLastWeeknight().getTime());
			System.out.println(startTime.toLocaleString());
			System.out.println(endTime.toLocaleString());
		}else if(searchType==3){//本月
			startTime = new Date(DateUtil.getTimesMonthmorning().getTime());
			endTime = new Date(DateUtil.getTimesMonthnight().getTime());
			System.out.println(startTime.toLocaleString());
			System.out.println(endTime.toLocaleString());
		}else if(searchType==4){//上月
			startTime = new Date(DateUtil.getTimesLastMonthmorning().getTime());
			endTime = new Date(DateUtil.getTimesLastMonthnight().getTime());
			System.out.println(startTime.toLocaleString());
			System.out.println(endTime.toLocaleString());
		}else if(searchType==5){//今天
			startTime = new Date(DateUtil.getTodayStart().getTime());
			endTime = new Date(DateUtil.getTodayEnd().getTime());
		}else if(searchType==6){//昨天
			startTime = new Date(DateUtil.getYesterdayStart().getTime());
			endTime = new Date(DateUtil.getYesterdayEnd().getTime());
		}
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("managerId", managerId);
		params.put("startNum", start);
		params.put("pageNumber", limit);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		return paylogMapper.selectByMidTime(params);
	}
	
	
	public int selectByMidTimeCount(int managerId, int searchType, int start, int limit,Date startTime,Date endTime) {
		//0是查询全部1是查本周2是查上周3是查本月4是查上月
		System.out.println("searchType============="+searchType);
		if(startTime==null&&endTime==null){
		if(searchType==0){//查询全部
			startTime = new Date(0L);
			endTime = new Date((new java.util.Date()).getTime()+86400000);
			System.out.println(startTime.toLocaleString());
			System.out.println(endTime.toLocaleString());
		}else if(searchType==1){//本周
			startTime = new Date(DateUtil.getTimesWeekmorning().getTime());
			endTime = new Date(DateUtil.getTimesWeeknight().getTime());
			System.out.println(startTime.toLocaleString());
			System.out.println(endTime.toLocaleString());
		}else if(searchType==2){//上周
			startTime = new Date(DateUtil.getTimesLastWeekmorning().getTime());
			endTime = new Date(DateUtil.getTimesLastWeeknight().getTime());
			System.out.println(startTime.toLocaleString());
			System.out.println(endTime.toLocaleString());
		}else if(searchType==3){//本月
			startTime = new Date(DateUtil.getTimesMonthmorning().getTime());
			endTime = new Date(DateUtil.getTimesMonthnight().getTime());
			System.out.println(startTime.toLocaleString());
			System.out.println(endTime.toLocaleString());
		}else if(searchType==4){//上月
			startTime = new Date(DateUtil.getTimesLastMonthmorning().getTime());
			endTime = new Date(DateUtil.getTimesLastMonthnight().getTime());
			System.out.println(startTime.toLocaleString());
			System.out.println(endTime.toLocaleString());
		}else if(searchType==5){//今天
			startTime = new Date(DateUtil.getTodayStart().getTime());
			endTime = new Date(DateUtil.getTodayEnd().getTime());
		}else if(searchType==6){//昨天
			startTime = new Date(DateUtil.getYesterdayStart().getTime());
			endTime = new Date(DateUtil.getYesterdayEnd().getTime());
		}
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("managerId", managerId);
		params.put("startNum", start);
		params.put("pageNumber", limit);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		return paylogMapper.selectByMidTimeCount(params);
	}
	
	
	public List<PaylogExt> selectByAllTime( int searchType, int start, int limit,Date startTime,Date endTime) {
		//0是查询全部1是查本周2是查上周3是查本月4是查上月
		System.out.println("searchType============="+searchType);
		if(startTime==null&&endTime==null){
		if(searchType==0){//查询全部
			startTime = new Date(0L);
			endTime = new Date((new java.util.Date()).getTime()+86400000);
			System.out.println(startTime.toLocaleString());
			System.out.println(endTime.toLocaleString());
		}else if(searchType==1){//本周
			startTime = new Date(DateUtil.getTimesWeekmorning().getTime());
			endTime = new Date(DateUtil.getTimesWeeknight().getTime());
			System.out.println(startTime.toLocaleString());
			System.out.println(endTime.toLocaleString());
		}else if(searchType==2){//上周
			startTime = new Date(DateUtil.getTimesLastWeekmorning().getTime());
			endTime = new Date(DateUtil.getTimesLastWeeknight().getTime());
			System.out.println(startTime.toLocaleString());
			System.out.println(endTime.toLocaleString());
		}else if(searchType==3){//本月
			startTime = new Date(DateUtil.getTimesMonthmorning().getTime());
			endTime = new Date(DateUtil.getTimesMonthnight().getTime());
			System.out.println(startTime.toLocaleString());
			System.out.println(endTime.toLocaleString());
		}else if(searchType==4){//上月
			startTime = new Date(DateUtil.getTimesLastMonthmorning().getTime());
			endTime = new Date(DateUtil.getTimesLastMonthnight().getTime());
			System.out.println(startTime.toLocaleString());
			System.out.println(endTime.toLocaleString());
		}else if(searchType==5){//今天
			startTime = new Date(DateUtil.getTodayStart().getTime());
			endTime = new Date(DateUtil.getTodayEnd().getTime());
		}else if(searchType==6){//昨天
			startTime = new Date(DateUtil.getYesterdayStart().getTime());
			endTime = new Date(DateUtil.getYesterdayEnd().getTime());
		}
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startNum", start);
		params.put("pageNumber", limit);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		return paylogMapper.selectByAllTime(params);
	}
	
	
	public int selectByAllTimeCount(int searchType, int start, int limit,Date startTime,Date endTime) {
		//0是查询全部1是查本周2是查上周3是查本月4是查上月
		System.out.println("searchType============="+searchType);
		if(startTime==null&&endTime==null){
		if(searchType==0){//查询全部
			startTime = new Date(0L);
			endTime = new Date((new java.util.Date()).getTime()+86400000);
			System.out.println(startTime.toLocaleString());
			System.out.println(endTime.toLocaleString());
		}else if(searchType==1){//本周
			startTime = new Date(DateUtil.getTimesWeekmorning().getTime());
			endTime = new Date(DateUtil.getTimesWeeknight().getTime());
			System.out.println(startTime.toLocaleString());
			System.out.println(endTime.toLocaleString());
		}else if(searchType==2){//上周
			startTime = new Date(DateUtil.getTimesLastWeekmorning().getTime());
			endTime = new Date(DateUtil.getTimesLastWeeknight().getTime());
			System.out.println(startTime.toLocaleString());
			System.out.println(endTime.toLocaleString());
		}else if(searchType==3){//本月
			startTime = new Date(DateUtil.getTimesMonthmorning().getTime());
			endTime = new Date(DateUtil.getTimesMonthnight().getTime());
			System.out.println(startTime.toLocaleString());
			System.out.println(endTime.toLocaleString());
		}else if(searchType==4){//上月
			startTime = new Date(DateUtil.getTimesLastMonthmorning().getTime());
			endTime = new Date(DateUtil.getTimesLastMonthnight().getTime());
			System.out.println(startTime.toLocaleString());
			System.out.println(endTime.toLocaleString());
		}else if(searchType==5){//今天
			startTime = new Date(DateUtil.getTodayStart().getTime());
			endTime = new Date(DateUtil.getTodayEnd().getTime());
		}else if(searchType==6){//昨天
			startTime = new Date(DateUtil.getYesterdayStart().getTime());
			endTime = new Date(DateUtil.getYesterdayEnd().getTime());
		}
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startNum", start);
		params.put("pageNumber", limit);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		return paylogMapper.selectByAllTimeCount(params);
	}

	@Override
	public int selectSumByManagerId(int managerId, int searchType,int payType) {
		//0是查询全部1是查本周2是查上周3是查本月4是查上月
				Date startTime = null;
				Date endTime = null;
				if(searchType==0){//查询全部
					startTime = new Date(0L);
					endTime = new Date((new java.util.Date()).getTime()+86400000);
				}else if(searchType==1){//本周
					startTime = new Date(DateUtil.getTimesWeekmorning().getTime());
					endTime = new Date(DateUtil.getTimesWeeknight().getTime());
				}else if(searchType==2){//上周
					startTime = new Date(DateUtil.getTimesLastWeekmorning().getTime());
					endTime = new Date(DateUtil.getTimesLastWeeknight().getTime());
				}else if(searchType==3){//本月
					startTime = new Date(DateUtil.getTimesMonthmorning().getTime());
					endTime = new Date(DateUtil.getTimesMonthnight().getTime());
				}else if(searchType==4){//上月
					startTime = new Date(DateUtil.getTimesLastMonthmorning().getTime());
					endTime = new Date(DateUtil.getTimesLastMonthnight().getTime());
				}else if(searchType==5){//今天
					startTime = new Date(DateUtil.getTodayStart().getTime());
					endTime = new Date(DateUtil.getTodayEnd().getTime());
				}else if(searchType==6){//昨天
					startTime = new Date(DateUtil.getYesterdayStart().getTime());
					endTime = new Date(DateUtil.getYesterdayEnd().getTime());
				}
				Map<String,Object> params = new HashMap<String,Object>();
				if(managerId>2)
				params.put("managerId", managerId);
				params.put("payType", payType);
				params.put("startTime", startTime);
				params.put("endTime", endTime);
				return paylogMapper.selectSumByManagerId(params);
	}

	@Override
	public int sumByManagerId(int managerId, int sumType) {
		//0是查询全部1是查本周2是查上周3是查本月4是查上月
		Date startTime = null;
		Date endTime = null;
		if(sumType==0){//查询全部
			startTime = new Date(0L);
			endTime = new Date((new java.util.Date()).getTime()+86400000);
		}else if(sumType==1){//本周
			startTime = new Date(DateUtil.getTimesWeekmorning().getTime());
			endTime = new Date(DateUtil.getTimesWeeknight().getTime());
		}else if(sumType==2){//上周
			startTime = new Date(DateUtil.getTimesLastWeekmorning().getTime());
			endTime = new Date(DateUtil.getTimesLastWeeknight().getTime());
		}else if(sumType==3){//本月
			startTime = new Date(DateUtil.getTimesMonthmorning().getTime());
			endTime = new Date(DateUtil.getTimesMonthnight().getTime());
		}else if(sumType==4){//上月
			startTime = new Date(DateUtil.getTimesLastMonthmorning().getTime());
			endTime = new Date(DateUtil.getTimesLastMonthnight().getTime());
		}else if(sumType==5){//今天
			startTime = new Date(DateUtil.getTodayStart().getTime());
			endTime = new Date(DateUtil.getTodayEnd().getTime());
		}else if(sumType==6){//昨天
			startTime = new Date(DateUtil.getYesterdayStart().getTime());
			endTime = new Date(DateUtil.getYesterdayEnd().getTime());
		}
		Map<String,Object> params = new HashMap<String,Object>();
		if(managerId>2)
		params.put("managerId", managerId);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		return paylogMapper.sumByManagerId(params);
	}

	@Override
	public int sumByTime(int managerId, Date start, Date end) {
		Map<String,Object> params = new HashMap<String,Object>();
		if(managerId>2)
		params.put("managerId", managerId);
		params.put("startTime", start);
		params.put("endTime", end);
		return paylogMapper.sumByManagerId(params);
	}

	@Override
	public int sumSubByManagerId(int managerId, int sumType,int powerId) {
		//0是查询全部1是查本周2是查上周3是查本月4是查上月
				Date startTime = null;
				Date endTime = null;
				if(sumType==0){//查询全部
					startTime = new Date(0L);
					endTime = new Date((new java.util.Date()).getTime()+86400000);
				}else if(sumType==1){//本周
					startTime = new Date(DateUtil.getTimesWeekmorning().getTime());
					endTime = new Date(DateUtil.getTimesWeeknight().getTime());
				}else if(sumType==2){//上周
					startTime = new Date(DateUtil.getTimesLastWeekmorning().getTime());
					endTime = new Date(DateUtil.getTimesLastWeeknight().getTime());
				}else if(sumType==3){//本月
					startTime = new Date(DateUtil.getTimesMonthmorning().getTime());
					endTime = new Date(DateUtil.getTimesMonthnight().getTime());
				}else if(sumType==4){//上月
					startTime = new Date(DateUtil.getTimesLastMonthmorning().getTime());
					endTime = new Date(DateUtil.getTimesLastMonthnight().getTime());
				}else if(sumType==5){//今天
					startTime = new Date(DateUtil.getTodayStart().getTime());
					endTime = new Date(DateUtil.getTodayEnd().getTime());
				}else if(sumType==6){//昨天
					startTime = new Date(DateUtil.getYesterdayStart().getTime());
					endTime = new Date(DateUtil.getYesterdayEnd().getTime());
				}
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("startTime", startTime);
				params.put("endTime", endTime);
				params.put("managerId", managerId);
				int result = 0;
				if(managerId>2){
					result+=paylogMapper.sumSubByManagerId(params);
					//宁都的不需要做处理
					if(powerId>3){//一级代理
						result+=paylogMapper.sumSub2ByManagerId(params);
						if(powerId>4)//总代
							result+=paylogMapper.sumSub3ByManagerId(params);
					}
					
				}
				return result;
				
				
	}

	@Override
	public int sumSubByTime(int managerId, Date start, Date end) {
		Map<String,Object> params = new HashMap<String,Object>();
		if(managerId>2)
		params.put("managerId", managerId);
		params.put("startTime", start);
		params.put("endTime", end);
		return paylogMapper.sumSubByManagerId(params);
	}

	@Override
	public int insertPaylog(Paylog paylog) {
		// TODO Auto-generated method stub
		return paylogMapper.insertSelective(paylog);
	}

	@Override
	public void tixianUpdate(Integer managerId,int status) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("managerId", managerId);
		params.put("status", status);
		paylogMapper.tixianUpdate(params);
	}

	@Override
	public void tixianDone(Integer id,int status) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("id", id);
		if(status>1)
			params.put("status", status);
		paylogMapper.tixianDone(params);
	}


	@Override
	public List<Paylog> selectUserPaySum(int managerId, int searchType, int start, int limit, Date startTime,
			Date endTime) {
		// TODO Auto-generated method stub
		if(startTime==null&&endTime==null){
		System.out.println("searchType============="+searchType);
		if(searchType==0){//查询全部
			startTime = new Date(0L);
			endTime = new Date((new java.util.Date()).getTime()+86400000);
			System.out.println(startTime.toLocaleString());
			System.out.println(endTime.toLocaleString());
		}else if(searchType==1){//本周
			startTime = new Date(DateUtil.getTimesWeekmorning().getTime());
			endTime = new Date(DateUtil.getTimesWeeknight().getTime());
			System.out.println(startTime.toLocaleString());
			System.out.println(endTime.toLocaleString());
		}else if(searchType==2){//上周
			startTime = new Date(DateUtil.getTimesLastWeekmorning().getTime());
			endTime = new Date(DateUtil.getTimesLastWeeknight().getTime());
			System.out.println(startTime.toLocaleString());
			System.out.println(endTime.toLocaleString());
		}else if(searchType==3){//本月
			startTime = new Date(DateUtil.getTimesMonthmorning().getTime());
			endTime = new Date(DateUtil.getTimesMonthnight().getTime());
			System.out.println(startTime.toLocaleString());
			System.out.println(endTime.toLocaleString());
		}else if(searchType==4){//上月
			startTime = new Date(DateUtil.getTimesLastMonthmorning().getTime());
			endTime = new Date(DateUtil.getTimesLastMonthnight().getTime());
			System.out.println(startTime.toLocaleString());
			System.out.println(endTime.toLocaleString());
		}else if(searchType==5){//今天
			startTime = new Date(DateUtil.getTodayStart().getTime());
			endTime = new Date(DateUtil.getTodayEnd().getTime());
		}else if(searchType==6){//昨天
			startTime = new Date(DateUtil.getYesterdayStart().getTime());
			endTime = new Date(DateUtil.getYesterdayEnd().getTime());
		}
		}
		Map<String,Object> params = new HashMap<String,Object>();
		if(managerId>2)
		params.put("managerId", managerId);
		params.put("startNum", start);
		params.put("pageNumber", limit);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		return paylogMapper.selectUserPaySum(params);
	}


	@Override
	public int selectUserPaySumCount(int managerId, int searchType, int start, int limit, Date startTime,
			Date endTime) {
		// TODO Auto-generated method stub
		if(startTime==null&&endTime==null){
			System.out.println("searchType============="+searchType);
			if(searchType==0){//查询全部
				startTime = new Date(0L);
				endTime = new Date((new java.util.Date()).getTime()+86400000);
				System.out.println(startTime.toLocaleString());
				System.out.println(endTime.toLocaleString());
			}else if(searchType==1){//本周
				startTime = new Date(DateUtil.getTimesWeekmorning().getTime());
				endTime = new Date(DateUtil.getTimesWeeknight().getTime());
				System.out.println(startTime.toLocaleString());
				System.out.println(endTime.toLocaleString());
			}else if(searchType==2){//上周
				startTime = new Date(DateUtil.getTimesLastWeekmorning().getTime());
				endTime = new Date(DateUtil.getTimesLastWeeknight().getTime());
				System.out.println(startTime.toLocaleString());
				System.out.println(endTime.toLocaleString());
			}else if(searchType==3){//本月
				startTime = new Date(DateUtil.getTimesMonthmorning().getTime());
				endTime = new Date(DateUtil.getTimesMonthnight().getTime());
				System.out.println(startTime.toLocaleString());
				System.out.println(endTime.toLocaleString());
			}else if(searchType==4){//上月
				startTime = new Date(DateUtil.getTimesLastMonthmorning().getTime());
				endTime = new Date(DateUtil.getTimesLastMonthnight().getTime());
				System.out.println(startTime.toLocaleString());
				System.out.println(endTime.toLocaleString());
			}else if(searchType==5){//今天
				startTime = new Date(DateUtil.getTodayStart().getTime());
				endTime = new Date(DateUtil.getTodayEnd().getTime());
			}else if(searchType==6){//昨天
				startTime = new Date(DateUtil.getYesterdayStart().getTime());
				endTime = new Date(DateUtil.getYesterdayEnd().getTime());
			}
			}
			Map<String,Object> params = new HashMap<String,Object>();
			if(managerId>2)
			params.put("managerId", managerId);
			params.put("startNum", start);
			params.put("pageNumber", limit);
			params.put("startTime", startTime);
			params.put("endTime", endTime);
			return paylogMapper.selectUserPaySumCount(params);
	}


	@Override
	public int updateByPrimaryKey(Paylog record) {
		// TODO Auto-generated method stub
		return paylogMapper.updateByPrimaryKey(record);
	}


	@Override
	public Paylog selectByUuidAndStatus(Integer uuid) {
		// TODO Auto-generated method stub
		return paylogMapper.selectByUuidAndStatus(uuid);
	}
}
