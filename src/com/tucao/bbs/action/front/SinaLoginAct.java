package com.tucao.bbs.action.front;

import static com.tucao.bbs.Constants.TPLDIR_HOME;
import static com.tucao.bbs.Constants.TPLDIR_USERLOGIN;
import static com.tucao.bbs.Constants.TPLDIR_INCLUDE;
import static com.tucao.core.action.front.LoginAct.RETURN_URL;
import static com.tucao.core.manager.AuthenticationMng.AUTH_KEY;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import weibo4j.Oauth;
import weibo4j.http.AccessToken;
import weibo4j.model.WeiboException;
import weibo4j.util.BareBonesBrowserLaunch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octo.captcha.service.image.ImageCaptchaService;

import com.tucao.bbs.cache.BbsConfigEhCache;
import com.tucao.bbs.entity.BbsForum;
import com.tucao.bbs.entity.BbsLoginLog;
import com.tucao.bbs.entity.BbsTopic;
import com.tucao.bbs.entity.BbsUser;
import com.tucao.bbs.entity.BbsUserExt;
import com.tucao.bbs.entity.BbsUserGroup;
import com.tucao.bbs.entity.BbsUserOnline;
import com.tucao.bbs.json.vo.AjaxResponseMessage;
import com.tucao.bbs.json.vo.ServletResponseHelper;
import com.tucao.bbs.manager.BbsConfigMng;
import com.tucao.bbs.manager.BbsForumMng;
import com.tucao.bbs.manager.BbsLoginLogMng;
import com.tucao.bbs.manager.BbsTopicMng;
import com.tucao.bbs.manager.BbsUserMng;
import com.tucao.bbs.manager.BbsUserOnlineMng;
import com.tucao.bbs.web.CmsUtils;
import com.tucao.bbs.web.FrontUtils;
import com.tucao.common.security.BadCredentialsException;
import com.tucao.common.security.DisabledException;
import com.tucao.common.security.UsernameNotFoundException;
import com.tucao.common.util.DateUtils;
import com.tucao.common.web.CookieUtils;
import com.tucao.common.web.RequestUtils;
import com.tucao.common.web.session.SessionProvider;
import com.tucao.core.entity.Authentication;
import com.tucao.core.entity.CmsSite;
import com.tucao.core.entity.UnifiedUser;
import com.tucao.core.manager.AuthenticationMng;
import com.tucao.core.manager.UnifiedUserMng;


@Controller
public class SinaLoginAct {
	
	 private final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	 public static String LAST_KEEP_SESSION_TIME = "last_keep_session_time";
		
	 @Autowired
		private SessionProvider session;
		
		@Autowired
		private BbsUserMng bbsUserMng;
		@Autowired
		private AuthenticationMng authMng;
		
		@Autowired
		private BbsLoginLogMng bbsLoginMng;
		@Autowired
		private BbsUserOnlineMng userOnlineMng;
		@Autowired
		private BbsConfigMng bbsConfigMng;
		@Autowired
		private BbsConfigEhCache bbsConfigEhCache;
		
		@Autowired
		private ImageCaptchaService imageCaptchaService;
		
		@Autowired
		private BbsForumMng bbsForumMng;
		@Autowired
		private BbsTopicMng bbsTopicMng;
		
		private UnifiedUserMng unifiedUserMng;

		@Autowired
		public void setUserMng(UnifiedUserMng unifiedUserMng) {
			this.unifiedUserMng = unifiedUserMng;
		}
		
		public static final String TPL_HOME = "tpl.home";
		public static final String TPL_SINABIAN = "tpl.sinabind";
		
	 @RequestMapping(value = "/sinaindex.jspx")
	 public String sina_login(HttpServletRequest request,HttpServletResponse response) throws IOException{
			response.setContentType("text/html;charset=utf-8");
			Oauth oauth = new Oauth();
			
			try{
			response.sendRedirect(oauth.authorize("code","all","1"));
			//BareBonesBrowserLaunch.openURL(oauth.authorize("code","all","1"));
	       
			}catch(WeiboException e){
				e.printStackTrace();
				return null;
			}
	       
			return null;
	  }
	 
	 @RequestMapping(value = "/afterSinaLogin.jspx")
		public String afterSinaLogin(HttpServletRequest request, ModelMap model,HttpServletResponse response) throws IOException {//throws IOException
			 
	         CmsSite site = CmsUtils.getSite(request);
	         
	         System.out.println("测试结果："+request.getParameter("code"));
			try{
			   AccessToken accessTokenObj = (new Oauth()).getAccessTokenByCode((String)request.getParameter("code"));
			  if (accessTokenObj.getAccessToken().equals("")) {
            //	            我们的网站被CSRF攻击了或者用户取消了授权, 做一些数据统计工作          
				  model.put("message", "sina账号授权有问题，请账号直接登录");
				  FrontUtils.frontData(request, model, site);		
				  return getView(null, site.getUrlWhole(), null);
	        } else {
	        	 System.out.println("返回结果uid:"+accessTokenObj.getUid());
	         	 session.setAttribute(request, response, "sina_access_token",accessTokenObj.getAccessToken());
	         	 session.setAttribute(request, response, "sina_token_expirein",String.valueOf(accessTokenObj.getExpireIn()));             

	             // 利用获取到的accessToken 去获取当前用的uid -------- start
	            
	             session.setAttribute(request, response, "sina_openid",accessTokenObj.getUid()); //openID
	             
	             UnifiedUser undifiedUser =unifiedUserMng.getUnifiedUserByOpenId(accessTokenObj.getUid(),false);//openID
	             //查询此用户是否已经绑定了,未绑定跳转到绑定页面
	             if(undifiedUser==null){
	         		 FrontUtils.frontData(request, model, site);
	         		 model.put("sinalogin", "1");//qq登录是1要判断是否和原来账户绑定
	         		 
	         		 System.out.println("测试是否进去sina：");
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
	             
	             //如果用户绑定了，则直接登录
	             String ip = RequestUtils.getIpAddr(request);
	             Authentication auth = authMng.login(undifiedUser, ip,request, response, session);
	 			 bbsUserMng.updateLoginInfo(auth.getUid(), ip);
	 			 BbsUser user = bbsUserMng.findById(auth.getUid());
	 			 if (user.getDisabled() != null && user.getDisabled()) {
	 				// 如果已经禁用，则推出登录。
	 				authMng.deleteById(auth.getId());
	 				session.logout(request, response);
	 				throw new DisabledException("user disabled");
	 			}
	 			// 登录记录
	 			BbsLoginLog loginLog = new BbsLoginLog();
	 			loginLog.setIp(RequestUtils.getIpAddr(request));
	 			Calendar calendar = Calendar.getInstance();
	 			loginLog.setLoginTime(calendar.getTime());
	 			loginLog.setUser(user);
	 			bbsLoginMng.save(loginLog);

	 			// 在线时长统计
	 			BbsUserOnline online = user.getUserOnline();
	 			if (online != null) {
	 				// 更新在线信息(这里对最后一次登陆时长做初始化，其余初始化用定时任务)
	 				online.setOnlineLatest(0d);
	 				userOnlineMng.update(online);
	 			} else {
	 				// 首次登陆
	 				online = new BbsUserOnline();
	 				online.setUser(user);
	 				online.initial();
	 				userOnlineMng.save(online);
	 			}
	 			session.setAttribute(request, response, LAST_KEEP_SESSION_TIME,calendar.getTime());
	             
	 			
	 			
	            String view = getView(null, site.getUrlWhole(), auth.getId());
							
				return view;
				           
	        }
			  
			}catch (WeiboException e2) {
				e2.printStackTrace();
				model.put("message", "sina登录有问题!");
				FrontUtils.frontData(request, model, site);		
				return getView(null, site.getUrlWhole(), null);
	        } catch (UsernameNotFoundException e) {
				model.put("message", "登录账号不正确!");
				FrontUtils.frontData(request, model, site);		
				return getView(null, site.getUrlWhole(), null);
			} catch (BadCredentialsException e) {
				model.put("message", "登录密码不正确!");
				FrontUtils.frontData(request, model, site);		
				return getView(null, site.getUrlWhole(), null);
			} catch (DisabledException e) {
				FrontUtils.frontData(request, model, site);
				model.put("message", "登录账号已禁用!");
				return getView(null, site.getUrlWhole(), null);
			}
		}
		
		
		private String getContent(CmsSite site ){
			//首页推荐
				List<BbsTopic> list=bbsTopicMng.getListTopShow(1);
				StringBuffer sb=new StringBuffer();
				String img="";
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						BbsTopic bbs=(BbsTopic)list.get(i);
						sb.append("<div class='hot_feed_top_tit'>");
						sb.append("<h1 class='hot_tit_top'>");
						sb.append("<span class='txt_top'>TOP ");
						sb.append(1+i);
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
			           
//				        sb.append("(function(){ var p = { url:'',showcount:'1',desc:'',summary:'',title:'',site:'',pics:'',style:'203',width:98,height:22};");
//				        sb.append(" var s = []; ");
//				        sb.append(" for(var i in p){ ");
//				        sb.append(" s.push(i + '=' + encodeURIComponent(p[i]||'')); }");
//				        sb.append(" document.write(['<a version=\"1.0\" class='qzOpenerDiv' href='http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?\',s.join(\'&\'),\'' target='_blank'>分享</a>\'].join(''));");
//				        
				        sb.append("<span class='S_func2'>浏览("+bbs.getViewCount()+")</span><em class='S_txt3'>|</em>");
//				        sb.append("<a class='S_func2' href='javascript:void(0);'>收藏</a><em class='S_txt3'>|</em>");
				        sb.append("<a class='S_func2' href='"+site.getUrlWhole()+bbs.getFirstPost().getNewRedirectUrl()+"' target='_blank'>回复("+bbs.getReplyCount()+")</a>");
				        sb.append("<em class='S_txt3'>|</em>");
				        sb.append("<span id='span_id_"+bbs.getId()+"'></span>");
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
				
				return sb.toString();
		}
		/**
		 * qq与用户绑定界面
		 * @param request
		 * @param response
		 * @param model
		 * @return
		 * @throws UnsupportedEncodingException 
		 */
		@RequestMapping(value = "/sinauser_bind.jspx")
		public String sinauser_bind(HttpServletRequest request,
				HttpServletResponse response, ModelMap model) throws UnsupportedEncodingException {
			CmsSite site = CmsUtils.getSite(request);
			FrontUtils.frontData(request, model, site);
			String processCookies = (String) session.getAttribute(request, "processCookies");
			
			//System.out.println("测试："+processCookies);
	        if(processCookies!=null&&"true".equals(processCookies)){
	        	Cookie c_username = CookieUtils.getCookie(request, "bbs_username");
	    		Cookie c_password = CookieUtils.getCookie(request, "bbs_password");
	    		if(c_username==null||c_password==null){
	    			return null;
	    		}
	    		model.addAttribute("cookie_username", URLDecoder.decode(c_username.getValue(),"utf-8"));
	    		model.addAttribute("cookie_password", c_password.getValue());
	        }
			return FrontUtils.getTplPath(request, site.getSolutionPath(),TPLDIR_INCLUDE, TPL_SINABIAN);
		}
		
		/**
		 * 
		 * sina与账号绑定
		 */
		@RequestMapping(value = "/user_loginsinabind.jspx")
		public void user_loginsinabind(String username, String password, String captcha,
				String processUrl, String returnUrl,
				HttpServletRequest request,HttpServletResponse response) {
			AjaxResponseMessage ajaxmessage = new AjaxResponseMessage();
			//CmsSite site = CmsUtils.getSite(request);
			try {
			String ip = RequestUtils.getIpAddr(request);
			//验证码验证
			if (!imageCaptchaService.validateResponseForID(session.getSessionId(request, response), captcha)) {
				ajaxmessage.setMsg("验证码没通过验证!");
				ajaxmessage.setSuccess(false);
				ServletResponseHelper.outUTF8(response, gson.toJson(ajaxmessage));
				return;
			}
			
			System.out.println("获取sina openid:"+(String)session.getAttribute(request, "sina_openid"));
			Authentication auth = authMng.login(username, password, ip,(String)session.getAttribute(request, "sina_openid"),false,request, response, session);
			// 是否需要在这里加上登录次数的更新？按正常的方式，应该在process里面处理的，不过这里处理也没大问题。
			bbsUserMng.updateLoginInfo(auth.getUid(), ip);
			BbsUser user = bbsUserMng.findById(auth.getUid());
			if (user.getDisabled() != null && user.getDisabled()) {
				// 如果已经禁用，则推出登录。
				authMng.deleteById(auth.getId());
				session.logout(request, response);
				throw new DisabledException("user disabled");
			}
			// 登录记录
			BbsLoginLog loginLog = new BbsLoginLog();
			loginLog.setIp(RequestUtils.getIpAddr(request));
			Calendar calendar = Calendar.getInstance();
			loginLog.setLoginTime(calendar.getTime());
			loginLog.setUser(user);
			bbsLoginMng.save(loginLog);

			// 在线时长统计
			BbsUserOnline online = user.getUserOnline();
			if (online != null) {
				// 更新在线信息(这里对最后一次登陆时长做初始化，其余初始化用定时任务)
				online.setOnlineLatest(0d);
				userOnlineMng.update(online);
			} else {
				// 首次登陆
				online = new BbsUserOnline();
				online.setUser(user);
				online.initial();
				userOnlineMng.save(online);
			}
			session.setAttribute(request, response, LAST_KEEP_SESSION_TIME,calendar.getTime());
			
			ajaxmessage.setMsg(returnUrl);
			}catch (UsernameNotFoundException e) {
				ajaxmessage.setSuccess(false);
				ajaxmessage.setMsg("登录账号不正确!");
			} catch (BadCredentialsException e) {
				ajaxmessage.setSuccess(false);
				ajaxmessage.setMsg("登录密码不正确!");
			} catch (DisabledException e) {
				ajaxmessage.setSuccess(false);
				ajaxmessage.setMsg("登录账号已禁用!");
			}

			ServletResponseHelper.outUTF8(response, gson.toJson(ajaxmessage));
		}
		
		/**
		 * 
		 * qq不与账号绑定 user_loginnobind.jspx
		 */
		@RequestMapping(value = "/user_loginsinanobind.jspx")
		public void user_user_loginsinanobind(BbsUserExt userExt,String processUrl,  String returnUrl,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
			AjaxResponseMessage ajaxmessage = new AjaxResponseMessage();
			CmsSite site = CmsUtils.getSite(request);
			try {
			String ip = RequestUtils.getIpAddr(request);
			BbsUserGroup group = bbsConfigMng.findById(site.getId()).getRegisterGroup();
			Integer groupId = null;
			if (group != null) {
				groupId = group.getId();
			}
			BbsUser user = null;
			System.out.println("获取sinaopenid:"+(String)session.getAttribute(request, "sina_openid"));
			String username="tucao"+bbsUserMng.getLongUserId("userid");//DateUtils.parseDate(new Date(),"yyyyMMddhhmmss");	
			user = bbsUserMng.registerMember(username, "", "000000", ip, groupId, userExt,null,(String)session.getAttribute(request, "sina_openid"));
			bbsConfigEhCache.setBbsConfigCache(0, 0, 0, 1, user, site.getId());
			
			//ajaxmessage.setMsg(returnUrl);
			//绑定后登录刷新界面
			Authentication auth = authMng.login(username, "000000", ip,request, response, session);
			// 是否需要在这里加上登录次数的更新？按正常的方式，应该在process里面处理的，不过这里处理也没大问题。
			bbsUserMng.updateLoginInfo(auth.getUid(), ip);
			
			// 登录记录
			BbsLoginLog loginLog = new BbsLoginLog();
			loginLog.setIp(RequestUtils.getIpAddr(request));
			Calendar calendar = Calendar.getInstance();
			loginLog.setLoginTime(calendar.getTime());
			loginLog.setUser(user);
			bbsLoginMng.save(loginLog);

			// 在线时长统计
			BbsUserOnline online = user.getUserOnline();
			if (online != null) {
				// 更新在线信息(这里对最后一次登陆时长做初始化，其余初始化用定时任务)
				online.setOnlineLatest(0d);
				userOnlineMng.update(online);
			} else {
				// 首次登陆
				online = new BbsUserOnline();
				online.setUser(user);
				online.initial();
				userOnlineMng.save(online);
			}
			session.setAttribute(request, response, LAST_KEEP_SESSION_TIME,calendar.getTime());
			
			
			ajaxmessage.setMsg(returnUrl);
			}catch (Exception e) {
				ajaxmessage.setMsg("sina不与账号绑定逻辑出错!");
				ajaxmessage.setSuccess(false);
				//model.put("message", "qq不与账号绑定逻辑出错!");
				//return getView(null, site.getUrlWhole(),null);
			}   
			ServletResponseHelper.outUTF8(response, gson.toJson(ajaxmessage));
		}
		
		/**
		 * 获得地址
		 * 
		 * @param processUrl
		 * @param returnUrl
		 * @param authId
		 * @param defaultUrl
		 * @return
		 */
		private String getView(String processUrl, String returnUrl, String authId) {
			if (!StringUtils.isBlank(processUrl)) {
				StringBuilder sb = new StringBuilder("redirect:");
				sb.append(processUrl).append("?").append(AUTH_KEY).append("=")
						.append(authId);
				if (!StringUtils.isBlank(returnUrl)) {
					sb.append("&").append(RETURN_URL).append("=").append(returnUrl);
				}
				return sb.toString();
			} else if (!StringUtils.isBlank(returnUrl)) {
				StringBuilder sb = new StringBuilder("redirect:");
				sb.append(returnUrl);
				return sb.toString();
			} else {
				return null;
			}
		}
}
