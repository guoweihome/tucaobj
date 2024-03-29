package com.tucao.bbs.entity;

import org.apache.commons.lang.StringUtils;

import com.tucao.bbs.entity.base.BaseCmsFriendlink;

public class CmsFriendlink extends BaseCmsFriendlink {
	private static final long serialVersionUID = 1L;

	public void init() {
		if (getPriority() == null) {
			setPriority(10);
		}
		if (getViews() == null) {
			setViews(0);
		}
		if (getEnabled() == null) {
			setEnabled(true);
		}
		blankToNull();
	}

	public void blankToNull() {
		if (StringUtils.isBlank(getLogo())) {
			setLogo(null);
		}
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public CmsFriendlink () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsFriendlink (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsFriendlink (
		java.lang.Integer id,
		com.tucao.bbs.entity.CmsFriendlinkCtg category,
		com.tucao.core.entity.CmsSite site,
		java.lang.String name,
		java.lang.String domain,
		java.lang.Integer views,
		java.lang.Integer priority,
		java.lang.Boolean enabled) {

		super (
			id,
			category,
			site,
			name,
			domain,
			views,
			priority,
			enabled);
	}

	/* [CONSTRUCTOR MARKER END] */

}