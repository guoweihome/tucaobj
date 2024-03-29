package com.tucao.bbs.dao;

import com.tucao.bbs.entity.BbsMagicConfig;
import com.tucao.common.hibernate3.Updater;

public interface BbsMagicConfigDao {
	
	public BbsMagicConfig findById(Integer id);

	public BbsMagicConfig save(BbsMagicConfig bean);

	public BbsMagicConfig updateByUpdater(Updater<BbsMagicConfig> updater);

	public BbsMagicConfig deleteById(Integer id);
}