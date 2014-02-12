package com.tucao.core.dao;

import java.util.List;

import com.tucao.common.hibernate3.Updater;
import com.tucao.core.entity.CmsConfig;

public interface CmsConfigDao {
	public CmsConfig findById(Integer id);

	public CmsConfig updateByUpdater(Updater<CmsConfig> updater);

	public List<CmsConfig> getList();
}