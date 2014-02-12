package com.tucao.core.web;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;

public class MagicMessage extends com.tucao.common.web.springmvc.MagicMessage {

	/**
	 * 通过HttpServletRequest创建MagicMessage
	 * 
	 * @param request
	 *            从request中获得MessageSource和Locale，如果存在的话。
	 * @return 如果LocaleResolver存在则返回国际化MagicMessage
	 */
	public static MagicMessage create(HttpServletRequest request) {
		return new MagicMessage(request);
	}

	public MagicMessage() {
	}

	public MagicMessage(HttpServletRequest request) {
		super(request);
	}

	/**
	 * 构造器
	 * 
	 * @param messageSource
	 * @param locale
	 */
	public MagicMessage(MessageSource messageSource, Locale locale) {
		super(messageSource, locale);
	}
}
