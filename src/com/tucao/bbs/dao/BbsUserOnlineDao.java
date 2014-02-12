package com.tucao.bbs.dao;

import java.util.List;

import com.tucao.bbs.entity.BbsUserOnline;
import com.tucao.common.hibernate3.Updater;
import com.tucao.common.page.Pagination;

public interface BbsUserOnlineDao {

	public Pagination getPage(int pageNo, int pageSize);

	public BbsUserOnline findById(Integer id);

	public BbsUserOnline save(BbsUserOnline bean);

	public BbsUserOnline updateByUpdater(Updater<BbsUserOnline> bean);

	public BbsUserOnline deleteById(Integer id);

	public List<BbsUserOnline> getList();

}