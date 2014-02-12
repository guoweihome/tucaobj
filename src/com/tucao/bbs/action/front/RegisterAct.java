package com.tucao.bbs.action.front;

import static com.tucao.bbs.Constants.TPLDIR_MEMBER;
import static com.tucao.bbs.Constants.TPLDIR_USERLOGIN;
import static com.tucao.bbs.Constants.TPLDIR_INCLUDE;
import java.io.IOException;
import java.util.Calendar;

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
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.ImageCaptchaService;
import com.tucao.bbs.cache.BbsConfigEhCache;
import com.tucao.bbs.entity.BbsConfig;
import com.tucao.bbs.entity.BbsLoginLog;
import com.tucao.bbs.entity.BbsUser;
import com.tucao.bbs.entity.BbsUserExt;
import com.tucao.bbs.entity.BbsUserGroup;
import com.tucao.bbs.entity.BbsUserOnline;
import com.tucao.bbs.json.vo.AjaxResponseMessage;
import com.tucao.bbs.json.vo.ServletResponseHelper;
import com.tucao.bbs.manager.BbsConfigMng;
import com.tucao.bbs.manager.BbsLoginLogMng;
import com.tucao.bbs.manager.BbsUserMng;
import com.tucao.bbs.manager.BbsUserOnlineMng;
import com.tucao.bbs.web.CmsUtils;
import com.tucao.bbs.web.FrontUtils;
import com.tucao.bbs.web.WebErrors;
import com.tucao.common.email.EmailSender;
import com.tucao.common.email.MessageTemplate;
import com.tucao.common.security.BadCredentialsException;
import com.tucao.common.security.UsernameNotFoundException;
import com.tucao.common.web.RequestUtils;
import com.tucao.common.web.ResponseUtils;
import com.tucao.common.web.session.SessionProvider;
import com.tucao.core.entity.Authentication;
import com.tucao.core.entity.CmsSite;
import com.tucao.core.entity.UnifiedUser;
import com.tucao.core.manager.AuthenticationMng;
import com.tucao.core.manager.ConfigMng;
import com.tucao.core.manager.UnifiedUserMng;

/**
 * 前台会员注册Action
 * 
 * @author liqiang
 * 
 */
@Controller
public class RegisterAct {
	private static final Logger log = LoggerFactory.getLogger(RegisterAct.class);
	private static String LAST_KEEP_SESSION_TIME = "last_keep_session_time";
	public static final String REGISTER = "tpl.register";
	public static final String USER_REGISTER = "tpl.userregister";
	public static final String REGISTER_RESULT = "tpl.registerResult";
	public static final String REGISTER_ACTIVE_SUCCESS = "tpl.registerActiveSuccess";
	public static final String LOGIN_INPUT = "tpl.loginInput";
    private final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();


	@RequestMapping(value = "/register.jspx", method = RequestMethod.GET)
	public String input(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),TPLDIR_MEMBER, REGISTER);
	}
	
	/**
	 * zqc新增,用户注册
	 * @param username
	 * @throws IOException
	 */
	@RequestMapping(value = "/user_register.jspx", method = RequestMethod.GET)
	public String user_register(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),TPLDIR_INCLUDE, USER_REGISTER);
	}
	

	@RequestMapping(value = "/user_register.jspx", method = RequestMethod.POST)
	public void user_submit(String username, String email, String password,
			BbsUserExt userExt, String captcha, String nextUrl,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws IOException, UsernameNotFoundException, BadCredentialsException {
		AjaxResponseMessage ajaxmessage = new AjaxResponseMessage();
		CmsSite site = CmsUtils.getSite(request);
		BbsConfig config = bbsConfigMng.findById(site.getId());
		//验证码验证
		try{
			if (!imageCaptchaService.validateResponseForID(session.getSessionId(request, response), captcha)) {
				ajaxmessage.setMsg("验证码没通过验证!");
				ajaxmessage.setSuccess(false);
				ServletResponseHelper.outUTF8(response, gson.toJson(ajaxmessage));
				return;
			}
		}catch(CaptchaServiceException capt){
			 ajaxmessage.setMsg("请刷新验证码再提交!");
			 ajaxmessage.setSuccess(false);
			 ServletResponseHelper.outUTF8(response, gson.toJson(ajaxmessage));
			 return;
		}
		
		
		//验证用户名是否存在
		// 用户名存在，返回false。
		if (unifiedUserMng.usernameExist(username)) {
			ajaxmessage.setMsg("用户名用户已经存在!");
			ajaxmessage.setSuccess(false);
			ServletResponseHelper.outUTF8(response, gson.toJson(ajaxmessage));
			return;
		}
		
		String ip = RequestUtils.getIpAddr(request);
		Integer groupId = null;
		BbsUserGroup group = bbsConfigMng.findById(site.getId()).getRegisterGroup();
		if (group != null) {
			groupId = group.getId();
		}
		BbsUser user = null;
		if(config.getEmailValidate()){
			EmailSender sender = configMng.getEmailSender();
			MessageTemplate msgTpl = configMng.getRegisterMessageTemplate();
			if (sender == null) {
				// 邮件服务器没有设置好
				ajaxmessage.setMsg("邮件服务器没有设置好!");
				ajaxmessage.setSuccess(false);
				//model.addAttribute("status", 4);
			} else if (msgTpl == null) {
				// 邮件模板没有设置好
				ajaxmessage.setMsg("邮件模板没有设置好!");
				ajaxmessage.setSuccess(false);
				//model.addAttribute("status", 5);
			} else {
				try {
					user = bbsUserMng.registerMember(username, email, password, ip,groupId, userExt, false, sender, msgTpl,null,null);
					bbsConfigEhCache.setBbsConfigCache(0, 0, 0, 1, user, site.getId());
					//model.addAttribute("status", 0);
				} catch (Exception e) {
					// 发送邮件异常
					ajaxmessage.setMsg("发送邮件异常!");
					ajaxmessage.setSuccess(false);
					//model.addAttribute("status", 100);
					//model.addAttribute("message", e.getMessage());
					log.error("send email exception.", e);
				}
			}
			log.info("member register success. username={}", username);
			if (!StringUtils.isBlank(nextUrl)) {
				response.sendRedirect(nextUrl);
			} 
		}else{ 
			user = bbsUserMng.registerMember(username, email, password, ip, groupId, userExt,null,null);
			bbsConfigEhCache.setBbsConfigCache(0, 0, 0, 1, user, site.getId());
			ajaxmessage.setMsg("0");//不需要邮箱验证
			//String ip = RequestUtils.getIpAddr(request);
			Authentication auth = authMng.login(username, password, ip,request, response, session);
			// 是否需要在这里加上登录次数的更新？按正常的方式，应该在process里面处理的，不过这里处理也没大问题。
			bbsUserMng.updateLoginInfo(auth.getUid(), ip);
			
			// 登录记录
			BbsLoginLog loginLog = new BbsLoginLog();
			loginLog.setIp(RequestUtils.getIpAddr(request));
			Calendar calendar = Calendar.getInstance();
			loginLog.setLoginTime(calendar.getTime());
			loginLog.setUser(user);
			
			bbsLoginMng.save(loginLog);
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
			session.setAttribute(request, response, LAST_KEEP_SESSION_TIME, calendar.getTime());
			
			//如果不发邮件，则直接登录
			log.info("member register success. username={}", username);
		}
		
		ServletResponseHelper.outUTF8(response, gson.toJson(ajaxmessage));
	}
	
	@RequestMapping(value = "/register.jspx", method = RequestMethod.POST)
	public String submit(String username, String email, String password,
			BbsUserExt userExt, String captcha, String nextUrl,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws IOException {
		CmsSite site = CmsUtils.getSite(request);
		BbsConfig config = bbsConfigMng.findById(site.getId());
		WebErrors errors = validateSubmit(username, email, password, captcha,site, request, response);
		if (errors.hasErrors()) {
			return FrontUtils.showError(request, response, model, errors);
		}
		String ip = RequestUtils.getIpAddr(request);
		Integer groupId = null;
		BbsUserGroup group = bbsConfigMng.findById(site.getId()).getRegisterGroup();
		if (group != null) {
			groupId = group.getId();
		}
		BbsUser user = null;
		if(config.getEmailValidate()){
			EmailSender sender = configMng.getEmailSender();
			MessageTemplate msgTpl = configMng.getRegisterMessageTemplate();
			if (sender == null) {
				// 邮件服务器没有设置好
				model.addAttribute("status", 4);
			} else if (msgTpl == null) {
				// 邮件模板没有设置好
				model.addAttribute("status", 5);
			} else {
				try {
					user = bbsUserMng.registerMember(username, email, password, ip,groupId, userExt, false, sender, msgTpl,null,null);
					bbsConfigEhCache.setBbsConfigCache(0, 0, 0, 1, user, site.getId());
					model.addAttribute("status", 0);
				} catch (Exception e) {
					// 发送邮件异常
					model.addAttribute("status", 100);
					model.addAttribute("message", e.getMessage());
					log.error("send email exception.", e);
				}
			}
			log.info("member register success. username={}", username);
			if (!StringUtils.isBlank(nextUrl)) {
				response.sendRedirect(nextUrl);
				return null;
			} else {
				FrontUtils.frontData(request, model, site);
				FrontUtils.frontPageData(request, model);
				return FrontUtils.getTplPath(request, site.getSolutionPath(),TPLDIR_MEMBER, REGISTER_RESULT);
			}
		}else{ 
			user = bbsUserMng.registerMember(username, email, password, ip, groupId, userExt,null,null);
			bbsConfigEhCache.setBbsConfigCache(0, 0, 0, 1, user, site.getId());
			log.info("member register success. username={}", username);
			FrontUtils.frontData(request, model, site);
			FrontUtils.frontPageData(request, model);
			model.addAttribute("success",true);
			return FrontUtils.getTplPath(request, site.getSolutionPath(),TPLDIR_MEMBER, LOGIN_INPUT);
		}
		
		/*
		 * BbsUser user = bbsUserMng.registerMember(username, email, password,
		 * ip, groupId, userExt);
		 */
	}

	// 激活账号
	@RequestMapping(value = "/active.jspx", method = RequestMethod.GET)
	public String active(String username, String key,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws IOException {
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors = validateActive(username, key, request, response);
		if (errors.hasErrors()) {
			return FrontUtils.showError(request, response, model, errors);
		}
		UnifiedUser user = unifiedUserMng.active(username, key);
		BbsUser bbsUser = bbsUserMng.findById(user.getId());
		String ip = RequestUtils.getIpAddr(request);
		authMng.activeLogin(user, ip, request, response, session);
		// 登录记录
		BbsLoginLog loginLog = new BbsLoginLog();
		loginLog.setIp(RequestUtils.getIpAddr(request));
		Calendar calendar = Calendar.getInstance();
		loginLog.setLoginTime(calendar.getTime());
		loginLog.setUser(bbsUser);
		bbsLoginMng.save(loginLog);
		// 在线时长统计
		BbsUserOnline online = bbsUser.getUserOnline();
		// 首次登陆
		online = new BbsUserOnline();
		online.setUser(bbsUser);
		online.initial();
		userOnlineMng.save(online);
		
		session.setAttribute(request, response, CasLoginAct.LAST_KEEP_SESSION_TIME, Calendar.getInstance().getTime());
		
		FrontUtils.frontData(request, model, site);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),TPLDIR_MEMBER, REGISTER_ACTIVE_SUCCESS);
	}

	@RequestMapping(value = "/username_unique.jspx")
	public void usernameUnique(HttpServletRequest request,
			HttpServletResponse response) {
		String username = RequestUtils.getQueryParam(request, "username");
		// 用户名为空，返回false。
		if (StringUtils.isBlank(username)) {
			ResponseUtils.renderText(response, "false");
			return;
		}
		// 用户名存在，返回false。
		if (unifiedUserMng.usernameExist(username)) {
			ResponseUtils.renderText(response, "用户已存在");
			return;
		}
		ResponseUtils.renderText(response, "可以使用");
	}

	@RequestMapping(value = "/email_unique.jspx")
	public void emailUnique(HttpServletRequest request,
			HttpServletResponse response) {
		String email = RequestUtils.getQueryParam(request, "email");
		// email为空，返回false。
		if (StringUtils.isBlank(email)) {
			ResponseUtils.renderJson(response, "false");
			return;
		}
		// email存在，返回false。
		if (unifiedUserMng.emailExist(email)) {
			ResponseUtils.renderJson(response, "false");
			return;
		}
		ResponseUtils.renderJson(response, "true");
	}

	private WebErrors validateSubmit(String username, String email,
			String password, String captcha, CmsSite site,
			HttpServletRequest request, HttpServletResponse response) {
		WebErrors errors = WebErrors.create(request);
		try {
			if (!imageCaptchaService.validateResponseForID(session.getSessionId(request, response), captcha)) {
				errors.addErrorCode("error.invalidCaptcha");
				return errors;
			}
		} catch (CaptchaServiceException e) {
			errors.addErrorCode("error.exceptionCaptcha");
			log.warn("", e);
			return errors;
		}
		if (errors.ifMaxLength(email, "email", 100)) {
			return errors;
		}
		// 用户名存在，返回false。
		if (unifiedUserMng.usernameExist(username)) {
			errors.addErrorCode("error.usernameExist");
			return errors;
		}
		return errors;
	}

	private WebErrors validateActive(String username, String activationCode,
			HttpServletRequest request, HttpServletResponse response) {
		WebErrors errors = WebErrors.create(request);
		if (StringUtils.isBlank(username)
				|| StringUtils.isBlank(activationCode)) {
			errors.addErrorCode("error.exceptionParams");
			return errors;
		}
		UnifiedUser user = unifiedUserMng.getByUsername(username);
		if (user == null) {
			errors.addErrorCode("error.usernameNotExist");
			return errors;
		}
		if (user.getActivation()
				|| StringUtils.isBlank(user.getActivationCode())) {
			errors.addErrorCode("error.usernameActivated");
			return errors;
		}
		if (!user.getActivationCode().equals(activationCode)) {
			errors.addErrorCode("error.exceptionActivationCode");
			return errors;
		}
		return errors;
	}

	@Autowired
	private UnifiedUserMng unifiedUserMng;
	@Autowired
	private BbsUserMng bbsUserMng;
	@Autowired
	private SessionProvider session;
	@Autowired
	private BbsConfigMng bbsConfigMng;
	@Autowired
	private BbsConfigEhCache bbsConfigEhCache;
	@Autowired
	private ImageCaptchaService imageCaptchaService;
	@Autowired
	private ConfigMng configMng;
	@Autowired
	private AuthenticationMng authMng;
	@Autowired
	private BbsLoginLogMng bbsLoginMng;
	@Autowired
	private BbsUserOnlineMng userOnlineMng;

}
