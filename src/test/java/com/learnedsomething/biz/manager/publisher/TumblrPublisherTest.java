package com.learnedsomething.biz.manager.publisher;

import static com.learnedsomething.model.DomainFactory.aLink;

import com.learnedsomething.dao.browser.WebBrowser;
import com.learnedsomething.model.Link;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by vladimir.
 */
@RunWith(MockitoJUnitRunner.class)
public class TumblrPublisherTest {
    private TumblrPublisher publisher;

    @Before
    public void before() {
        this.publisher = new TumblrPublisher();
    }

    @Ignore
    @Test
    public void realTest() throws Exception {
        // given
        publisher.email = "";
        publisher.password = "";
        Link link = aLink();
        link.setText("Something else here..." + System.currentTimeMillis());
        link.setUri("http://www.google.com/");
        WebBrowser browser = new WebBrowser(null);

        // when
        publisher.publish(link, browser);
    }
}
