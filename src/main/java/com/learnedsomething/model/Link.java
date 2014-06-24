package com.learnedsomething.model;

/**
 * Main Link object.
 */
public class Link {
    private String id;
    private String uri;
    private String text;
    private boolean broadcasted;

    public Link() {
    }

    public Link(String uri) {
        this.uri = uri;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isBroadcasted() {
        return broadcasted;
    }

    public void setBroadcasted(boolean broadcasted) {
        this.broadcasted = broadcasted;
    }

    @Override
    public String toString() {
        return "Link{"
                + "id='" + id + '\''
                + ", uri='" + uri + '\''
                + ", text='" + text + '\''
                + ", broadcasted=" + broadcasted
                + '}';
    }
}
