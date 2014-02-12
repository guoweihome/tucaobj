package com.tucao.bbs.json.vo;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Star
 * Date: 12-8-10
 * Time: 上午10:58
 * To change this template use File | Settings | File Templates.
 */
public class ServletResponseHelper {
    public static void outUTF8(HttpServletResponse response, String resStr) {
        Logger logger = Logger.getLogger(ServletResponseHelper.class);
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(resStr);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static void outUTF8ToXml(HttpServletResponse response, String resStr) {
        Logger logger = Logger.getLogger(ServletResponseHelper.class);
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/xml; charset=utf-8");
            response.getWriter().write(resStr);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static void outUTF8ToJson(HttpServletResponse response, String resStr) {
        Logger logger = Logger.getLogger(ServletResponseHelper.class);
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write(resStr);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static void outGBK(HttpServletResponse response, String resStr) {
        Logger logger = Logger.getLogger(ServletResponseHelper.class);
        try {
            response.setCharacterEncoding("GBK");
            response.setContentType("text/html; charset=gbk");
            response.getWriter().write(resStr);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
