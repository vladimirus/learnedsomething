package com.learnedsomething.biz.manager.publisher;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.openqa.selenium.OutputType.FILE;

import com.learnedsomething.dao.browser.WebBrowser;
import com.learnedsomething.dao.browser.WebBrowserPool;
import com.learnedsomething.model.Link;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.TakesScreenshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
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
            try {
                File error = ((TakesScreenshot) browser.getDriver()).getScreenshotAs(FILE);
                FileUtils.copyFile(error, new File("/tmp/learnedsomething/" + publisher.getClass().getSimpleName() + ".png"));
            } catch (IOException e1) {
                LOG.error("Can't take screenshot", e);
            }
        }
    }
}
