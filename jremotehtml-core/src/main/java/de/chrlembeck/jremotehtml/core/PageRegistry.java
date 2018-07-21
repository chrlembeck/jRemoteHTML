package de.chrlembeck.jremotehtml.core;

import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class PageRegistry {

	private Map<String, Page> pageMap = new TreeMap<>();

	private String defaultPageName;

	public void setDefaultPageName(String defaultPageName) {
		this.defaultPageName = defaultPageName;
	}

	public void registerPage(String name, Page page) {
		Assert.hasLength(name, "Der Name darf nicht leer sein");
		pageMap.put(name, page);
	}

	public String getDefaultPageName() {
		return defaultPageName;
	}

	public Page getPage(String pageName) {
		return pageMap.get(pageName);
	}
}