package de.chrlembeck.jremotehtml.components;

import de.chrlembeck.jremotehtml.core.element.BodyTag;
import de.chrlembeck.jremotehtml.core.element.Page;
import de.chrlembeck.jremotehtml.core.element.Span;
import de.chrlembeck.jremotehtml.core.element.Tag;

public class ModalMessage {

    public static void showInfoMessage(Page page, String message) {
        BodyTag body = page.getBodyNode();
        body.appendElement(createModal(message));

    }

    public static Tag createModal(String message) {
        Tag background = new Tag("div");
        Tag script = new Tag("script");
        script.setAttribute("type", "text/javascript");
        script.appendTextElement("function hover_close() {alert(\"1\");}");
        // event.target.style.color='#000';
        // event.target.style.text-decoration='none';
        // event.target.style.cursor='pointer';
        background.appendElement(script);

        setBackgroundStyle(background);
        Tag dialog = new Tag("div");
        setDialogStyle(dialog);
        Span close = new Span("&times;");
        setCloseStyle(close);
        Tag text = new Tag("p");
        text.appendTextElement(message);
        dialog.appendElement(close);
        dialog.appendElement(text);
        close.addClickListener(ModalMessage::closeButtonPressed);

        background.appendElement(dialog);
        return background;
    }

    private static void setCloseStyle(Span close) {
        close.setStyleAttribute("color", "#aaa");
        close.setStyleAttribute("float", "right");
        close.setStyleAttribute("font-size", "28px");
        close.setStyleAttribute("font-weight", "bold");
        close.setAttribute("onmouseenter", "hover_close()");

        // close.setAttribute("onmouseenter", "{alert('1'); alert('2');}");
    }

    public static void closeButtonPressed(Tag source) {
        Tag dialog = source.getParent();
        Tag background = dialog.getParent();
        Tag body = background.getParent();
        body.removeElement(background);
    }

    public static void setBackgroundStyle(Tag background) {
        background.setStyleAttribute("display", "block");
        background.setStyleAttribute("position", "fixed");
        background.setStyleAttribute("z-index", "1");
        background.setStyleAttribute("padding-top", "100px");
        background.setStyleAttribute("left", "0");
        background.setStyleAttribute("top", "0");
        background.setStyleAttribute("width", "100%");
        background.setStyleAttribute("height", "100%");
        background.setStyleAttribute("overflow", "auto");
        background.setStyleAttribute("background-color", "rgb(0,0,0)");
        background.setStyleAttribute("background-color", "rgba(0,0,0,0.4)");
    }

    public static void setDialogStyle(Tag dialog) {
        dialog.setStyleAttribute("background-color", "#fefefe");
        dialog.setStyleAttribute("margin", "auto");
        dialog.setStyleAttribute("padding", "20px");
        dialog.setStyleAttribute("border", "1px solid #888");
        dialog.setStyleAttribute("width", "80%");
    }
}