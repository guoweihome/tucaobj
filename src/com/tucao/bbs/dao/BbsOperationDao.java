package com.tucao.bbs.dao;

import com.tucao.bbs.entity.BbsOperation;
import com.tucao.common.hibernate3.Updater;
import com.tucao.common.page.Pagination;

public interface BbsOperationDao {

	public Pagination getPage(int pageNo, int pageSize);

	public BbsOperation findById(Integer id);

	public BbsOperation save(BbsOperation bean);

	public BbsOperation updateByUpdater(Updater<BbsOperation> updater);

	public BbsOperation deleteById(Integer id);

}