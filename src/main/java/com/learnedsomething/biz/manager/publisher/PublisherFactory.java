package com.learnedsomething.biz.manager.publisher;

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
    @Autowired
    WebBrowserPool webBrowserPool;
    @Autowired
    List<Publisher> publishers;

    @Override
    public void publish(Link link) {
        WebBrowser browser = webBrowserPool.get();
        if (browser != null) {
            for (Publisher publisher : publishers) {
                try {
                    publisher.publish(link, browser);
                } catch (Exception e) {
                    LOG.error(publisher.getClass() + " cannot publish link: " + link.toString());
                    LOG.error("Can't publish", e);
                }
            }
            webBrowserPool.close(browser);
        }
    }
}
