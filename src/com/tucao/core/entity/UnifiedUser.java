package com.tucao.core.entity;

import com.tucao.core.entity.base.BaseUnifiedUser;



public class UnifiedUser extends BaseUnifiedUser {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public UnifiedUser () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public UnifiedUser (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public UnifiedUser (
		java.lang.Integer id,
		java.lang.String username,
		java.lang.String password,
		java.util.Date registerTime,
		java.lang.String registerIp,
		java.lang.Integer loginCount,
		java.lang.String openId,
		java.lang.String weiboId) {

		super (
			id,
			username,
			password,
			registerTime,
			registerIp,
			loginCount,
			openId,
			weiboId);
	}

/*[CONSTRUCTOR MARKER END]*/


}