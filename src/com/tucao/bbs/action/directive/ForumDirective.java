package com.tucao.bbs.action.directive;

import static com.tucao.common.web.freemarker.DirectiveUtils.OUT_LIST;
import static freemarker.template.ObjectWrapper.DEFAULT_WRAPPER;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import com.tucao.bbs.manager.BbsForumMng;
import com.tucao.bbs.web.FrontUtils;
import com.tucao.common.web.freemarker.DirectiveUtils;
import com.tucao.core.entity.CmsSite;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class ForumDirective implements TemplateDirectiveModel {

	public static final String CATEGORY_ID = "categoryId";

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		CmsSite site = FrontUtils.getSite(env);
		Integer categoryId = DirectiveUtils.getInt(CATEGORY_ID, params);
		List forumList = manager.getList(site.getId(), categoryId);
		
		
		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(
				params);
		paramWrap.put(OUT_LIST, DEFAULT_WRAPPER.wrap(forumList));
		Map<String, TemplateModel> origMap = DirectiveUtils
				.addParamsToVariable(env, paramWrap);
		body.render(env.getOut());
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
	}

	@Autowired
	private BbsForumMng manager;
//	@Autowired
//	private BbsTopicMng topicMng;
}
