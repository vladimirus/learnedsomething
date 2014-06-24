package com.learnedsomething.biz.manager.publisher;

import com.learnedsomething.dao.browser.WebBrowser;
import com.learnedsomething.model.Link;

/**
 * Publisher.
 */
public interface Publisher {
    void publish(Link link, WebBrowser browser) throws Exception;
}
