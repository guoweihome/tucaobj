package com.tucao.bbs.action.front;

import static com.tucao.bbs.Constants.TPLDIR_FORUM;
import static com.tucao.bbs.Constants.TPLDIR_HOME;
import static com.tucao.bbs.Constants.TPLDIR_INCLUDE;
import static com.tucao.bbs.Constants.TPLDIR_INDEX;
import static com.tucao.bbs.Constants.TPLDIR_SHUOSH;
import static com.tucao.bbs.Constants.TPLDIR_TOPIC;
import static com.tucao.common.web.Constants.INDEX;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qq.connect.utils.json.JSONException;
import com.qq.connect.utils.json.JSONObject;
import com.tucao.bbs.cache.TopicCountEhCache;
import com.tucao.bbs.entity.BbsForum;
import com.tucao.bbs.entity.BbsPostType;
import com.tucao.bbs.entity.BbsTopic;
import com.tucao.bbs.entity.BbsUser;
import com.tucao.bbs.entity.BbsUserGroup;
import com.tucao.bbs.json.vo.AjaxResponseMessage;
import com.tucao.bbs.json.vo.ServletResponseHelper;
import com.tucao.bbs.manager.BbsConfigMng;
import com.tucao.bbs.manager.BbsForumMng;
import com.tucao.bbs.manager.BbsPostTypeMng;
import com.tucao.bbs.manager.BbsTopicMng;
import com.tucao.bbs.manager.BbsUserMng;
import com.tucao.bbs.web.CmsUtils;
import com.tucao.bbs.web.FrontUtils;
import com.tucao.common.util.DateUtils;
import com.tucao.common.web.CookieUtils;
import com.tucao.common.web.RequestUtils;
import com.tucao.common.web.ResponseUtils;
import com.tucao.common.web.session.SessionProvider;
import com.tucao.common.web.springmvc.MessageResolver;
import com.tucao.core.entity.CmsSite;
import com.tucao.core.web.front.URLHelper;

@Controller
public class DynamicPageAct {
	private static final Logger log = LoggerFactory
			.getLogger(DynamicPageAct.class);
    private final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

	public static final String TPL_INDEX = "tpl.index";
	public static final String TPL_FORUM = "tpl.forum";
	public static final String TPL_TOPIC = "tpl.topic";
	public static final String TPL_NO_VIEW = "tpl.noview";
	public static final String TPL_HOME = "tpl.home";
	public static final String TPL_TUCAO = "tpl.tucao";
	public static final String TPL_SHAREWEIQQ = "tpl.qqweishare";
	
	public static final String LOGIN_INPUT = "tpl.loginInput";
	public static final String TPL_SHUOSH = "tpl.shuosh";
	
	@RequestMapping(value = "/index.jhtml", method = RequestMethod.GET)
	public String home(HttpServletRequest request, ModelMap model,HttpServletResponse response) throws UnsupportedEncodingException {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		String processCookies = (String) session.getAttribute(request, "processCookies");
		if(!("true".equals(processCookies))&&!("false".equals(processCookies))){
			String result = cookieLogin(request,model,response);
			if(cookieLogin(request,model,response)!=null){
				return result;
			}
		}else if("true".equals(processCookies)){
			Cookie c_username = CookieUtils.getCookie(request, "bbs_username");
			if(c_username==null){
				return null;
			}
			model.addAttribute("cookie_username", URLDecoder.decode(c_username.getValue(),"utf-8"));
		}
		//首页推荐
		List<BbsTopic> list=bbsTopicMng.getListTopShow(1);
		
		if(list!=null&&list.size()>0){
			int i=0;
			for(BbsTopic bbs:list){
				i++;
				bbs.setCreateString(DateUtils.parseDate(bbs.getCreateTime(), "MM月dd日  HH:mm"));
				bbs.setNumShow(String.valueOf(i));
			}
		}
		
		model.put("bbsContentList", list);

		//活跃大神
		List<BbsUser> bbsUserList=bbsUserMng.getHotMember(8);
		model.put("bbsUserList", bbsUserList);
		
		//热门标签
		List<BbsForum> bbsfromList= this.bbsForumMng.getList(site.getId());
		model.put("bbsfromList", bbsfromList);
		
		
		return FrontUtils.getTplPath(request, site.getSolutionPath(),TPLDIR_HOME, TPL_HOME);
	}
	
	
	@RequestMapping(value = "/shuosh.jhtml", method = RequestMethod.GET)
	public String shuosh(HttpServletRequest request, ModelMap model,HttpServletResponse response) throws UnsupportedEncodingException {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		List<BbsTopic> list=bbsTopicMng.getShuoshList(10);
		
		System.out.println("查询结果："+list.size());
		model.put("bbs", list);
		String keys="";
		for(BbsTopic bbs:list){
			keys=keys+bbs.getId()+",";
		}
		
		if(!"".equals(keys)){
			keys=keys.substring(0,keys.length()-1);
		}
		model.put("datetime", DateUtils.parseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		
		String processCookies = (String) session.getAttribute(request, "processCookies");
		if(!("true".equals(processCookies))&&!("false".equals(processCookies))){
			String result = cookieLogin(request,model,response);
			if(cookieLogin(request,model,response)!=null){
				return result;
			}
		}else if("true".equals(processCookies)){
			Cookie c_username = CookieUtils.getCookie(request, "bbs_username");
			if(c_username==null){
				return null;
			}
			model.addAttribute("cookie_username", URLDecoder.decode(c_username.getValue(),"utf-8"));
		}
		
		//活跃大神
		List<BbsUser> bbsUserList=bbsUserMng.getHotMember(8);
		model.put("bbsUserList", bbsUserList);
		
		//热门标签
		List<BbsForum> bbsfromList= this.bbsForumMng.getList(site.getId());
		model.put("bbsfromList", bbsfromList);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),TPLDIR_SHUOSH, TPL_SHUOSH);
	}
	
	@RequestMapping(value = "/ajaxshuosh.jhtml" ,method = RequestMethod.POST)
	public void ajaxtoppost(HttpServletRequest request, ModelMap model,HttpServletResponse response) throws JSONException {
		
		//System.out.println("请求结果"+(String)request.getParameter("datetime"));
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		JSONObject obj=new JSONObject();
		//bbsTopicMng.getNewList(10, (String)request.getParameter("datetime"));
		List<BbsTopic> list=bbsTopicMng.getNewList(10, (String)request.getParameter("datetime"));
		StringBuffer sb=new StringBuffer();
		String img="";
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				BbsTopic bbs=(BbsTopic)list.get(i);
				sb.append("<li><div class='div_left'>");
				sb.append("<a href='javascript:vod(0)' onclick='javascript:apply("+"\""+bbs.getCreater().getUsername()+"\""+")' >");
				if(bbs.getCreater().getAvatar()!=null&&!"".equals(bbs.getCreater().getAvatar())&&bbs.getCreater().getAvatar().contains("user")){
					img=bbs.getCreater().getAvatar();
				}else if(bbs.getCreater().getAvatar()!=null&&!"".equals(bbs.getCreater().getAvatar())){
					img="/"+FrontUtils.getResPath(site)+"/bbs_member/img/face/"+bbs.getCreater().getAvatar();
				}else{
					img="/"+FrontUtils.getResPath(site)+"/bbs_member/img/face/none.gif";
				}
				
				sb.append("<img class='media-object img-circle' src='"+img+"'  title='加为好友' ></a>");
				sb.append("</div><div class='div_right'>");
				sb.append(" <h4 class='media-heading'><a href='javascript:vod(0)' onclick'javascript:apply("+"\""+bbs.getCreater().getUsername()+"\""+")'>");
				sb.append(bbs.getCreater().getUsername());
				sb.append("</a><span id='detail_"+bbs.getCreater().getUsername()+"'>");
				sb.append("</span>：<a href='"+site.getUrlWhole()+bbs.getFirstPost().getNewRedirectUrl()+"' target='_blank'>##"+bbs.getTitle()+"##</a> </h4>");
				
				sb.append(bbs.getFirstPost().getSubContentHtml());
				sb.append("<div class='twit_item_time'>"+bbs.getTimeString()+"</div>");
				sb.append("</div> </li>");
			}
			obj.put("nums", list.size());
			obj.put("strs",sb.toString());
			
			System.out.println("总计是："+list.size());
		}else{
			obj.put("nums", 0);
		}
		
		obj.put("datetime", DateUtils.parseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
				
		ResponseUtils.renderText(response, obj.toString());		
	}
	
	//我要吐槽
	@RequestMapping(value = "/tucaoforward.jhtml", method = RequestMethod.GET)
	public String tucaoforward(HttpServletRequest request, ModelMap model,HttpServletResponse response)  {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		BbsUser user = CmsUtils.getUser(request);
		
		BbsForum forum = bbsForumMng.findById(1);		
		Set<BbsPostType> postTypes=user.getPostTypeByForum(forum);
		
		System.out.println("发表主题："+postTypes.size());
		model.put("forumId", "1");
		model.put("postTypes", postTypes);
			
		return  FrontUtils.getTplPath(request, site.getSolutionPath(),TPLDIR_INCLUDE, TPL_TUCAO);
	}
	
//	@RequestMapping(value = "/qqweishare.jhtml", method = RequestMethod.GET)
//	public String qq_share_weibo(HttpServletRequest request, ModelMap model,HttpServletResponse response)  {
//		CmsSite site = CmsUtils.getSite(request);
//		FrontUtils.frontData(request, model, site);
//		Integer id=Integer.parseInt((String)request.getParameter("id"));
//		BbsTopic bbs=bbsTopicMng.findById(id);
//		model.put("url", site.getUrlWhole()+bbs.getFirstPost().getNewRedirectUrl());
//        model.put("title", bbs.getTitle());
//        model.put("createuser", bbs.getCreater().getUsername());
//        model.put("createdate", DateUtils.parseDate(bbs.getCreateTime(), "MM月dd日  HH:mm"));
//			
//		return  FrontUtils.getTplPath(request, site.getSolutionPath(),TPLDIR_USERLOGIN, TPL_SHAREWEIQQ);
//	}
	
	@RequestMapping(value = "tucaosubmit.jhtml" ,method = RequestMethod.POST)
	public void tucaosubmit(HttpServletRequest request, ModelMap model,HttpServletResponse response) throws JSONException {
		AjaxResponseMessage ajaxmessage = new AjaxResponseMessage();
		
		String forumId=(String)request.getParameter("forumId");
		String title=(String)request.getParameter("title");
		String content=(String)request.getParameter("content");
		String postTypeId=(String)request.getParameter("postTypeId");
		
		System.out.println("ceshi test:"+forumId+":"+title+":"+content+":"+postTypeId);
		try{		
		  CmsSite site = CmsUtils.getSite(request);
		  BbsUser user = CmsUtils.getUser(request);
				
		  String ip = RequestUtils.getIpAddr(request);
		
		  BbsTopic bean = bbsTopicMng.postTopic(user.getId(), site.getId(), Integer.parseInt(forumId),Integer.parseInt(postTypeId), title, content, ip, 1, null,null, null);
		  log.info("save BbsTopic id={}", bean.getId());
		}catch(Exception e){
			ajaxmessage.setSuccess(false);
			ajaxmessage.setMsg("保存出现异常!");
		}
		
		ServletResponseHelper.outUTF8(response, gson.toJson(ajaxmessage));
	}
		
	@RequestMapping(value = "/main.jhtml", method = RequestMethod.GET)
	public String index(HttpServletRequest request, ModelMap model,HttpServletResponse response) throws UnsupportedEncodingException {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		String processCookies = (String) session.getAttribute(request, "processCookies");
		if(!("true".equals(processCookies))&&!("false".equals(processCookies))){
			String result = cookieLogin(request,model,response);
			if(cookieLogin(request,model,response)!=null){
				return result;
			}
		}
		return FrontUtils.getTplPath(request, site.getSolutionPath(),TPLDIR_INDEX, TPL_INDEX);
	}
	
	@RequestMapping(value = "/toppost.jhtml")
	public void toppost(HttpServletRequest request, ModelMap model,HttpServletResponse response) {
		CmsSite site = CmsUtils.getSite(request);
		//FrontUtils.frontData(request, model, site);
		int numshow=Integer.parseInt((String)request.getParameter("page"));
		List<BbsTopic> list=bbsTopicMng.getListTopShow(numshow);
		StringBuffer sb=new StringBuffer();
		String img="";
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				BbsTopic bbs=(BbsTopic)list.get(i);
				sb.append("<div class='hot_feed_top_tit'>");
				sb.append("<h1 class='hot_tit_top'>");
				sb.append("<span class='txt_top'>TOP ");
				sb.append((numshow-1)*10+1+i);
				sb.append("</span>");
				sb.append("</h1>");
				sb.append("</div>");

				sb.append("<div class='WB_feed_type'>");
				sb.append("<div class='WB_feed_datail S_line2 clearfix'>");
				sb.append("<div class='WB_face'>");
				
				if(bbs.getCreater().getAvatar()!=null&&!"".equals(bbs.getCreater().getAvatar())&&bbs.getCreater().getAvatar().contains("user")){
					img=bbs.getCreater().getAvatar();
				}else if(bbs.getCreater().getAvatar()!=null&&!"".equals(bbs.getCreater().getAvatar())){
					img="/"+FrontUtils.getResPath(site)+"/bbs_member/img/face/"+bbs.getCreater().getAvatar();
				}else{
					img="/"+FrontUtils.getResPath(site)+"/bbs_member/img/face/none.gif";
				}
				sb.append("<a  href='javascript:vod(0)' onclick='javascript:apply("+"\""+bbs.getCreater().getUsername()+"\""+")'><img width='51' height='50' src='"+img+"' title='加为好友'  class='W_face_radius'></a>");
				sb.append("</div>");
				sb.append("<div class='WB_detail'>");
				sb.append("<div class='WB_info'>");
				sb.append("<a class='WB_name S_func1'  href='javascript:vod(0)' title='"+bbs.getCreater().getUsername()+"' onclick='javascript:apply("+"\""+bbs.getCreater().getUsername()+"\""+")'>"+bbs.getCreater().getUsername()+"</a><span id='detail_"+bbs.getCreater().getUsername()+"'/>");
				sb.append("</div>");
				sb.append("<div class='WB_text'>");
				sb.append("<a class='a_topic' href='"+site.getUrlWhole()+bbs.getFirstPost().getNewRedirectUrl()+"' target='_blank'>#"+bbs.getTitle()+"#</a>");
				sb.append(bbs.getFirstPost().getSubContentHtml());
		        sb.append("</div>");
		        
		        sb.append("<ul class='clearfix'>");//WB_media_list 
		      //  sb.append("<li><div class='chePicMin'> </div></li>");
		        sb.append("</ul>");
		        
		        //sb.append("<div class='WB_media_expand SW_fun2 S_line1 S_bg1' style='display: none;'></div>");
		        
		        sb.append("<div class='WB_func clearfix'><div class='WB_handle'>");
		        sb.append("<span class='S_func2'>浏览("+bbs.getViewCount()+")</span><em class='S_txt3'>|</em>");
//		        sb.append("<a class='S_func2' href='javascript:void(0);'>收藏</a><em class='S_txt3'>|</em>");
		        sb.append("<a class='S_func2' href='"+site.getUrlWhole()+bbs.getFirstPost().getNewRedirectUrl()+"' target='_blank'>回复("+bbs.getReplyCount()+")</a>");
		        sb.append("<em class='S_txt3'>|</em>");
		        sb.append("<span id='span_id_"+bbs.getId()+"'>|</span>");
		        sb.append("<script type='text/javascript'>");
	            sb.append("jsout('"+site.getUrlWhole()+bbs.getFirstPost().getNewRedirectUrl()+"','"+bbs.getTitle()+"','"+bbs.getId()+"');");
	            sb.append("</script>");
	            sb.append("<em class='S_txt3'>|</em><span id='span_qqshare_"+bbs.getId()+"'></span>");
	            sb.append("<script type='text/javascript'>");
	            sb.append("qqshare('"+site.getUrlWhole()+bbs.getFirstPost().getNewRedirectUrl()+"','"+bbs.getTitle()+"','"+bbs.getId()+"');");
	            sb.append("</script>");
	            
	            sb.append("<em class='S_txt3'>|</em><span id='span_sinashare_"+bbs.getId()+"'></span>");
	            sb.append("<script type='text/javascript'>");
	            sb.append("sinashare('"+site.getUrlWhole()+bbs.getFirstPost().getNewRedirectUrl()+"','"+bbs.getTitle()+"','"+bbs.getId()+"');");
	            sb.append("</script>");
		        sb.append("</div>");
		        
		        sb.append("<div class='WB_from S_txt3'>");
		        sb.append("<a class='S_func2 WB_time' href='javascript:vod(0)' >"+DateUtils.parseDate(bbs.getCreateTime(), "MM月dd日  HH:mm")+"</a>");
		        sb.append("来自<a href='javascript:vod(0)' rel='nofollow'>"+bbs.getComefrom()+"</a>");
		        sb.append(" </div></div></div></div></div>");
			}
		}  		
		
		//String text="html";
		ResponseUtils.renderText(response, sb.toString());
	}
	
	
	public String cookieLogin(HttpServletRequest request,ModelMap model,HttpServletResponse response) throws UnsupportedEncodingException{
		Cookie c_username = CookieUtils.getCookie(request, "bbs_username");
		Cookie c_password = CookieUtils.getCookie(request, "bbs_password");
		if(c_username==null||c_password==null){
			return null;
		}
		String username =URLDecoder.decode(c_username.getValue(),"utf-8");
		String password = c_password.getValue();
		if(!"".equals(username)&&!"".equals(password)){
			return "redirect:login_cookie.jspx";
		}
		return null;
	}
	
	@RequestMapping(value = "/**/*.*", method = RequestMethod.GET)
	public String dynamic(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,String ty) {
		if(ty==null){
			ty="a";
		}
		String[] paths = URLHelper.getPaths(request);
		String requestUrl = request.getRequestURI();
		String address = requestUrl.substring(requestUrl.lastIndexOf('/')+1,requestUrl.lastIndexOf('.'));
		if(ty!=null){
			if(ty.length()>=4){
				if("jing".equals(ty.substring(0,4))){
					return forum(paths[0],null, request, response, model,"index_jing",ty);
				}
			}
		}
		
		if("index_jing".equals(address)){
			return forum(paths[0], null,request, response, model,"index_jing",ty);
		}
		int len = paths.length;
		if (len == 1) {
			return null;
		} else if (len == 2) {
			
			//静态页面
			if (paths[0].equals("static")){
				return staticPage(paths[1],request, response, model,ty);
			}
			
			if (paths[1].equals(INDEX)) {
				// 主题列表
				return forum(paths[0],null, request, response, model,"index",ty);
			} else {
				// 主题详细页
				try {
					Integer topicId = Integer.parseInt(paths[1]);
					return topic(paths[0], topicId, request, response, model);
				} catch (NumberFormatException e) {
					log.debug("topic id must String: {}", paths[1]);
					return FrontUtils.pageNotFound(request, response, model);
				}
			}
		}else if(len==3){
			// 主题分类列表
			return forum(paths[0], paths[2],request, response, model,"index",ty);
		}else {
			log.debug("Illegal path length: {}, paths: {}", len, paths);
			return FrontUtils.pageNotFound(request, response, model);
		}
	}

	private String staticPage(String name, HttpServletRequest request,HttpServletResponse response, ModelMap model,String ty){
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(site.getSolutionPath(),"static", name);
	}
	
	private String forum(String path,String typeId, HttpServletRequest request,
			HttpServletResponse response, ModelMap model,String type,String ty) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		BbsForum forum = bbsForumMng.getByPath(site.getId(), path);
		boolean check = checkView(forum, user, site);
		if (!check) {
			model.put("msg",MessageResolver.getMessage(request, "member.hasnopermission"));
			return FrontUtils.getTplPath(request, site.getSolutionPath(),TPLDIR_FORUM, TPL_NO_VIEW);
		}
		model.put("manager", checkManager(forum, user, site));
		model.put("uptop", checkUpTop(forum, user, site));
		model.put("sheild", checkShield(forum, user, site));
		model.put("moderators", checkModerators(forum, user, site));//'版主
		model.put("forum", forum);
		if(StringUtils.isNotBlank(typeId)){
			Integer typeIds=Integer.valueOf(typeId);
			BbsPostType postType=bbsPostTypeMng.findById(typeIds);
			if(postType!=null&&postType.getParent()==null){
				//有子类的，包含子类
				if(postType.getChilds()!=null&&postType.getChilds().size()>0){
					model.put("parentTypeId", typeIds);
				}else{
					//帖子大类(没有子类)
					model.put("typeId", typeIds);
				}
			}else{
				model.put("typeId", typeIds);
			}
		}
		model.put("type", type);
		model.put("ty", ty);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),TPLDIR_FORUM, TPL_FORUM);
	}

	private String topic(String path, Integer topicId,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		BbsForum forum = bbsForumMng.getByPath(site.getId(), path);
		boolean check = checkView(forum, user, site);
		if (!check) {
			model.put("msg",MessageResolver.getMessage(request, "member.hasnopermission"));
			return FrontUtils.getTplPath(request, site.getSolutionPath(),
					TPLDIR_FORUM, TPL_NO_VIEW);
		}
		BbsTopic topic = bbsTopicMng.findById(topicId);
		topicCountEhCache.setViewCount(topicId);
		if(topic!=null&&topic.getPostType()!=null){
			model.put("postTypeId", topic.getPostType().getId());
		}
		model.put("moderators", checkModerators(forum, user, site));
		model.put("forum", forum);
		model.put("topic", topic);
		FrontUtils.frontPageData(request, model);
		
		System.out.println("获取地址："+FrontUtils.getTplPath(request, site.getSolutionPath(),TPLDIR_TOPIC, TPL_TOPIC));
		return FrontUtils.getTplPath(request, site.getSolutionPath(),TPLDIR_TOPIC, TPL_TOPIC);
	}

	private boolean checkView(BbsForum forum, BbsUser user, CmsSite site) {
		if (forum.getGroupViews() == null) {
			return false;
		} else {
			BbsUserGroup group = null;
			if (user == null) {
				group = bbsConfigMng.findById(site.getId()).getDefaultGroup();
			} else {
				group = user.getGroup();
			}
			if (group == null) {
				return false;
			}
			if (forum.getGroupViews().indexOf("," + group.getId() + ",") == -1) {
				return false;
			}
		}
		return true;
	}

	private boolean checkManager(BbsForum forum, BbsUser user, CmsSite site) {
		if (forum.getGroupViews() == null) {
			return false;
		} else {
			BbsUserGroup group = null;
			if (user == null) {
				group = bbsConfigMng.findById(site.getId()).getDefaultGroup();
			} else {
				group = user.getGroup();
			}
			if (group == null) {
				return false;
			}
			if (!group.hasRight(forum, user)) {
				return false;
			}
			if (!group.topicManage()) {
				return false;
			}
		}
		return true;
	}

	private boolean checkUpTop(BbsForum forum, BbsUser user, CmsSite site) {
		if (forum.getGroupViews() == null) {
			return false;
		} else {
			BbsUserGroup group = null;
			if (user == null) {
				group = bbsConfigMng.findById(site.getId()).getDefaultGroup();
			} else {
				group = user.getGroup();
			}
			if (group == null) {
				return false;
			}
			if (!group.hasRight(forum, user)) {
				return false;
			}
			if (group.topicTop() == 0) {
				return false;
			}
		}
		return true;
	}

	private boolean checkShield(BbsForum forum, BbsUser user, CmsSite site) {
		if (forum.getGroupViews() == null) {
			return false;
		} else {
			BbsUserGroup group = null;
			if (user == null) {
				group = bbsConfigMng.findById(site.getId()).getDefaultGroup();
			} else {
				group = user.getGroup();
			}
			if (group == null) {
				return false;
			}
			if (!group.hasRight(forum, user)) {
				return false;
			}
			if (!group.topicShield()) {
				return false;
			}
		}
		return true;
	}

	private boolean checkModerators(BbsForum forum, BbsUser user, CmsSite site) {
		if (forum.getGroupViews() == null) {
			return false;
		} else {
			BbsUserGroup group = null;
			if (user == null) {
				group = bbsConfigMng.findById(site.getId()).getDefaultGroup();
			} else {
				group = user.getGroup();
			}
			if (group == null) {
				return false;
			}
			if (!group.hasRight(forum, user)) {
				return false;
			}
		}
		return true;
	}

	
	@Autowired
	private BbsTopicMng bbsTopicMng;
	@Autowired
	private BbsForumMng bbsForumMng;
	@Autowired
	private BbsConfigMng bbsConfigMng;
	@Autowired
	private TopicCountEhCache topicCountEhCache;
	@Autowired
	private SessionProvider session;
	@Autowired
	private BbsPostTypeMng bbsPostTypeMng;
	@Autowired
	private BbsUserMng bbsUserMng;

}
