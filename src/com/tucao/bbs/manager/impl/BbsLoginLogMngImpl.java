package com.tucao.bbs.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tucao.bbs.dao.BbsLoginLogDao;
import com.tucao.bbs.entity.BbsLoginLog;
import com.tucao.bbs.manager.BbsLoginLogMng;
import com.tucao.common.hibernate3.Updater;
import com.tucao.common.page.Pagination;

@Service
@Transactional
public class BbsLoginLogMngImpl implements BbsLoginLogMng {

	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public BbsLoginLog findById(Integer id) {
		BbsLoginLog entity = dao.findById(id);
		return entity;
	}

	public BbsLoginLog save(BbsLoginLog bean) {
		dao.save(bean);
		return bean;
	}

	public BbsLoginLog update(BbsLoginLog bean) {
		Updater<BbsLoginLog> updater = new Updater<BbsLoginLog>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public BbsLoginLog deleteById(Integer id) {
		BbsLoginLog bean = dao.deleteById(id);
		return bean;
	}

	public BbsLoginLog[] deleteByIds(Integer[] ids) {
		BbsLoginLog[] beans = new BbsLoginLog[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private BbsLoginLogDao dao;

	@Autowired
	public void setDao(BbsLoginLogDao dao) {
		this.dao = dao;
	}

}
