package com.tucao.bbs.manager.impl;

import static com.tucao.bbs.Constants.TPLDIR_HOME;
import static com.tucao.common.web.ProcessTimeFilter.START_TIME;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.tucao.bbs.entity.BbsForum;
import com.tucao.bbs.entity.BbsTopic;
import com.tucao.bbs.entity.BbsUser;
import com.tucao.bbs.manager.BbsForumMng;
import com.tucao.bbs.manager.BbsTopicMng;
import com.tucao.bbs.manager.BbsUserMng;
import com.tucao.bbs.manager.StaticPageSvc;
import com.tucao.bbs.web.FrontUtils;
import com.tucao.common.web.RequestUtils;
import com.tucao.common.web.springmvc.RealPathResolver;
import com.tucao.core.entity.CmsSite;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
@Transactional
public class StaticPageSvcImpl implements StaticPageSvc, InitializingBean{

	private Configuration conf;
	
	@Autowired
	private RealPathResolver realPathResolver;
	@Autowired
	private BbsTopicMng bbsTopicMng;
	@Autowired
	private BbsUserMng bbsUserMng;
	@Autowired
	private BbsForumMng bbsForumMng;
	
	@Transactional(readOnly = true)
	public void index(CmsSite site,HttpServletRequest request) throws IOException, TemplateException {
		Map<String, Object> data = new HashMap<String, Object>();
		FrontUtils.frontData(data, site, null,RequestUtils.getLocation(request),request.getContextPath(),(Long) request.getAttribute(START_TIME));
		
		//首页推荐
		List<BbsTopic> list=bbsTopicMng.getListTopShow(1);
		data.put("bbsContentList", list);
		//活跃大神
		List<BbsUser> bbsUserList=bbsUserMng.getHotMember(8);
		data.put("bbsUserList", bbsUserList);
		
		//热门标签
		List<BbsForum> bbsfromList= this.bbsForumMng.getList(site.getId());
		data.put("bbsfromList", bbsfromList);
		
		String tpl = FrontUtils.getTplPath(request, site.getSolutionPath(),TPLDIR_HOME, "tpl.home");
		index(site, tpl, data);
	}

	@Transactional(readOnly = true)
	public void index(CmsSite site, String tpl, Map<String, Object> data)
			throws IOException, TemplateException {
		long time = System.currentTimeMillis();
		File f = new File(getIndexPath(site));
		File parent = f.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
		Writer out = null;
		try {
			// FileWriter不能指定编码确实是个问题，只能用这个代替了。
			out = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
			Template template = conf.getTemplate(tpl);
			template.process(data, out);
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
		time = System.currentTimeMillis() - time;
	}
	
	
	private String getIndexPath(CmsSite site) {
		StringBuilder pathBuff = new StringBuilder();
		if (!site.getIndexToRoot()) {
			if (!StringUtils.isBlank(site.getStaticDir())) {
				pathBuff.append(site.getStaticDir());
			}
		}
		pathBuff.append("/").append("index").append(
				site.getStaticSuffix());
		return realPathResolver.get(pathBuff.toString());
	}

	
	

	
	public void setFreeMarkerConfigurer(
			FreeMarkerConfigurer freeMarkerConfigurer) {
		this.conf = freeMarkerConfigurer.getConfiguration();
	}

	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}
}
