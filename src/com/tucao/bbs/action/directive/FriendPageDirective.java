package com.tucao.bbs.action.directive;

import static com.tucao.common.web.freemarker.DirectiveUtils.OUT_PAGINATION;
import static freemarker.template.ObjectWrapper.DEFAULT_WRAPPER;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.tucao.bbs.manager.BbsUserMng;
import com.tucao.bbs.web.FrontUtils;
import com.tucao.common.page.Pagination;
import com.tucao.common.web.freemarker.DirectiveUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 *
 */

public class FriendPageDirective implements TemplateDirectiveModel {
	public static final String PARAM_USERNAME = "username";

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		String username = getUsername(params);
		Pagination pagination = bbsUserMng.getPage(username, null, null, false,
				false,null,null,null,null,null, FrontUtils.getPageNo(env), FrontUtils.getCount(params));
		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(
				params);
		paramWrap.put(OUT_PAGINATION, DEFAULT_WRAPPER.wrap(pagination));
		Map<String, TemplateModel> origMap = DirectiveUtils
				.addParamsToVariable(env, paramWrap);
		body.render(env.getOut());
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
	}

	private String getUsername(Map<String, TemplateModel> params)
			throws TemplateException {
		String username = DirectiveUtils.getString(PARAM_USERNAME, params);
		return username == null ? "" : username;
	}

	@Autowired
	private BbsUserMng bbsUserMng;
}
