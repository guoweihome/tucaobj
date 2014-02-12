package com.tucao.bbs.dao;

import com.tucao.bbs.entity.BbsMagicLog;
import com.tucao.common.hibernate3.Updater;
import com.tucao.common.page.Pagination;

public interface BbsMagicLogDao {

	public Pagination getPage(Byte operator,Integer userId,int pageNo, int pageSize);

	public BbsMagicLog findById(Integer id);

	public BbsMagicLog save(BbsMagicLog bean);

	public BbsMagicLog updateByUpdater(Updater<BbsMagicLog> bean);

	public BbsMagicLog deleteById(Integer id);

}