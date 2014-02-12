package com.tucao.bbs.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tucao.bbs.entity.BbsConfig;
import com.tucao.bbs.entity.BbsCreditExchange;
import com.tucao.bbs.entity.BbsTopic;
import com.tucao.bbs.entity.BbsUserGroup;
import com.tucao.bbs.manager.BbsConfigMng;
import com.tucao.bbs.manager.BbsCreditExchangeMng;
import com.tucao.bbs.manager.BbsTopicMng;
import com.tucao.bbs.manager.BbsUserGroupMng;
import com.tucao.bbs.manager.StaticPageSvc;
import com.tucao.bbs.web.CmsUtils;
import com.tucao.bbs.web.WebErrors;
import com.tucao.common.web.ResponseUtils;
import com.tucao.core.entity.CmsConfig;
import com.tucao.core.entity.CmsSite;
import com.tucao.core.entity.Ftp;
import com.tucao.core.entity.Config.ConfigEmailSender;
import com.tucao.core.entity.Config.ConfigLogin;
import com.tucao.core.entity.Config.ConfigMessageTemplate;
import com.tucao.core.manager.CmsConfigMng;
import com.tucao.core.manager.CmsSiteMng;
import com.tucao.core.manager.ConfigMng;
import com.tucao.core.manager.FtpMng;

@Controller
public class CmsSiteConfigAct {
	private static final Logger log = LoggerFactory
			.getLogger(CmsSiteConfigAct.class);

	@RequestMapping("/site_config/v_system_edit.do")
	public String systemEdit(HttpServletRequest request, ModelMap model) {
		model.addAttribute("cmsConfig", cmsConfigMng.get());
		return "site_config/system_edit";
	}

	@RequestMapping("/site_config/o_system_update.do")
	public String systemUpdate(CmsConfig bean, Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateSystemUpdate(bean, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		bean = cmsConfigMng.update(bean);
		model.addAttribute("message", "global.success");
		log.info("update systemConfig of CmsConfig.");
		return systemEdit(request, model);
	}

	@RequestMapping("/site_config/v_base_edit.do")
	public String baseEdit(HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		List<Ftp> ftpList = ftpMng.getList();
		model.addAttribute("ftpList", ftpList);
		model.addAttribute("cmsSite", site);
		return "site_config/base_edit";
	}

	@RequestMapping("/site_config/o_base_update.do")
	public String baseUpdate(CmsSite bean, Integer uploadFtpId,
			HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateBaseUpdate(bean, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		CmsSite site = CmsUtils.getSite(request);
		bean.setId(site.getId());
		bean = manager.update(bean, uploadFtpId);
		model.addAttribute("message", "global.success");
		log.info("update CmsSite success. id={}", site.getId());
		return baseEdit(request, model);
	}

	@RequestMapping("/bbs_config/v_edit.do")
	public String bbsEdit(HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsConfig config = bbsConfigMng.findById(site.getId());
		List<BbsUserGroup> groupList = bbsUserGroupMng.getList(site.getId());
		model.addAttribute("config", config);
		model.addAttribute("groupList", groupList);
		return "site_config/bbs_edit";
	}

	@RequestMapping("/bbs_config/o_update.do")
	public String bbsUpdate(BbsConfig bean, Integer registerGroupId,
			Integer defaultGroupId, HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		bean.setSite(site);
		bean.setRegisterGroup(bbsUserGroupMng.findById(registerGroupId));
		bean.setDefaultGroup(bbsUserGroupMng.findById(defaultGroupId));
		bbsConfigMng.update(bean);
		return bbsEdit(request, model);
	}
	@RequestMapping("/bbs_config/v_login_edit.do")
	public String loginEdit(HttpServletRequest request, ModelMap model) {
		model.addAttribute("configLogin", configMng.getConfigLogin());
		model.addAttribute("emailSender", configMng.getEmailSender());
		model.addAttribute("forgotPasswordTemplate", configMng.getForgotPasswordMessageTemplate());
		model.addAttribute("registerTemplate", configMng.getRegisterMessageTemplate());
		return "site_config/login_edit";
	}

	@RequestMapping("/bbs_config/o_login_update.do")
	public String loginUpdate(ConfigLogin configLogin,
			ConfigEmailSender emailSender, ConfigMessageTemplate msgTpl,
			HttpServletRequest request, ModelMap model) {
		configMng.updateOrSave(configLogin.getAttr());
		configMng.updateOrSave(emailSender.getAttr());
		configMng.updateOrSave(msgTpl.getAttr());
		model.addAttribute("message", "global.success");
		log.info("update loginCoinfig of Config.");
		return loginEdit(request, model);
	}
	
	@RequestMapping("/bbs_config/v_creditExchangeEdit.do")
	public String bbsCreditExchangeEdit(HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsCreditExchange creditExchange =creditExchangeMng.findById(site.getId());
		model.addAttribute("creditExchange",creditExchange);
		return "site_config/credit_exchange_rule";
	}
	
	@RequestMapping("/bbs_config/v_topicpost.do")
	public String v_topicpost(HttpServletRequest request, ModelMap model) {
		//CmsSite site = CmsUtils.getSite(request);
		List<BbsTopic> list=bbsTopicMng.getNewList(20);
		model.addAttribute("list", list);
		
		return "site_config/topicpost";
	}
	
	@RequestMapping("/bbs_config/v_topicpostadd.do")
	public void v_topicpostadd(HttpServletRequest request,HttpServletResponse response, ModelMap model) {
		System.out.println(request.getParameter("id"));
		System.out.println(request.getParameter("comfrom"));
		BbsTopic bbsTopic=bbsTopicMng.findById(Integer.parseInt(request.getParameter("id")));
		bbsTopic.setHasTopShow(true);
		bbsTopic.setComefrom(request.getParameter("comfrom"));
		
		bbsTopicMng.update(bbsTopic);
		
		JSONObject object = new JSONObject();
		try {
			object.put("msg", "ok");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response,object.toString());
	}
	
	
	@RequestMapping("/bbs_config/v_creditExchangeUpdate.do")
	public String bbsCreditExchangeUpdate(BbsCreditExchange creditExchange ,HttpServletRequest request, ModelMap model) {
		if(creditExchange.getExchangetax()<1.0&&creditExchange.getExchangetax()>=0.0){
			creditExchangeMng.update(creditExchange);
			model.addAttribute("message", "global.success");
			log.info("update BbsCreditExchange");
		}
		return bbsCreditExchangeEdit(request, model);
	}
	
	/**
	 * 首页静态化
	 * @param request
	 * @param response
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping("/bbs_config/staticIndex.do")
	public void staticIndex(HttpServletRequest request,HttpServletResponse response, ModelMap model)throws Exception {
		CmsSite site = CmsUtils.getSite(request);
		staticPageSv.index(site, request);
		JSONObject object = new JSONObject();
		object.put("msg", "ok");
		ResponseUtils.renderJson(response,object.toString());
	}

	private WebErrors validateSystemUpdate(CmsConfig bean,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		return errors;
	}

	private WebErrors validateBaseUpdate(CmsSite bean,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		return errors;
	}

	@Autowired
	private StaticPageSvc staticPageSv;
	@Autowired
	private CmsSiteMng manager;
	@Autowired
	private FtpMng ftpMng;
	@Autowired
	private BbsConfigMng bbsConfigMng;
	@Autowired
	private BbsUserGroupMng bbsUserGroupMng;
	@Autowired
	private CmsConfigMng cmsConfigMng;
	@Autowired
	private ConfigMng configMng;
	@Autowired
	private BbsCreditExchangeMng creditExchangeMng;
	@Autowired
	private BbsTopicMng bbsTopicMng;
}