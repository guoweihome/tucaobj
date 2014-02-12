package com.tucao.bbs.manager;

import com.tucao.bbs.entity.BbsLoginLog;
import com.tucao.common.page.Pagination;

public interface BbsLoginLogMng {

	public Pagination getPage(int pageNo, int pageSize);

	public BbsLoginLog findById(Integer id);

	public BbsLoginLog save(BbsLoginLog bean);

	public BbsLoginLog update(BbsLoginLog bean);

	public BbsLoginLog deleteById(Integer id);

	public BbsLoginLog[] deleteByIds(Integer[] ids);
}