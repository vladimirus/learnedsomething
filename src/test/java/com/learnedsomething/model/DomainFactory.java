package com.learnedsomething.model;

/**
 * It is responsible to instantiate default domain class instances.
 */
public final class DomainFactory {

    private DomainFactory() {
        // Don't instantiate
    }

    public static Link aLink() {
        Link link = new Link("http://example.com");
        link.setText("Some text");
        link.setId("this-is-id");
        return link;
    }
}
