package com.tucao.bbs.manager;


import com.tucao.bbs.entity.BbsMessageReply;
import com.tucao.common.page.Pagination;


public interface BbsMessageReplyMng {
	public BbsMessageReply findById(Integer id);

	public BbsMessageReply save(BbsMessageReply bean);

	public BbsMessageReply update(BbsMessageReply bean);

	public BbsMessageReply deleteById(Integer id);

	public BbsMessageReply[] deleteByIds(Integer[] ids);

	public Pagination getPageByMsgId(Integer msgId, Integer pageNo, Integer pageSize);
}