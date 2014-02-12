package com.tucao.bbs.dao;

import com.tucao.bbs.entity.BbsCreditExchange;
import com.tucao.common.hibernate3.Updater;

public interface BbsCreditExchangeDao {
	
	public BbsCreditExchange findById(Integer id);

	public BbsCreditExchange save(BbsCreditExchange bean);

	public BbsCreditExchange updateByUpdater(Updater<BbsCreditExchange> updater);

	public BbsCreditExchange deleteById(Integer id);
}