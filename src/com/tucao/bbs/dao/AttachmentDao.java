package com.tucao.bbs.dao;

import com.tucao.bbs.entity.Attachment;
import com.tucao.common.hibernate3.Updater;
import com.tucao.common.page.Pagination;

public interface AttachmentDao {
	public Pagination getPage(int pageNo, int pageSize);

	public Attachment findById(Integer id);

	public Attachment save(Attachment bean);

	public Attachment updateByUpdater(Updater<Attachment> updater);

	public Attachment deleteById(Integer id);
}