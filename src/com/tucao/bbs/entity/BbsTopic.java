package com.tucao.bbs.entity;

import static com.tucao.bbs.Constants.DAY_MILLIS;
import static com.tucao.bbs.web.FrontUtils.replaceSensitivity;

import java.sql.Timestamp;
import java.util.Date;

import com.tucao.bbs.entity.base.BaseBbsTopic;

public class BbsTopic extends BaseBbsTopic {
	
	private String createString;
	
	private String numShow;
	
	
	
	public String getNumShow() {
		return numShow;
	}

	public void setNumShow(String numShow) {
		this.numShow = numShow;
	}

	public String getCreateString() {
		return createString;
	}

	public void setCreateString(String createString) {
		this.createString = createString;
	}

	private static final long serialVersionUID = 1L;
	/**
	 * 正常状态
	 */
	public static final short NORMAL = 0;
	/**
	 * 屏蔽状态
	 */
	public static final short SHIELD = -1;
	/**
	 * 锁定状态
	 */
	public static final short LOCKED = 1;
	
	
	/**
	 * 普通帖
	 */
	public static final Integer TOPIC_NORMAL = 100;
	
	/**
	 * 投票帖
	 */
	public static final Integer TOPIC_VOTE = 101;
	
	/**
	 * 前台状态
	 * 
	 * @return 3:锁;2:旧;1:新
	 */
	public short getFrontStatus() {
		if (isLocked()) {
			return 3;
		} else if (System.currentTimeMillis() - getLastTime().getTime() > DAY_MILLIS) {
			return 2;
		} else {
			return 1;
		}
	}

	/**
	 * 是否热帖
	 * 
	 * @return
	 */
	public boolean isHot() {
		return getReplyCount() >= 30;
	}

	/**
	 * 是否锁定
	 * 
	 * @return
	 */
	public boolean isLocked() {
		return getStatus() == LOCKED
				|| getForum().isTopicLock(getCreateTime().getTime());
	}

	/**
	 * 是否屏蔽
	 * 
	 * @return
	 */
	public boolean isShield() {
		return getStatus() == SHIELD;
	}

	/**
	 * 样式是否有效
	 * 
	 * @return
	 */
	public boolean isStyle() {
		Date d = getStyleTime();
		if (d == null) {
			return true;
		}
		long time = d.getTime();
		return time - System.currentTimeMillis() > 0;
	}

	public String getUrl() {
		return getSite().getUrlBuffer(true, null, false).append("/").append(
				getForum().getPath()).append("/").append(getId()).append(
				getSite().getDynamicSuffix()).toString();
	}

	/**
	 * 获得访问路径前缀。如：http://bbs.tucao.com/luntan/2
	 * 
	 * @return
	 */
	public StringBuilder getUrlPerfix() {
		return getSite().getUrlBuffer(true, null, false).append("/").append(
				getForum().getPath()).append("/").append(getId());
	}

	public String getRedirectUrl() {
		String path = getForum().getPath();
		String url = "/" + path + "/" + getId() + getSite().getDynamicSuffix();
		return url;
	}

	public void init() {
		Date now = new Timestamp(System.currentTimeMillis());
		if (getCreateTime() == null) {
			setCreateTime(now);
		}
		if (getLastTime() == null) {
			setLastTime(now);
		}
		if (getPrimeLevel() == null) {
			setPrimeLevel(NORMAL);
		}
		if (getSortTime() == null) {
			setSortTime(now);
		}
		if (getViewCount() == null) {
			setViewCount(0L);
		}
		if (getReplyCount() == null) {
			setReplyCount(0);
		}
		if (getTopLevel() == null) {
			setTopLevel(NORMAL);
		}
		if (getStyleBold() == null) {
			setStyleBold(false);
		}
		if (getStatus() == null) {
			setStatus(NORMAL);
		}
		if (getModeratorReply() == null) {
			setModeratorReply(false);
		}
		if(getAllayReply()==null){
			setAllayReply(true);
		}
		if(getHasSofeed()==null){
			setHasSofeed(false);
		}
	}
	
	public String getTitle(){
		BbsTopicText text = getTopicText();
		if (text == null) {
			return null;
		} else {
			return replaceSensitivity(text.getTitle());
		}
	}
	
	public String getTimeString(){
		long nd = 1000*24*60*60;//一天的毫秒数
		long nh = 1000*60*60;//一小时的毫秒数
		long nm = 1000*60;//一分钟的毫秒数
		long ns = 1000;//
		
		
		long diff=System.currentTimeMillis()-this.getCreateTime().getTime();
		
		long day = diff/nd;//计算差多少天
		long hour = diff%nd/nh;//计算差多少小时
		long min = diff%nd%nh/nm;//计算差多少分钟
		long sec = diff%nd%nh%nm/ns;//计算差多少秒//输出结果


		
		if(day!=0){
			return String.valueOf(day)+"天前";
		}else if(hour!=0){
			return String.valueOf(hour)+"小时前";
		}else if(min!=0){
			return String.valueOf(min)+"分钟前";
		}else if(sec!=0){
			return String.valueOf(sec)+"秒前";
		}else{
			return "0"+"时间";
		}
	}
	
	public void setTitle(String title) {
		BbsTopicText text = getTopicText();
		if (text != null) {
			text.setTitle(title);
		} 
	}
	public void setTopicTitle(String title) {
		super.setTitle(title);
	}
	
	
	public Integer getCategory(){
		return TOPIC_NORMAL;
	}
	
	/* [CONSTRUCTOR MARKER BEGIN] */
	public BbsTopic () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsTopic (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsTopic (
		java.lang.Integer id,
		com.tucao.core.entity.CmsSite site,
		com.tucao.bbs.entity.BbsForum forum,
		com.tucao.bbs.entity.BbsUser creater,
		com.tucao.bbs.entity.BbsUser lastReply,
		java.util.Date createTime,
		java.util.Date lastTime,
		java.util.Date sortTime,
		java.lang.Long viewCount,
		java.lang.Integer replyCount,
		java.lang.Short topLevel,
		java.lang.Short primeLevel,
		java.lang.Boolean styleBold,
		java.lang.Boolean styleItalic,
		java.lang.Short status,
		java.lang.Boolean affix,
		java.lang.Boolean moderatorReply) {

		super (
			id,
			site,
			forum,
			creater,
			lastReply,
			createTime,
			lastTime,
			sortTime,
			viewCount,
			replyCount,
			topLevel,
			primeLevel,
			styleBold,
			styleItalic,
			status,
			affix,
			moderatorReply);
	}
	/* [CONSTRUCTOR MARKER END] */

}