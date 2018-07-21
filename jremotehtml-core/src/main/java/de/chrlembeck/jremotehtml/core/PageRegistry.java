package de.chrlembeck.jremotehtml.core;

import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class PageRegistry {

    private Map<String, PageCreator> pageCreatorMap = new TreeMap<>();

    private String defaultPageName;

    public void setDefaultPageName(String defaultPageName) {
        this.defaultPageName = defaultPageName;
    }

    public void registerCreator(String name, PageCreator creator) {
        Assert.hasLength(name, "Der Name darf nicht leer sein");
        pageCreatorMap.put(name, creator);
    }

    public String getDefaultPageName() {
        return defaultPageName;
    }

    public PageCreator getPageCreator(String pageName) {
        return pageCreatorMap.get(pageName);
    }
}