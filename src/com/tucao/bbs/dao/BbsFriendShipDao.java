package com.tucao.bbs.dao;


import com.tucao.bbs.entity.BbsFriendShip;
import com.tucao.common.hibernate3.Updater;
import com.tucao.common.page.Pagination;

public interface BbsFriendShipDao {
	public BbsFriendShip findById(Integer id);

	public BbsFriendShip save(BbsFriendShip bean);

	public BbsFriendShip updateByUpdater(Updater<BbsFriendShip> updater);

	public BbsFriendShip deleteById(Integer id);

	public BbsFriendShip getFriendShip(Integer userId, Integer friendId);

	public Pagination getPageByUserId(Integer userId, Integer pageNo, Integer pageSize);

	public Pagination getApplyByUserId(Integer userId, Integer pageNo,
			Integer pageSize);
}