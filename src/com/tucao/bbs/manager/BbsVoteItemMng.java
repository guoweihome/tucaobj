package com.tucao.bbs.manager;

import java.util.List;

import com.tucao.bbs.entity.BbsUser;
import com.tucao.bbs.entity.BbsVoteItem;
import com.tucao.bbs.entity.BbsVoteTopic;


public interface BbsVoteItemMng {
	public BbsVoteItem findById(Integer id);
	
	public List<BbsVoteItem> findByTopic(Integer topicId);
	
	public BbsVoteItem save(BbsVoteItem bean);
	
	public BbsVoteItem update(BbsVoteItem bean);

	public void vote(BbsUser user, BbsVoteTopic topic, Integer[] itemIds);
}
