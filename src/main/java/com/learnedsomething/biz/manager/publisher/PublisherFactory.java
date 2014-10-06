package com.learnedsomething.biz.manager.publisher;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import com.learnedsomething.dao.browser.WebBrowser;
import com.learnedsomething.dao.browser.WebBrowserPool;
import com.learnedsomething.model.Link;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by vladimir.
 */
@Service
public class PublisherFactory implements Publishable {
    private static final transient Logger LOG = Logger.getLogger(PublisherFactory.class);
    Long sleepAfterEachPublishMillis = 30000L; //30 seconds
    @Autowired
    WebBrowserPool webBrowserPool;
    @Autowired
    List<Publisher> publishers;

    @Override
    public void publish(Link link) {
        for (Publisher publisher : publishers) {
            WebBrowser browser = webBrowserPool.get();
            if (browser != null) {
                publish(link, browser, publisher);
                webBrowserPool.close(browser);
            }
        }
    }

    private void publish(Link link, WebBrowser browser, Publisher publisher) {
        try {
            LOG.debug("Publishing to " + publisher.getClass());
            publisher.publish(link, browser);
            if (sleepAfterEachPublishMillis > 0) {
                sleepUninterruptibly(sleepAfterEachPublishMillis, MILLISECONDS);
            }
        } catch (Exception e) {
            LOG.error(publisher.getClass() + " cannot publish link: " + link.toString());
            LOG.error("Can't publish", e);
        }
    }
}
