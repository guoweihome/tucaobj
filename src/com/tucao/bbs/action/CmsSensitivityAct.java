package com.tucao.bbs.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tucao.bbs.entity.CmsSensitivity;
import com.tucao.bbs.manager.CmsSensitivityMng;
import com.tucao.bbs.web.CmsUtils;
import com.tucao.bbs.web.WebErrors;

@Controller
public class CmsSensitivityAct {
	private static final Logger log = LoggerFactory
			.getLogger(CmsSensitivityAct.class);

	@RequestMapping("/sensitivity/v_list.do")
	public String list(HttpServletRequest request, ModelMap model) {
		Integer siteId = CmsUtils.getSiteId(request);
		List<CmsSensitivity> list = manager.getList(siteId, false);
		model.addAttribute("list", list);
		return "sensitivity/list";
	}

	@RequestMapping("/sensitivity/o_save.do")
	public String save(CmsSensitivity bean, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateSave(bean, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		Integer siteId = CmsUtils.getSiteId(request);
		bean = manager.save(bean, siteId);
		model.addAttribute("message", "global.success");
		log.info("save CmsSensitivity id={}", bean.getId());
		return list(request, model);
	}

	@RequestMapping("/sensitivity/o_update.do")
	public String update(Integer[] id, String[] search, String[] replacement,
			HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateUpdate(id, search, replacement, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		manager.updateEnsitivity(id, search, replacement);
		model.addAttribute("message", "global.success");
		log.info("update CmsSensitivity.");
		return list(request, model);
	}

	@RequestMapping("/sensitivity/o_delete.do")
	public String delete(Integer[] ids, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		CmsSensitivity[] beans = manager.deleteByIds(ids);
		for (CmsSensitivity bean : beans) {
			log.info("delete CmsSensitivity id={}", bean.getId());
		}
		model.addAttribute("message", "global.success");
		return list(request, model);
	}

	private WebErrors validateSave(CmsSensitivity bean,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		return errors;
	}

	private WebErrors validateUpdate(Integer[] ids, String[] searchs,
			String[] replacements, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (errors.ifEmpty(ids, "id")) {
			return errors;
		}
		if (errors.ifEmpty(searchs, "name")) {
			return errors;
		}
		if (errors.ifEmpty(replacements, "url")) {
			return errors;
		}
		if (ids.length != searchs.length || ids.length != replacements.length) {
			errors.addErrorString("id, searchs, replacements length"
					+ " not equals");
			return errors;
		}
		for (Integer id : ids) {
			vldExist(id, errors);
			return errors;
		}
		return errors;
	}

	private WebErrors validateDelete(Integer[] ids, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (errors.ifEmpty(ids, "ids")) {
			return errors;
		}
		for (Integer id : ids) {
			vldExist(id, errors);
		}
		return errors;
	}

	private boolean vldExist(Integer id, WebErrors errors) {
		if (errors.ifNull(id, "id")) {
			return true;
		}
		CmsSensitivity entity = manager.findById(id);
		if (errors.ifNotExist(entity, CmsSensitivity.class, id)) {
			return true;
		}
		return false;
	}

	@Autowired
	private CmsSensitivityMng manager;
}