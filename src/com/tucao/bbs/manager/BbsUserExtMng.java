package com.tucao.bbs.manager;

import com.tucao.bbs.entity.BbsUser;
import com.tucao.bbs.entity.BbsUserExt;

public interface BbsUserExtMng {
	public BbsUserExt save(BbsUserExt ext, BbsUser user);

	public BbsUserExt update(BbsUserExt ext, BbsUser user);
}