package com.tucao.bbs.manager.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tucao.bbs.dao.BbsMessageReplyDao;
import com.tucao.bbs.entity.BbsMessageReply;
import com.tucao.bbs.manager.BbsMessageReplyMng;
import com.tucao.common.hibernate3.Updater;
import com.tucao.common.page.Pagination;

@Service
@Transactional
public class BbsMessageReplyMngImpl implements BbsMessageReplyMng {
	@Transactional(readOnly = true)
	public BbsMessageReply findById(Integer id) {
		BbsMessageReply entity = dao.findById(id);
		return entity;
	}

	public BbsMessageReply save(BbsMessageReply bean) {
		dao.save(bean);
		return bean;
	}

	public BbsMessageReply update(BbsMessageReply bean) {
		Updater<BbsMessageReply> updater = new Updater<BbsMessageReply>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public BbsMessageReply deleteById(Integer id) {
		BbsMessageReply bean = dao.deleteById(id);
		return bean;
	}

	public BbsMessageReply[] deleteByIds(Integer[] ids) {
		BbsMessageReply[] beans = new BbsMessageReply[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	public Pagination getPageByMsgId(Integer msgId, Integer pageNo,
			Integer pageSize) {
		return dao.getPageByMsgId(msgId, pageNo, pageSize);
	}

	private BbsMessageReplyDao dao;

	@Autowired
	public void setDao(BbsMessageReplyDao dao) {
		this.dao = dao;
	}
}