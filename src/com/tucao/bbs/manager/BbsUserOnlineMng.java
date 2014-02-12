package com.tucao.bbs.manager;

import java.util.List;

import com.tucao.bbs.entity.BbsUserOnline;
import com.tucao.common.page.Pagination;

public interface BbsUserOnlineMng {

	public List<BbsUserOnline> getList();

	public Pagination getPage(int pageNo, int pageSize);

	public BbsUserOnline findById(Integer id);

	public BbsUserOnline save(BbsUserOnline bean);

	public BbsUserOnline update(BbsUserOnline bean);

	public BbsUserOnline deleteById(Integer id);

	public BbsUserOnline[] deleteByIds(Integer[] ids);
}