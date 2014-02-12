package com.tucao.bbs.action.front;

import static com.tucao.bbs.Constants.TPLDIR_MEMBER;
import static com.tucao.bbs.Constants.TPLDIR_USERLOGIN;
import static com.tucao.bbs.Constants.TPLDIR_INCLUDE;
import static com.tucao.core.action.front.LoginAct.MESSAGE;
import static com.tucao.core.action.front.LoginAct.PROCESS_URL;
import static com.tucao.core.action.front.LoginAct.RETURN_URL;

import static com.tucao.core.manager.AuthenticationMng.AUTH_KEY;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octo.captcha.service.image.ImageCaptchaService;
import com.tucao.bbs.entity.BbsLoginLog;
import com.tucao.bbs.entity.BbsUser;
import com.tucao.bbs.entity.BbsUserOnline;
import com.tucao.bbs.json.vo.AjaxResponseMessage;
import com.tucao.bbs.json.vo.ServletResponseHelper;
import com.tucao.bbs.manager.BbsLoginLogMng;
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
import com.tucao.core.manager.AuthenticationMng;
import com.tucao.core.web.WebErrors;

@Controller
public class CasLoginAct {

	public static final String LOGIN_INPUT = "tpl.loginInput";
	public static final String LOGIN_USERLOGIN = "tpl.userlogin";
	public static final String LOGIN_STATUS = "tpl.loginStatus";
	public static final String REGISTER_ACTIVE_SUCCESS = "tpl.registerActiveSuccess";
	public static String LAST_KEEP_SESSION_TIME = "last_keep_session_time";
	private final Gson gson = new GsonBuilder().setDateFormat(
			"yyyy-MM-dd HH:mm:ss").create();

	@RequestMapping(value = "/login_cookie.jspx")
	public String login_cookie(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		String ip = RequestUtils.getIpAddr(request);
		
		try {
			Cookie c_username = CookieUtils.getCookie(request, "bbs_username");
			Cookie c_password = CookieUtils.getCookie(request, "bbs_password");
			String username = URLDecoder.decode(c_username.getValue(),"utf-8");
			String password = c_password.getValue();
			Authentication auth = authMng.login(username, password, ip,
					request, response, session);
			// 是否需要在这里加上登录次数的更新？按正常的方式，应该在process里面处理的，不过这里处理也没大问题。//zqc ,不清楚的地方
			bbsUserMng.updateLoginInfo(auth.getUid(), ip);
			BbsUser user = bbsUserMng.findById(auth.getUid());
			if (user.getDisabled() != null && user.getDisabled()) {
				// 如果已经禁用，则推出登录。
				authMng.deleteById(auth.getId());
				session.logout(request, response);
				throw new DisabledException("user disabled");
			}
			Calendar calendar = Calendar.getInstance();
			session.setAttribute(request, response, LAST_KEEP_SESSION_TIME,
					calendar.getTime());
			
			
			String view = getView(null, site.getUrlWhole(), auth.getId());
			
			
			if (view != null) {
				session.setAttribute(request, response, "processCookies","true");
				return view;
			} else {
				FrontUtils.frontData(request, model, site);
				session.setAttribute(request, response, "processCookies",
						"false");
				return "redirect:login.jspx";
			}
		} catch (Exception e) {
			// TODO: handle exception
			FrontUtils.frontData(request, model, site);
			session.setAttribute(request, response, "processCookies", "false");
			return getView(null, site.getUrlWhole(), null);
		}
	}

	@RequestMapping(value = "/login.jspx")
	public String login(HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		String sol = site.getSolutionPath();
		model.addAttribute("site", site);
		FrontUtils.frontData(request, model, site);
		return FrontUtils.getTplPath(request, sol, TPLDIR_MEMBER, LOGIN_INPUT);
	}

	@RequestMapping(value = "/userlogin.jspx")
	public String userlogin(HttpServletRequest request, ModelMap model) {
		System.out.println("sysout ok begin");
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		
		String sol = site.getSolutionPath();
		model.addAttribute("site", site);
		
		System.out.println("sysout ok user login");
		return FrontUtils.getTplPath(request, sol, TPLDIR_INCLUDE,
				LOGIN_USERLOGIN);
	}

	@RequestMapping(value = "/loginsubmit.jspx", method = RequestMethod.POST)
	public void submitLogin(String username, String password, String captcha,
			String processUrl, String cookieType, String returnUrl,
			String message, HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		AjaxResponseMessage ajaxmessage = new AjaxResponseMessage();
		
		//System.out.println("水平:"+bbsUserMng.getLongUserId("userid"));

		try {
			String ip = RequestUtils.getIpAddr(request);
			Authentication auth = authMng.login(username, password, ip,
					request, response, session);
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
			String view = getView(processUrl, returnUrl, auth.getId());

			System.out.println("你好---------------------:" + view);
			if (view != null) {
				// 4 不保存
				// 3 保存1天
				// 2 一个月
				// 1 年
				// 0 永久
				Integer maxDate = 0;
				if ("0".equals(cookieType)) {
					maxDate = Integer.MAX_VALUE;
				} else if ("1".equals(cookieType)) {
					maxDate = 360 * 24 * 60 * 60;
				} else if ("2".equals(cookieType)) {
					maxDate = 30 * 24 * 60 * 60;
				} else if ("3".equals(cookieType)) {
					maxDate = 1 * 24 * 60 * 60;
				}
				CookieUtils.addCookie(request, response, "bbs_username",username, maxDate);
				CookieUtils.addCookie(request, response, "bbs_password",password, maxDate);

				ajaxmessage.setMsg(returnUrl);
			} else {
				ajaxmessage.setSuccess(false);
				ajaxmessage.setMsg("登录发生未知错误!");
			}
		} catch (UsernameNotFoundException e) {
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

	@RequestMapping(value = "/login.jspx", method = RequestMethod.POST)
	public String submit(String username, String password, String captcha,
			String processUrl, String cookieType, String returnUrl,
			String message, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws UnsupportedEncodingException {

		CmsSite site = CmsUtils.getSite(request);
		String sol = site.getSolutionPath();
		WebErrors errors = validateSubmit(username, password, request, captcha,response);
		if (!errors.hasErrors()) {
			try {
				String ip = RequestUtils.getIpAddr(request);
				Authentication auth = authMng.login(username, password, ip,
						request, response, session);
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
				session.setAttribute(request, response, LAST_KEEP_SESSION_TIME,
						calendar.getTime());
				String view = getView(processUrl, returnUrl, auth.getId());

				System.out.println("你好---------------------:" + view);
				if (view != null) {
					// 4 不保存
					// 3 保存1天
					// 2 一个月
					// 1 年
					// 0 永久

					Integer maxDate = 0;
					if ("0".equals(cookieType)) {
						maxDate = Integer.MAX_VALUE;
					} else if ("1".equals(cookieType)) {
						maxDate = 360 * 24 * 60 * 60;
					} else if ("2".equals(cookieType)) {
						maxDate = 30 * 24 * 60 * 60;
					} else if ("3".equals(cookieType)) {
						maxDate = 1 * 24 * 60 * 60;
					}
					CookieUtils.addCookie(request, response, "bbs_username",
							username, maxDate);
					CookieUtils.addCookie(request, response, "bbs_password",
							password, maxDate);
					return view;

				} else {
					FrontUtils.frontData(request, model, site);
					return "redirect:login.jspx";
				}
			} catch (UsernameNotFoundException e) {
				errors.addErrorString(e.getMessage());
			} catch (BadCredentialsException e) {
				errors.addErrorString(e.getMessage());
			} catch (DisabledException e) {
				errors.addErrorString(e.getMessage());
			}
		}
		errors.toModel(model);
		FrontUtils.frontData(request, model, site);
		if (!StringUtils.isBlank(processUrl)) {
			model.addAttribute(PROCESS_URL, processUrl);
		}
		if (!StringUtils.isBlank(returnUrl)) {
			model.addAttribute(RETURN_URL, returnUrl);
		}
		if (!StringUtils.isBlank(message)) {
			model.addAttribute(MESSAGE, message);
		}
		return FrontUtils.getTplPath(request, sol, TPLDIR_MEMBER, LOGIN_INPUT);
	}

	@RequestMapping(value = "/logout.jspx")
	public String logout(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws UnsupportedEncodingException {
		String authId = (String) session.getAttribute(request, AUTH_KEY);
		BbsUser user = CmsUtils.getUser(request);
		Calendar calendar = Calendar.getInstance();
		BbsLoginLog log = user.getUserLaestLoginLog();
		// 后台管理员登陆没有记录登陆记录
		if (log != null) {
			log.setLogoutTime(new Date());
			bbsLoginMng.update(log);
		}
		// 在线时长
		BbsUserOnline online = user.getUserOnline();
		Date lastKeepSessionTime = (Date) session.getAttribute(request,LAST_KEEP_SESSION_TIME);
		online.updateOnline(DateUtils.diffTwoDate(lastKeepSessionTime, calendar.getTime()));
		userOnlineMng.update(online);

		if (authId != null) {
			authMng.deleteById(authId);
			session.logout(request, response);
		}
		
		//System.out.println("退出测试："+request.getRequestURI()+"---"+request.getRequestURL());
		String processUrl = RequestUtils.getQueryParam(request, PROCESS_URL);
		String returnUrl = RequestUtils.getQueryParam(request, RETURN_URL);
		String view = getView(processUrl, returnUrl, authId);
		session.setAttribute(request, response, "processCookies", "false");
		if (view != null) {
			CookieUtils.addCookie(request, response, "bbs_username", null,
					Integer.MAX_VALUE);
			CookieUtils.addCookie(request, response, "bbs_password", null,
					Integer.MAX_VALUE);
			return view;
		} else {
			return "redirect:login.jspx";
		}
	}

	private WebErrors validateSubmit(String username, String password,
			HttpServletRequest request, String captcha,
			HttpServletResponse response) {
		WebErrors errors = WebErrors.create(request);
		if (!imageCaptchaService.validateResponseForID(session.getSessionId(
				request, response), captcha)) {
			errors.addErrorCode("error.invalidCaptcha");
			return errors;
		}

		if (errors.ifOutOfLength(username, "username", 1, 100)) {
			return errors;
		}
		if (errors.ifOutOfLength(password, "password", 1, 32)) {
			return errors;
		}
		return errors;
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

	@Autowired
	private BbsUserMng bbsUserMng;
	@Autowired
	private AuthenticationMng authMng;
	@Autowired
	private SessionProvider session;
	@Autowired
	private ImageCaptchaService imageCaptchaService;
	@Autowired
	private BbsLoginLogMng bbsLoginMng;
	@Autowired
	private BbsUserOnlineMng userOnlineMng;
}
