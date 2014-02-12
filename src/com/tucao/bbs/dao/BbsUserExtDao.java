package com.tucao.bbs.dao;

import com.tucao.bbs.entity.BbsUserExt;
import com.tucao.common.hibernate3.Updater;

public interface BbsUserExtDao {
	public BbsUserExt findById(Integer id);

	public BbsUserExt save(BbsUserExt bean);

	public BbsUserExt updateByUpdater(Updater<BbsUserExt> updater);
}