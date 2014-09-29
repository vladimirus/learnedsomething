package com.learnedsomething.dao.impl;

import com.learnedsomething.dao.SearchDao;
import com.learnedsomething.dao.browser.WebBrowser;
import com.learnedsomething.dao.browser.WebBrowserPool;
import com.learnedsomething.model.reddit.SearchQuery;
import com.learnedsomething.model.reddit.SearchResult;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * This class actually connects to reddit and parses its response.
 */
@Repository
public class RedditDaoImpl implements SearchDao {
    private static final transient Logger LOG = Logger.getLogger(RedditDaoImpl.class);
    @Autowired
    WebBrowserPool webBrowserPool;
    @Autowired
    RedditAuthenticator authenticator;

    public SearchResult search(SearchQuery query) {
        SearchResult searchResult = new SearchResult();
        if (query != null && StringUtils.hasText(query.getSearchUri())) {
            WebBrowser browser = webBrowserPool.get();
            if (browser != null) {
                doSearch(query.getSearchUri(), searchResult, browser.getDriver());
            }
            webBrowserPool.release(browser);
        }
        return searchResult;
    }

    void doSearch(String query, SearchResult searchResult, WebDriver driver) {
        try {
            driver.get(query);
            loginIfNeeded(query, driver);
            new RedditParser(driver, searchResult).parse(); //this should come from a factory... maybe later..
        } catch (Exception ignore) {
            LOG.error(ignore);
        }
    }

    private void loginIfNeeded(String redirectUrl, WebDriver driver) {
        if (!authenticator.isLoggedIn(driver)) {
            authenticator.login(driver);
            driver.get(redirectUrl);
        }
    }
}
