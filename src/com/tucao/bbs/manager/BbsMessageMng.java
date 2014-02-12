package com.tucao.bbs.manager;


import java.util.List;

import com.tucao.bbs.entity.BbsMessage;
import com.tucao.bbs.entity.BbsUser;
import com.tucao.common.page.Pagination;


public interface BbsMessageMng {
	public BbsMessage findById(Integer id);

	public BbsMessage save(BbsMessage bean);

	public BbsMessage update(BbsMessage bean);

	public BbsMessage deleteById(Integer id);

	public BbsMessage[] deleteByIds(Integer[] ids);
	
	public void sendMsg(BbsUser sender, BbsUser receiver, BbsMessage sMsg);
	
	public Pagination getPageByUserId(Integer userId,Integer typeId, Integer pageNo, Integer pageSize);
	
	public List getListUserIdStatus(Integer userId,Integer typeId, Boolean status);
	
	public boolean hasUnReadMessage(Integer userId,Integer typeId);
}