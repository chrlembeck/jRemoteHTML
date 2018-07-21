package de.chrlembeck.jremotehtml;


public class NewClickListener implements Change {

	private Tag tag;

	public NewClickListener(Tag tag) {
		this.tag = tag;
	}

	public Tag getTag() {
		return tag;
	}
}
