package de.chrlembeck.jremotehtml.core;

import de.chrlembeck.jremotehtml.core.element.Page;

@FunctionalInterface
public interface PageCreator {

    Page createPage(String name);
}