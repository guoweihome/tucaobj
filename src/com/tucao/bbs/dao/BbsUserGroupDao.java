package com.tucao.bbs.dao;

import java.util.List;

import com.tucao.bbs.entity.BbsUser;
import com.tucao.bbs.entity.BbsUserGroup;
import com.tucao.common.hibernate3.Updater;
import com.tucao.common.page.Pagination;

public interface BbsUserGroupDao {
	/**
	 * 获得论坛会员组
	 * 
	 * @param siteId
	 *            站点ID
	 * @return
	 */
	public List<BbsUserGroup> getList(Integer siteId);

	public Pagination getPage(int pageNo, int pageSize);

	public BbsUserGroup getRegDef();

	public BbsUserGroup findById(Integer id);

	public BbsUserGroup save(BbsUserGroup bean);

	public BbsUserGroup updateByUpdater(Updater<BbsUserGroup> updater);

	public BbsUserGroup deleteById(Integer id);
	
	public BbsUserGroup findNearByPoint(Long point);
}