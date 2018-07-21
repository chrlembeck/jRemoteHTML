package de.chrlembeck.jremotehtml.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import de.chrlembeck.jremotehtml.core.element.Page;
import de.chrlembeck.jremotehtml.core.element.Tag;

@WebServlet(urlPatterns = "/javahtml/*")
@Component
public class PageDispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 5454395065700298914L;

    private static Logger LOGGER = LoggerFactory.getLogger(PageDispatcherServlet.class);

    @Autowired
    private PageRegistry pageRegistry;

    public PageDispatcherServlet() {
        System.err.println("new servlet");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pageName = req.getPathInfo();
        if (pageName != null && pageName.length() > 0) {
            pageName = pageName.substring(1);
        }

        if (pageName == null || pageName.length() == 0) {
            pageName = pageRegistry.getDefaultPageName();
            if (pageName == null || pageName.length() == 0) {
                resp.sendError(HttpStatus.BAD_REQUEST.value(),
                        "Kein pageName angegeben und kein defaultPageName hinterlegt.");
                return;
            }
        }
        LOGGER.debug("pageName=" + pageName);

        final Page page = pageRegistry.getPage(pageName);
        if (page == null) {
            resp.sendError(HttpStatus.NOT_FOUND.value(), "Keine Page mit dem Namen " + pageName + " gefunden.");
            return;
        }
        HttpSession session = req.getSession();
        LOGGER.debug("setting currentPage to session. " + session.getId() + " - " + page);
        session.setAttribute("currentPage", page);
        resp.setContentType(MediaType.TEXT_HTML_VALUE);
        resp.setStatus(HttpStatus.OK.value());
        PrintWriter writer = resp.getWriter();
        page.render(writer);
        writer.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        org.springframework.boot.json.JsonParser parser = JsonParserFactory.getJsonParser();
        String json = readText(req.getInputStream(), req.getCharacterEncoding());
        Map<String, Object> requestMap = parser.parseMap(json);
        String action = (String) requestMap.get("action");
        switch (action) {
        case "loadContent":
            loadContent(req, resp, requestMap);
            return;
        case "elementClicked":
            elementClicked(req, resp, requestMap);
            return;
        default:
            resp.sendError(HttpStatus.BAD_REQUEST.value(), "Unbekannte action angegeben: " + action);
            return;
        }
    }

    private void elementClicked(HttpServletRequest req, HttpServletResponse resp, Map<String, Object> requestMap)
            throws IOException {
        HttpSession session = req.getSession();
        Page page = (Page) session.getAttribute("currentPage");
        LOGGER.debug("Page read from session: " + session + " - " + page);
        Integer elementId = (Integer) requestMap.get("elementId");
        Tag tag = page.getTagById(elementId);
        tag.fireElementClicked();
        page.sendChanges(resp);
    }

    private void loadContent(HttpServletRequest req, HttpServletResponse resp, Map<String, Object> requestMap)
            throws IOException {
        HttpSession session = req.getSession();
        Page page = (Page) session.getAttribute("currentPage");
        LOGGER.debug("Page read from session: " + session + " - " + page);

        page.sendChanges(resp);
    }

    private String readText(InputStream input, String encoding) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (InputStreamReader reader = new InputStreamReader(input, encoding)) {
            char[] buffer = new char[2048];
            int length;
            while ((length = reader.read(buffer)) != -1) {
                sb.append(buffer, 0, length);
            }
        }
        return sb.toString();
    }

    public void printDebugInfos(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        resp.setContentType(MediaType.TEXT_PLAIN_VALUE);
        writer.write("pathInfo=" + req.getPathInfo());
        writer.write("\nrequestURI=" + req.getRequestURI());
        writer.write("\nrequestURL=" + req.getRequestURL());
        writer.write("\ncontextPath=" + req.getContextPath());
        writer.write("\nqueryString=" + req.getQueryString());
        writer.write("\nservletPath=" + req.getServletPath());
        writer.flush();
    }
}