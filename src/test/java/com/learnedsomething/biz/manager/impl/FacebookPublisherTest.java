package com.learnedsomething.biz.manager.impl;

import static com.learnedsomething.model.DomainFactory.aLink;

import com.learnedsomething.dao.browser.WebBrowserPool;
import com.learnedsomething.model.Link;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * Created by vladimir.
 */
public class FacebookPublisherTest {
    private FacebookPublisher facebookPublisher;
    @Mock
    private WebBrowserPool webBrowserPool;

    @Before
    public void before() {
        this.facebookPublisher = new FacebookPublisher();
        this.facebookPublisher.webBrowserPool = webBrowserPool;
    }

    @Test
    public void broadcast() {
        // given
        Link link = aLink();

        // when
        facebookPublisher.publish(link);

        // then
    }
}
