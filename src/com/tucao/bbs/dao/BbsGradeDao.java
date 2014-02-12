package com.tucao.bbs.dao;

import com.tucao.bbs.entity.BbsGrade;
import com.tucao.common.hibernate3.Updater;
import com.tucao.common.page.Pagination;

public interface BbsGradeDao {
	public Pagination getPage(int pageNo, int pageSize);

	public BbsGrade findById(Integer id);

	public BbsGrade save(BbsGrade bean);

	public BbsGrade updateByUpdater(Updater<BbsGrade> updater);

	public BbsGrade deleteById(Integer id);
}