package com.tucao.bbs;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

public class InstallServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String dbHost = request.getParameter("dbHost");
		String dbPort = request.getParameter("dbPort");
		String dbName = request.getParameter("dbName");
		String dbUser = request.getParameter("dbUser");
		String dbPassword = request.getParameter("dbPassword");

		String isCreateDb = request.getParameter("isCreateDb");
		String isCreateTable = request.getParameter("isCreateTable");
		String isInitData = request.getParameter("isInitData");
		String domain = request.getParameter("domain");
		String cxtPath = request.getParameter("cxtPath");
		String port = request.getParameter("port");

		String dbFileName = request.getParameter("dbFileName");
		String initFileName = request.getParameter("initFileName");
		String dbXmlFileName = "/WEB-INF/config/jdbc.properties";
		String webXmlFrom = "/install/config/web.xml";
		String webXmlTo = "/WEB-INF/web.xml";
		String install="/install";
		// 创建数据库
		try {
			if ("true".equals(isCreateDb)) {
				Install.createDb(dbHost, dbPort, dbName, dbUser, dbPassword);
			} else {
				Install.changeDbCharset(dbHost, dbPort, dbName, dbUser,
						dbPassword);
			}
			// 创建表
			if ("true".equals(isCreateTable)) {
				String sqlPath = getServletContext().getRealPath(dbFileName);
				List<String> sqlList = Install.readSql(sqlPath);
				Install.createTable(dbHost, dbPort, dbName, dbUser, dbPassword,
						sqlList);
			}
			// 初始化数据
			if ("true".equals(isInitData)) {
				String initPath = getServletContext().getRealPath(initFileName);
				List<String> initList = Install.readSql(initPath);
				Install.createTable(dbHost, dbPort, dbName, dbUser, dbPassword,
						initList);
			}
			// 更新配置
			Install.updateConfig(dbHost, dbPort, dbName, dbUser, dbPassword,
					domain, cxtPath, port);
			// 处理数据库配置文件
			String dbXmlPath = getServletContext().getRealPath(dbXmlFileName);
			Install
					.dbXml(dbXmlPath, dbHost, dbPort, dbName, dbUser,
							dbPassword);
			// 处理web.xml
			String webXmlFromPath = getServletContext().getRealPath(webXmlFrom);
			String webXmlToPath = getServletContext().getRealPath(webXmlTo);
			Install.webXml(webXmlFromPath, webXmlToPath);
			//处理安装程序目录
			File installDirectory=new File(getServletContext().getRealPath(install));
			FileUtils.deleteDirectory(installDirectory);
		} catch (Exception e) {
			throw new ServletException("install failed!", e);
		}
		RequestDispatcher dispatcher = request
				.getRequestDispatcher("/install_setup.html");
		dispatcher.forward(request, response);
	}
}
