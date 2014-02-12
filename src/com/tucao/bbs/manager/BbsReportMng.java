package com.tucao.bbs.manager;

import com.tucao.bbs.entity.BbsReport;
import com.tucao.common.page.Pagination;

public interface BbsReportMng {

	public Pagination getPage(Boolean status,Integer pageNo, Integer pageSize);

	public BbsReport findById(Integer id);
	
	public BbsReport findByUrl(String url);

	public BbsReport save(BbsReport bean);
	

	public BbsReport update(BbsReport bean);

	public BbsReport deleteById(Integer id);

	public BbsReport[] deleteByIds(Integer[] ids);
}