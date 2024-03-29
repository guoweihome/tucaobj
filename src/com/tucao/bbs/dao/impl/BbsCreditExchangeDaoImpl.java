package com.tucao.bbs.dao.impl;

import org.springframework.stereotype.Repository;

import com.tucao.bbs.dao.BbsCreditExchangeDao;
import com.tucao.bbs.entity.BbsCreditExchange;
import com.tucao.common.hibernate3.HibernateBaseDao;

@Repository
public class BbsCreditExchangeDaoImpl extends
		HibernateBaseDao<BbsCreditExchange, Integer> implements
		BbsCreditExchangeDao {
	public BbsCreditExchange findById(Integer id) {
		BbsCreditExchange entity = get(id);
		return entity;
	}

	public BbsCreditExchange save(BbsCreditExchange bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsCreditExchange deleteById(Integer id) {
		BbsCreditExchange entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<BbsCreditExchange> getEntityClass() {
		return BbsCreditExchange.class;
	}
}