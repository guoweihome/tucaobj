package com.tucao.bbs.json.vo;

import java.io.Serializable;

public class BbsTopicBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private String seq;
	
	private String title;
	
	private String content;
	
	private String img;
	
	private Long viewcount;
	
	private Integer replaycount;
	
	private String topredirect;
	
	private String comefrom;
	
	private String createDate;
	
	private String usercode;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	

	public Long getViewcount() {
		return viewcount;
	}

	public void setViewcount(Long viewcount) {
		this.viewcount = viewcount;
	}

	public Integer getReplaycount() {
		return replaycount;
	}

	public void setReplaycount(Integer replaycount) {
		this.replaycount = replaycount;
	}

	public String getTopredirect() {
		return topredirect;
	}

	public void setTopredirect(String topredirect) {
		this.topredirect = topredirect;
	}

	public String getComefrom() {
		return comefrom;
	}

	public void setComefrom(String comefrom) {
		this.comefrom = comefrom;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
	

}
