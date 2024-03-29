package com.tucao.bbs.manager.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tucao.bbs.dao.BbsVoteItemDao;
import com.tucao.bbs.entity.BbsTopic;
import com.tucao.bbs.entity.BbsUser;
import com.tucao.bbs.entity.BbsVoteItem;
import com.tucao.bbs.entity.BbsVoteRecord;
import com.tucao.bbs.entity.BbsVoteTopic;
import com.tucao.bbs.manager.BbsVoteItemMng;
import com.tucao.bbs.manager.BbsVoteRecordMng;
import com.tucao.common.hibernate3.Updater;

@Service
@Transactional
public class BbsVoteItemMngImpl implements BbsVoteItemMng {
	public BbsVoteItem findById(Integer id) {
		return dao.findById(id);
	}

	public List<BbsVoteItem> findByTopic(Integer topicId) {
		return dao.findByTopic(topicId);
	}

	public BbsVoteItem save(BbsVoteItem bean) {
		return dao.save(bean);
	}

	public BbsVoteItem update(BbsVoteItem bean) {
		Updater<BbsVoteItem> updater = new Updater<BbsVoteItem>(bean);
		BbsVoteItem entity = dao.updateByUpdater(updater);
		return entity;
	}

	public void vote(BbsUser user, BbsVoteTopic topic, Integer[] itemIds) {
		int count = 0;
		for (Integer id : itemIds) {
			BbsVoteItem bean = findById(id);
			bean.setVoteCount(bean.getVoteCount() + 1);
			count++;
		}
		topic.setTotalCount(topic.getTotalCount() + count);
		BbsVoteRecord record = new BbsVoteRecord();
		record.setUser(user);
		record.setTopic(topic);
		record.setVoteTime(new Date());
		bbsVoteRecordMng.save(record);
	}

	private BbsVoteItemDao dao;
	@Autowired
	private BbsVoteRecordMng bbsVoteRecordMng;

	@Autowired
	public void setDao(BbsVoteItemDao dao) {
		this.dao = dao;
	}
}
