package com.tucao.bbs.manager;

import com.tucao.bbs.entity.BbsCreditExchange;

public interface BbsCreditExchangeMng {
	public BbsCreditExchange findById(Integer id);

	public BbsCreditExchange update(BbsCreditExchange bean);
}