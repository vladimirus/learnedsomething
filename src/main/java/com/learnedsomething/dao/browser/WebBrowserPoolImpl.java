package com.learnedsomething.dao.browser;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Implementation of the pool.
 */
@Component
public class WebBrowserPoolImpl implements WebBrowserPool {
    private static final transient Logger LOG = Logger.getLogger(WebBrowserPoolImpl.class);
    Integer numberOfAttempts = 10;
    Long millisToSleepWhileAttempt = 10000L; //10 seconds
    Integer totalNumberOfWebBrowsers = 10;
    List<WebBrowser> pool = new ArrayList<WebBrowser>();
    WebDriver defaultWebClient;

    @Override
    public synchronized WebBrowser get() {
        WebBrowser browser = null;

        for (int i = 0; i < numberOfAttempts; i++) {
            browser = getWebBrowserFromPool();

            if (browser != null) {
                break;
            }

            try {
                Thread.sleep(millisToSleepWhileAttempt);
            } catch (InterruptedException e) {
                LOG.error(e);
            }
        }

        return browser;
    }

    @Override
    public void release(WebBrowser browser) {
        browser.setAvailable(true);
    }

    @Override
    @PreDestroy
    public void closeAll() {
        LOG.debug("Closing all browsers...");
        for (WebBrowser br : pool) {
            close(br);
        }

        pool.clear();
    }

    @Override
    public void close(WebBrowser browser) {
        try {
            browser.setAvailable(false);
            pool.remove(browser);
            browser.close();
        } catch (Exception e) {
            LOG.error("error closing", e);
        }
    }

    private WebBrowser getWebBrowserFromPool() {
        WebBrowser browser = null;
        int count = 0;

        Set<WebBrowser> cleanUp = new HashSet<WebBrowser>();
        for (WebBrowser br : pool) {
            count++;
            boolean close = toCloseWebBrowser(br);
            if (!close) {
                if (br.isAvailable()) {
                    browser = br;
                    break;
                }
            } else {
                cleanUp.add(br);
            }
        }

        closeWebBrowsers(cleanUp);

        if (browser == null && count < totalNumberOfWebBrowsers) {
            browser = createWebBrowser();
            pool.add(browser);
        }

        if (browser != null) {
            browser.setAvailable(false);
        }
        return browser;
    }

    private void closeWebBrowsers(Set<WebBrowser> cleanUp) {
        if (cleanUp != null && !cleanUp.isEmpty()) {
            for (WebBrowser br : cleanUp) {
                close(br);
            }
        }
    }

    private boolean toCloseWebBrowser(WebBrowser browser) {
        boolean isExpired = false;

        if (browser.isExpired()) {
            isExpired = true;
        } else if (browser.getDriver() == null) {
            isExpired = true;
        } else if (browser.getDriver().toString().contains("(null)")) {
            isExpired = true;
        }

        return isExpired;
    }

    private WebBrowser createWebBrowser() {
        WebBrowser browser = null;
        try {
            browser = new WebBrowser(defaultWebClient);
        } catch (Exception e) {
            LOG.error(e);
        }
        return browser;
    }

    public WebDriver getDefaultWebClient() {
        return defaultWebClient;
    }

    public void setDefaultWebClient(WebDriver defaultWebClient) {
        this.defaultWebClient = defaultWebClient;
    }
}
