package com.learnedsomething.biz.manager.publisher;

import static com.learnedsomething.model.DomainFactory.aLink;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.learnedsomething.dao.browser.WebBrowser;
import com.learnedsomething.model.Link;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Created by vladimir.
 */
@RunWith(MockitoJUnitRunner.class)
public class TwitterPublisherTest {
    private TwitterPublisher twitterPublisher;

    @Mock
    private WebBrowser browser;
    @Mock
    private WebDriver driver;
    @Mock
    private WebElement webElement;

    @Before
    public void before() {
        this.twitterPublisher = new TwitterPublisher();
    }

    @Test
    public void shouldPublish() throws Exception {
        // given
        Link link = aLink();
        link.setText("Something else here..." + System.currentTimeMillis());
        link.setUri("http://www.google.com/");
        given(browser.getDriver()).willReturn(driver);
        given(driver.findElement(isA(By.class))).willReturn(webElement);

        // when
        twitterPublisher.publish(link, browser);

        // then
        verify(browser, atLeastOnce()).getDriver();
    }

    @Test
    public void shouldNotpublish() throws Exception {
        // given
        Link link = aLink();

        StringBuilder text = new StringBuilder();
        for (int i = 0; i < 141; i++) {
            text.append("a");
        }
        link.setText(text.toString());
        link.setUri("http://www.google.com/");
        given(browser.getDriver()).willReturn(driver);
        given(driver.findElement(isA(By.class))).willReturn(webElement);

        // when
        twitterPublisher.publish(link, browser);

        // then
        verify(browser, never()).getDriver();
    }

    @Ignore
    @Test
    public void realTest() throws Exception {
        // given
        twitterPublisher.email = "";
        twitterPublisher.password = "";
        Link link = aLink();
        link.setText("Something else here..." + System.currentTimeMillis());
        link.setUri("http://www.google.com/");
        WebBrowser browser = new WebBrowser(null);

        // when
        twitterPublisher.publish(link, browser);
    }
}
