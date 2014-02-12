package com.tucao.core.manager;

import java.util.Date;

import com.tucao.core.entity.CmsConfig;

public interface CmsConfigMng {
	public CmsConfig get();

	public void updateCountCopyTime(Date d);

	public void updateCountClearTime(Date d);

	public CmsConfig update(CmsConfig bean);
}