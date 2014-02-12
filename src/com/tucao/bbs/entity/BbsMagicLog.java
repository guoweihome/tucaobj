package com.tucao.bbs.entity;

import com.tucao.bbs.entity.base.BaseBbsMagicLog;



public class BbsMagicLog extends BaseBbsMagicLog {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsMagicLog () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsMagicLog (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsMagicLog (
		java.lang.Integer id,
		com.tucao.bbs.entity.BbsCommonMagic magic,
		com.tucao.bbs.entity.BbsUser user,
		java.util.Date logTime) {

		super (
			id,
			magic,
			user,
			logTime);
	}

/*[CONSTRUCTOR MARKER END]*/


}