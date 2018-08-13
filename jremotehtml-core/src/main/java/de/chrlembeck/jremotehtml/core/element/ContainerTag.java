package de.chrlembeck.jremotehtml.core.element;


public abstract class ContainerTag extends HTMLElement {

	private static final long serialVersionUID = -3223125142431237486L;

	public ContainerTag(String name) {
		super(name);
	}

	public void insertElement(int index, HTMLDomNode element) {
		// TODO Auto-generated method stub
		super.insertElement(index, element);
	}
	
	public void appendElement(HTMLDomNode element) {
		super.appendElement(element);
	}
	
	public void appendTextElement(String text) {
		super.appendTextElement(text);
	}
}
