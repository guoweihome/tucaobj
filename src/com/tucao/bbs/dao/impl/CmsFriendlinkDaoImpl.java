package com.tucao.bbs.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tucao.bbs.dao.CmsFriendlinkDao;
import com.tucao.bbs.entity.CmsFriendlink;
import com.tucao.common.hibernate3.Finder;
import com.tucao.common.hibernate3.HibernateBaseDao;

@Repository
public class CmsFriendlinkDaoImpl extends
		HibernateBaseDao<CmsFriendlink, Integer> implements CmsFriendlinkDao {
	@SuppressWarnings("unchecked")
	public List<CmsFriendlink> getList(Integer siteId, Integer ctgId,
			Boolean enabled) {
		Finder f = Finder.create("from CmsFriendlink bean where 1=1");
		if (enabled != null) {
			f.append(" and bean.enabled=:enabled");
			f.setParam("enabled", enabled);
		}
		if (siteId != null) {
			f.append(" and bean.site.id=:siteId");
			f.setParam("siteId", siteId);
		}
		if (ctgId != null) {
			f.append(" and bean.category.id=:ctgId");
			f.setParam("ctgId", ctgId);
		}
		f.append(" order by bean.priority asc");
		return find(f);
	}

	public int countByCtgId(Integer ctgId) {
		String hql = "select count(*) from CmsFriendlink bean where bean.category.id=:ctgId";
		return ((Number) getSession().createQuery(hql).setParameter("ctgId",
				ctgId).iterate().next()).intValue();
	}

	public CmsFriendlink findById(Integer id) {
		CmsFriendlink entity = get(id);
		return entity;
	}

	public CmsFriendlink save(CmsFriendlink bean) {
		getSession().save(bean);
		return bean;
	}

	public CmsFriendlink deleteById(Integer id) {
		CmsFriendlink entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<CmsFriendlink> getEntityClass() {
		return CmsFriendlink.class;
	}
}