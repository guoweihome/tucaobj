package com.tucao.bbs.dao;

import com.tucao.bbs.entity.BbsMemberMagic;
import com.tucao.common.hibernate3.Updater;
import com.tucao.common.page.Pagination;

public interface BbsMemberMagicDao {

	public Pagination getPage(Integer userId, int pageNo, int pageSize);

	public BbsMemberMagic findById(Integer id);

	public BbsMemberMagic save(BbsMemberMagic bean);

	public BbsMemberMagic updateByUpdater(Updater<BbsMemberMagic> bean);

	public BbsMemberMagic deleteById(Integer id);

}