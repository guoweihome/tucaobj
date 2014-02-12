package com.tucao.bbs.manager;


import com.tucao.bbs.entity.BbsVoteRecord;


public interface BbsVoteRecordMng {
	public BbsVoteRecord findRecord(Integer userId, Integer topicId);
	
	public BbsVoteRecord save(BbsVoteRecord bean);
}
