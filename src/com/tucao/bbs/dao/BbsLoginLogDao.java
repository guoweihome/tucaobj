package com.tucao.bbs.dao;

import com.tucao.bbs.entity.BbsLoginLog;
import com.tucao.common.hibernate3.Updater;
import com.tucao.common.page.Pagination;

public interface BbsLoginLogDao {
	
	public Pagination getPage(int pageNo, int pageSize);

	public BbsLoginLog findById(Integer id);

	public BbsLoginLog save(BbsLoginLog bean);

	public BbsLoginLog updateByUpdater(Updater<BbsLoginLog> bean);

	public BbsLoginLog deleteById(Integer id);
	
}