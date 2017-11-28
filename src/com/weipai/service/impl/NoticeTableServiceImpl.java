package com.weipai.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.weipai.mapper.NoticeTableMapper;
import com.weipai.model.NoticeTable;
import com.weipai.service.NoticeTableService;

@Service
@Transactional(propagation=Propagation.REQUIRED,rollbackFor=RuntimeException.class)
public class NoticeTableServiceImpl implements NoticeTableService {
	@Resource
	NoticeTableMapper noticeTableMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return noticeTableMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(NoticeTable record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelective(NoticeTable record) {
		// TODO Auto-generated method stub
		return noticeTableMapper.insertSelective(record);
	}

	@Override
	public NoticeTable selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateByPrimaryKeySelective(NoticeTable record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateByPrimaryKey(NoticeTable record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<NoticeTable> selectRecentlyObject(Map<String,Object> params) {
		return noticeTableMapper.selectRecentlyObject(params);
	}

	@Override
	public List<NoticeTable> selectAllNotice(Map<String,Object> params) {
		// TODO Auto-generated method stub
		return noticeTableMapper.selectAllNotice(params);
	}

	@Override
	public int selectAllNoticeCount() {
		// TODO Auto-generated method stub
		return noticeTableMapper.selectAllNoticeCount();
	}

	@Override
	public int updateStatusByPrimaryKey(NoticeTable record) {
		// TODO Auto-generated method stub
		return noticeTableMapper.updateStatusByPrimaryKey(record);
	}

}
