package com.learnedsomething.biz.manager.impl;

import static com.learnedsomething.model.DomainFactory.aLink;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.learnedsomething.dao.browser.WebBrowser;
import com.learnedsomething.dao.browser.WebBrowserPool;
import com.learnedsomething.model.Link;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by vladimir.
 */
@RunWith(MockitoJUnitRunner.class)
public class FacebookPublisherTest {
    private FacebookPublisher facebookPublisher;
    @Mock
    private WebBrowserPool webBrowserPool;
    @Mock
    private WebBrowser webBrowser;

    @Before
    public void before() {
        this.facebookPublisher = new FacebookPublisher();
        this.facebookPublisher.webBrowserPool = webBrowserPool;
    }

    @Test
    public void broadcast() {
        // given
        Link link = aLink();
        given(webBrowserPool.get()).willReturn(webBrowser);

        // when
        facebookPublisher.publish(link);

        // then
        verify(webBrowserPool).get();
        verify(webBrowserPool).close(webBrowser);
    }
}
