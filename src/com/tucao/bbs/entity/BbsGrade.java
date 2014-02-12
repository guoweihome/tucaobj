package com.tucao.bbs.entity;

import com.tucao.bbs.entity.base.BaseBbsGrade;



public class BbsGrade extends BaseBbsGrade {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsGrade () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsGrade (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsGrade (
		java.lang.Integer id,
		com.tucao.bbs.entity.BbsPost post,
		com.tucao.bbs.entity.BbsUser grader) {

		super (
			id,
			post,
			grader);
	}

/*[CONSTRUCTOR MARKER END]*/


}