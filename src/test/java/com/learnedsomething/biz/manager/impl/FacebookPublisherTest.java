package com.learnedsomething.biz.manager.impl;

import static com.learnedsomething.model.DomainFactory.aLink;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.learnedsomething.dao.browser.WebBrowser;
import com.learnedsomething.dao.browser.WebBrowserPool;
import com.learnedsomething.model.Link;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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
    @Mock
    private WebDriver driver;
    @Mock
    private WebElement webElement;

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
        given(webBrowser.getDriver()).willReturn(driver);

        // when
        facebookPublisher.publish(link);

        // then
        verify(webBrowserPool).get();
        verify(webBrowserPool).close(webBrowser);
    }

    @Ignore
    @Test
    public void realTest() throws Exception {
        // given
        System.setProperty("ls.email", "");
        System.setProperty("ls.pass", "");
        Link link = aLink();
        link.setText("Something else here..." + System.currentTimeMillis());
        link.setUri("http://www.google.com/");
        WebBrowser browser = new WebBrowser(null);
        given(webBrowserPool.get()).willReturn(browser);

        // when
        facebookPublisher.publish(link);
    }
}
