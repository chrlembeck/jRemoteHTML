package de.chrlembeck.jremotehtml;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.chrlembeck.jremotehtml.core.Page;
import de.chrlembeck.jremotehtml.core.PageRegistry;
import de.chrlembeck.jremotehtml.core.Span;
import de.chrlembeck.jremotehtml.core.Tag;

@Configuration
public class PageConfiguration {

    @Bean
    public PageRegistry pageRegistry() {
        PageRegistry pageRegistry = new PageRegistry();
        pageRegistry.setDefaultPageName("default");
        pageRegistry.registerPage("default", createDefaultPage());

        return pageRegistry;
    }

    public Page createDefaultPage() {
        Page page = new Page();
        Tag body = page.getBodyNode();
        Span span = new Span("test");
        body.appendElement(span);
        body.appendElement(new Span("noch ein Test"));
        span.addClickListener(tag -> System.out.println(tag.getId()));

        return page;
    }
}