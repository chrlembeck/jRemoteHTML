package de.chrlembeck.jremotehtml.components;

import de.chrlembeck.jremotehtml.core.element.BodyTag;
import de.chrlembeck.jremotehtml.core.element.Button;
import de.chrlembeck.jremotehtml.core.element.Div;
import de.chrlembeck.jremotehtml.core.element.Page;
import de.chrlembeck.jremotehtml.core.element.Tag;
import de.chrlembeck.jremotehtml.core.element.TagFactory;

/**
 * Idee für das Verwalten und hinzufügen von Modulen:
 * <ul>
 * <li>Jedes Modul darf eine eigene javascript-datei mitbringen, die der
 * Startseite bekannt gemacht werden muss.</li>
 * <li>Es existiert eine globale Registrierungsmethode, die aufgerufen wird,
 * wenn ein DOM-Element eine bestimmte eigenschaft (classe, attribut)
 * besitzt.</li>
 * <li>Jedes Modul kann sich bei der Registrierungsmethode einklinken und eine
 * Funktion hinterlegen, die aufgerufen wird, wenn ein gekennzeichneter Knoten
 * hinzugefügt wird. Diese Methode kann dann entsprechende Listener
 * registrieren.</li>
 * <li></li>
 * </ul>
 * <a href=
 * "https://wiki.selfhtml.org/wiki/JavaScript/Organisation_von_JavaScripten">https://wiki.selfhtml.org/wiki/JavaScript/Organisation_von_JavaScripten</a>
 */
public class ModalMessage {

    public static void showInfoMessage(Page page, String headerText, String message) {
        BodyTag body = page.getBodyNode();
        body.appendElement(createModal(headerText, message));
    }

    public static Tag createModal(String header, String message) {
        Tag script = TagFactory.createTag("script").setAttribute("type", "text/javascript")
                .appendTextElement("function hover_close() {alert(\"1\");}").create();
        Tag textPar = TagFactory.createTag("p").appendTextElement(message).create();
        Tag headerText = TagFactory.createSpan(header).setClass("header-text").create();
        Tag close = TagFactory.createSpan("&times;").setClass("close-symbol")
                .addClickListener(ModalMessage::closeButtonPressed).create();
        Div clearFloat = TagFactory.createDiv().setAttribute("style", "clear: both;").create();

        Div modalHeader = TagFactory.createDiv().setClass("modal-header").appendElement(headerText).appendElement(close)
                .appendElement(clearFloat).create();
        Div modalBody = TagFactory.createDiv().appendElement(textPar).setClass("modal-body").create();
        Button button = TagFactory.createButton("Schließen", ModalMessage::closeButtonPressed).setClass("dialog-button")
                .create();
        Div modalFooter = TagFactory.createDiv().setClass("modal-footer").appendElement(button).create();

        Tag dialog = TagFactory.createTag("div").setClass("modal-dialog").appendElement(modalHeader)
                .appendElement(modalBody).appendElement(modalFooter).create();
        Tag background = TagFactory.createTag("div").setClass("modal-background").appendElement(script)
                .appendElement(dialog).create();
        return background;
    }

    public static void closeButtonPressed(Tag source) {
        Tag dialogHeader = source.getParent();
        Tag dialog = dialogHeader.getParent();
        Tag background = dialog.getParent();
        Tag body = background.getParent();
        body.removeElement(background);
    }
}