package com.tucao.bbs.dao;


import com.tucao.bbs.entity.BbsMessageReply;
import com.tucao.common.hibernate3.Updater;
import com.tucao.common.page.Pagination;

public interface BbsMessageReplyDao {
	public BbsMessageReply findById(Integer id);

	public BbsMessageReply save(BbsMessageReply bean);

	public BbsMessageReply updateByUpdater(Updater<BbsMessageReply> updater);

	public BbsMessageReply deleteById(Integer id);

	public Pagination getPageByMsgId(Integer msgId, Integer pageNo,
			Integer pageSize);
}