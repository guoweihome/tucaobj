package com.tucao.bbs.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tucao.bbs.dao.AttachmentDao;
import com.tucao.bbs.entity.Attachment;
import com.tucao.bbs.manager.AttachmentMng;
import com.tucao.common.hibernate3.Updater;
import com.tucao.common.page.Pagination;

@Service
@Transactional
public class AttachmentMngImpl implements AttachmentMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public Attachment findById(Integer id) {
		Attachment entity = dao.findById(id);
		return entity;
	}

	public Attachment save(Attachment bean) {
		dao.save(bean);
		return bean;
	}

	public Attachment update(Attachment bean) {
		Updater<Attachment> updater = new Updater<Attachment>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public Attachment deleteById(Integer id) {
		Attachment bean = dao.deleteById(id);
		return bean;
	}
	
	public Attachment[] deleteByIds(Integer[] ids) {
		Attachment[] beans = new Attachment[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private AttachmentDao dao;

	@Autowired
	public void setDao(AttachmentDao dao) {
		this.dao = dao;
	}
}