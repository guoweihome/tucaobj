package com.tucao.core.dao;

import java.util.List;

import com.tucao.common.hibernate3.Updater;
import com.tucao.common.page.Pagination;
import com.tucao.core.entity.UnifiedUser;

public interface UnifiedUserDao {
	public UnifiedUser getByUsername(String username);

	public List<UnifiedUser> getByEmail(String email);

	public int countByEmail(String email);

	public Pagination getPage(int pageNo, int pageSize);

	public UnifiedUser findById(Integer id);

	public UnifiedUser save(UnifiedUser bean);

	public UnifiedUser updateByUpdater(Updater<UnifiedUser> updater);

	public UnifiedUser deleteById(Integer id);
	
	public UnifiedUser getUnifiedUserByOpenId(String openid,boolean isqq);
}