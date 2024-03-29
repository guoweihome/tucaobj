package com.tucao.bbs.manager;

import java.util.List;

import com.tucao.bbs.entity.CmsSensitivity;

public interface CmsSensitivityMng {

	public String replaceSensitivity(Integer siteId, String s);

	public List<CmsSensitivity> getList(Integer siteId, boolean cacheable);

	public CmsSensitivity findById(Integer id);

	public CmsSensitivity save(CmsSensitivity bean, Integer siteId);

	public void updateEnsitivity(Integer[] ids, String[] searchs,
			String[] replacements);

	public CmsSensitivity deleteById(Integer id);

	public CmsSensitivity[] deleteByIds(Integer[] ids);
}