package com.tucao.core.template;

import com.tucao.common.developer.ModuleGenerator;

public class CoreModuleGenerator {
	private static String packName = "com.tucao.core.template";
	private static String fileName = "jeecore.properties";

	public static void main(String[] args) {
		new ModuleGenerator(packName, fileName).generate();
	}
}
