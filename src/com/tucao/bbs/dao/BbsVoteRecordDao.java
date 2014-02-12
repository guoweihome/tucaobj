package com.tucao.bbs.dao;

import com.tucao.bbs.entity.BbsVoteRecord;




public interface BbsVoteRecordDao {
	public BbsVoteRecord findRecord(Integer userId, Integer topicId);
	
	public BbsVoteRecord save(BbsVoteRecord bean);
}
