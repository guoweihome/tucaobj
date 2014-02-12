package com.tucao.bbs.manager;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.tucao.core.entity.CmsSite;

import freemarker.template.TemplateException;

public interface StaticPageSvc {

	public void index(CmsSite site,HttpServletRequest request) throws IOException, TemplateException;
}
