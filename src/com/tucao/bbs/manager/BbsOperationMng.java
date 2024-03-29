﻿package com.tucao.bbs.manager;

import com.tucao.bbs.entity.BbsOperation;
import com.tucao.bbs.entity.BbsUser;
import com.tucao.common.page.Pagination;
import com.tucao.core.entity.CmsSite;

public interface BbsOperationMng {
	/**
	 * 保存操作记录
	 * 
	 * @param site
	 *            站点
	 * @param operator
	 *            操作者
	 * @param optName
	 *            操作名称
	 * @param reason
	 *            操作理由
	 * @param target
	 *            操作目标
	 * @return
	 */
	public BbsOperation saveOpt(CmsSite site, BbsUser operator, String optName,
			String reason, Object target);

	public Pagination getPage(int pageNo, int pageSize);

	public BbsOperation findById(Integer id);

	public BbsOperation save(BbsOperation bean);

	public BbsOperation update(BbsOperation bean);

	public BbsOperation deleteById(Integer id);

	public BbsOperation[] deleteByIds(Integer[] ids);
}