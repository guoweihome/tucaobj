package com.tucao.bbs.entity;

import com.tucao.bbs.entity.base.BaseBbsPostType;



public class BbsPostType extends BaseBbsPostType {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsPostType () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsPostType (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsPostType (
		java.lang.Integer id,
		com.tucao.core.entity.CmsSite site,
		com.tucao.bbs.entity.BbsForum forum) {

		super (
			id,
			site,
			forum);
	}

/*[CONSTRUCTOR MARKER END]*/


}