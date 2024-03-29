package com.tucao.bbs.action.directive;

import static com.tucao.common.web.freemarker.DirectiveUtils.OUT_PAGINATION;
import static freemarker.template.ObjectWrapper.DEFAULT_WRAPPER;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.tucao.bbs.action.directive.abs.AbstractTopicPageDirective;
import com.tucao.bbs.web.FrontUtils;
import com.tucao.common.page.Pagination;
import com.tucao.common.web.freemarker.DirectiveUtils;
import com.tucao.common.web.freemarker.DirectiveUtils.InvokeType;
import com.tucao.core.entity.CmsSite;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class MyReplyPageDirective extends AbstractTopicPageDirective {

	/**
	 * 我的回复
	 */
	public static final String TPL_MY_POST = "mypost_page";

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		CmsSite site = FrontUtils.getSite(env);
		InvokeType type = DirectiveUtils.getInvokeType(params);
		Pagination page = bbsTopicMng.getMemberReply(site.getId(),
				getCreaterId(params), FrontUtils.getPageNo(env), FrontUtils
						.getCount(params));
		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(
				params);
		paramWrap.put(OUT_PAGINATION, DEFAULT_WRAPPER.wrap(page));
		Map<String, TemplateModel> origMap = DirectiveUtils
				.addParamsToVariable(env, paramWrap);
		if (InvokeType.custom == type) {
			FrontUtils.includeTpl(TPL_MY_POST, site, params, env);
			FrontUtils.includePagination(site, params, env);
		} else if (InvokeType.body == type) {
			body.render(env.getOut());
			FrontUtils.includePagination(site, params, env);
		} else {
			throw new RuntimeException("invoke type not handled: " + type);
		}
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
	}

}
