package com.tucao.bbs.manager;

import com.tucao.bbs.entity.BbsMagicConfig;

public interface BbsMagicConfigMng {

	public BbsMagicConfig findById(Integer id);

	public BbsMagicConfig update(BbsMagicConfig bean);

}