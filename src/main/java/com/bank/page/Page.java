package com.bank.page;

import java.util.ArrayList;
import java.util.List;

public class Page {

	private static List<String> beforeLoginUrls;
	private static List<String> afterLoginUrls;
	private static List<String> defaultUrls;

	static {
		beforeLoginUrls = new ArrayList<>();
		beforeLoginUrls.add("/");
		beforeLoginUrls.add("/login");
		beforeLoginUrls.add("/registration/**");
		beforeLoginUrls.add("/activation/**");

		afterLoginUrls = new ArrayList<>();
		afterLoginUrls.add("/index");
		afterLoginUrls.add("/transfer/**");
		afterLoginUrls.add("/transferhistory");
		afterLoginUrls.add("/billregistration/**");

		defaultUrls = new ArrayList<>();
		defaultUrls.add("/error");
	}

	public static String[] getBeforeLoginUrls() {

		String url;
		String[] result = new String[beforeLoginUrls.size()];
		for (int i = 0; i < result.length; i++) {
			url = beforeLoginUrls.get(i);
			result[i] = url;
		}
		return result;
	}

	public static String[] getAfterLoginUrls() {

		String url;
		String[] result = new String[afterLoginUrls.size()];
		for (int i = 0; i < result.length; i++) {
			url = afterLoginUrls.get(i);
			result[i] = url;
		}
		return result;
	}

	public static String[] getDefaultUrls() {

		String url;
		String[] result = new String[defaultUrls.size()];
		for (int i = 0; i < result.length; i++) {
			url = defaultUrls.get(i);
			result[i] = url;
		}
		return result;
	}

}
