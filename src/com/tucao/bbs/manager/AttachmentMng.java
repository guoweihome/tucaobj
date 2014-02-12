package com.tucao.bbs.manager;

import com.tucao.bbs.entity.Attachment;
import com.tucao.common.page.Pagination;

public interface AttachmentMng {
	public Pagination getPage(int pageNo, int pageSize);

	public Attachment findById(Integer id);

	public Attachment save(Attachment bean);

	public Attachment update(Attachment bean);

	public Attachment deleteById(Integer id);
	
	public Attachment[] deleteByIds(Integer[] ids);
}