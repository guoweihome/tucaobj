﻿package com.tucao.bbs.entity;

import com.tucao.bbs.entity.base.BaseBbsOperation;

public class BbsOperation extends BaseBbsOperation {
	private static final long serialVersionUID = 1L;
	public static final String TOPIC = "TOPI";
	public static final String POST = "POST";
	public static final String MEMBER = "MEMB";

	private Object target;

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public BbsOperation () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsOperation (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsOperation (
		java.lang.Integer id,
		com.tucao.core.entity.CmsSite site,
		com.tucao.bbs.entity.BbsUser operater,
		com.tucao.bbs.entity.BbsPost post,
		java.lang.String optName,
		java.util.Date optTime) {

		super (
			id,
			site,
			operater,
			post,
			optName,
			optTime);
	}

	/* [CONSTRUCTOR MARKER END] */

}