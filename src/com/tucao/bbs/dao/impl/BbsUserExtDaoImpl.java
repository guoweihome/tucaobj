package com.tucao.bbs.dao.impl;

import org.springframework.stereotype.Repository;

import com.tucao.bbs.dao.BbsUserExtDao;
import com.tucao.bbs.entity.BbsUserExt;
import com.tucao.common.hibernate3.HibernateBaseDao;

@Repository
public class BbsUserExtDaoImpl extends HibernateBaseDao<BbsUserExt, Integer> implements BbsUserExtDao {
	public BbsUserExt findById(Integer id) {
		BbsUserExt entity = get(id);
		return entity;
	}

	public BbsUserExt save(BbsUserExt bean) {
		getSession().save(bean);
		return bean;
	}
	
	@Override
	protected Class<BbsUserExt> getEntityClass() {
		return BbsUserExt.class;
	}
}