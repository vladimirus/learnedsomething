package com.learnedsomething.biz.manager.impl;

import com.learnedsomething.biz.manager.Publisher;
import com.learnedsomething.dao.browser.WebBrowserPool;
import com.learnedsomething.model.Link;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by vladimir.
 */
public class FacebookPublisher implements Publisher {
    @Autowired
    WebBrowserPool webBrowserPool;

    @Override
    public void publish(Link link) {

    }
}
