package com.tucao.bbs.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tucao.bbs.dao.BbsReportExtDao;
import com.tucao.bbs.entity.BbsReportExt;
import com.tucao.bbs.manager.BbsReportExtMng;
import com.tucao.common.hibernate3.Updater;
import com.tucao.common.page.Pagination;

@Service
@Transactional
public class BbsReportExtMngImpl implements BbsReportExtMng {

	@Transactional(readOnly = true)
	public Pagination getPage(Integer reportId,Integer pageNo, Integer pageSize) {
		Pagination page = dao.getPage(reportId,pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public BbsReportExt findById(Integer id) {
		BbsReportExt entity = dao.findById(id);
		return entity;
	}
	
	@Transactional(readOnly = true)
	public BbsReportExt findByReportUid(Integer reportId, Integer userId) {
		
		return dao.findByReportUid(reportId,userId);
	}

	public BbsReportExt save(BbsReportExt bean) {
		dao.save(bean);
		return bean;
	}

	public BbsReportExt update(BbsReportExt bean) {
		Updater<BbsReportExt> updater = new Updater<BbsReportExt>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public BbsReportExt deleteById(Integer id) {
		BbsReportExt bean = dao.deleteById(id);
		return bean;
	}

	public BbsReportExt[] deleteByIds(Integer[] ids) {
		BbsReportExt[] beans = new BbsReportExt[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private BbsReportExtDao dao;

	@Autowired
	public void setDao(BbsReportExtDao dao) {
		this.dao = dao;
	}

	

}
