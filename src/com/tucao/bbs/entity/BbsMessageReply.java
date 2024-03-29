package com.tucao.bbs.entity;

import com.tucao.bbs.entity.base.BaseBbsMessageReply;



public class BbsMessageReply extends BaseBbsMessageReply {
	private static final long serialVersionUID = 1L;
	
	public BbsMessageReply(BbsMessage message){
		setContent(message.getContent());
		setCreateTime(message.getCreateTime());
		setMessage(message);
		setSender(message.getSender());
		setReceiver(message.getReceiver());
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsMessageReply () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsMessageReply (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsMessageReply (
		java.lang.Integer id,
		com.tucao.bbs.entity.BbsMessage message,
		com.tucao.bbs.entity.BbsUser receiver,
		java.util.Date createTime) {

		super (
			id,
			message,
			receiver,
			createTime);
	}

/*[CONSTRUCTOR MARKER END]*/


}