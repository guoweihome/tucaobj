package com.tucao.bbs.dao.impl;

import static com.tucao.bbs.entity.BbsFriendShip.ACCEPT;
import static com.tucao.bbs.entity.BbsFriendShip.APPLYING;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.tucao.bbs.dao.BbsFriendShipDao;
import com.tucao.bbs.entity.BbsFriendShip;
import com.tucao.common.hibernate3.Finder;
import com.tucao.common.hibernate3.HibernateBaseDao;
import com.tucao.common.page.Pagination;

@Repository
public class BbsFriendShipDaoImpl extends
		HibernateBaseDao<BbsFriendShip, Integer> implements BbsFriendShipDao {
	public BbsFriendShip findById(Integer id) {
		BbsFriendShip entity = get(id);
		return entity;
	}

	public BbsFriendShip save(BbsFriendShip bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsFriendShip deleteById(Integer id) {
		BbsFriendShip entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	public BbsFriendShip getFriendShip(Integer userId, Integer friendId) {
		String hql = "from BbsFriendShip bean where bean.user.id=:userId and bean.friend.id=:friendId";
		Finder f = Finder.create(hql).setParam("userId", userId).setParam("friendId", friendId);
		f.setMaxResults(1);
		Query query = f.createQuery(getSession());
		return (BbsFriendShip) query.uniqueResult();
	}
	
	public Pagination getPageByUserId(Integer userId, Integer pageNo, Integer pageSize) {
		String hql = "from BbsFriendShip bean where bean.user.id=:userId and bean.status="+ACCEPT;
		Finder f = Finder.create(hql).setParam("userId", userId);
		return find(f, pageNo, pageSize);
	}
	
	public Pagination getApplyByUserId(Integer userId, Integer pageNo, Integer pageSize) {
		String hql = "from BbsFriendShip bean where bean.friend.id=:userId and bean.status="+APPLYING;
		Finder f = Finder.create(hql).setParam("userId", userId);
		return find(f, pageNo, pageSize);
	}

	@Override
	protected Class<BbsFriendShip> getEntityClass() {
		return BbsFriendShip.class;
	}
}