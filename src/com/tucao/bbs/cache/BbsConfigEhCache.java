package com.tucao.bbs.cache;

import com.tucao.bbs.entity.BbsUser;

public interface BbsConfigEhCache {
	
	public void setBbsConfigCache(int postToday, int topicTotal, int postTotal,
			int userTotal, BbsUser lastUser, Integer siteId);

	public BbsConfigCache getBbsConfigCache(Integer siteId);
}
