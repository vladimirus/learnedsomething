package com.learnedsomething.dao.impl;

import com.learnedsomething.dao.SearchDao;
import com.learnedsomething.dao.browser.WebBrowser;
import com.learnedsomething.dao.browser.WebBrowserPool;
import com.learnedsomething.model.Link;
import com.learnedsomething.model.reddit.SearchQuery;
import com.learnedsomething.model.reddit.SearchResult;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * This class accually connects to reddit and parses its response.
 */
@Repository
public class RedditDaoImpl implements SearchDao {
    private final transient Logger log = Logger.getLogger(this.getClass());
    @Autowired
    WebBrowserPool webBrowserPool;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

    public SearchResult search(SearchQuery query) {
        SearchResult searchResult = new SearchResult();
        if (query != null && StringUtils.hasText(query.getSearchUri())) {
            WebBrowser browser = webBrowserPool.get();
            if (browser != null) {
                doSearch(query, searchResult, browser);
            }
            webBrowserPool.release(browser);
        }
        return searchResult;
    }

    void doSearch(SearchQuery query, SearchResult searchResult, WebBrowser browser) {
        try {
            WebDriver driver = browser.getDriver();
            driver.get(query.getSearchUri());
            WebElement siteTable = driver.findElement(By.id("siteTable"));
            processLinks(searchResult, siteTable.findElements(By.className("link")));
            processPaginationUris(searchResult, siteTable.findElements(By.cssSelector("span.nextprev a")));
        } catch (Exception ignore) { // in case browser is closed while searching
            log.error(ignore);
        }
    }

    private void processLinks(SearchResult searchResult, List<WebElement> links) {
        if (links != null && links.size() > 0) {
            for (WebElement rawLink : links) {
                Link link = null;
                try {
                    if (rawLink.isDisplayed()) {
                        link = processLink(rawLink);
                    }
                } catch (Exception ignore) {
                    log.debug("Can't parse link, ignoring...", ignore);
                }

                if (link != null && StringUtils.hasText(link.getUri())) {
                    searchResult.getLinks().add(link);
                }
            }
        }
    }

    private Link processLink(WebElement rawLink) {
        Link link = new Link();
        WebElement rawRank = rawLink.findElement(By.className("rank"));
        String rank = rawRank.getText();

        if (StringUtils.hasText(rank)) {
            Integer down = NumberUtils.parseNumber(rawLink.getAttribute("data-downs"), Integer.class);
            Integer up = NumberUtils.parseNumber(rawLink.getAttribute("data-ups"), Integer.class);
            WebElement rawEntry = rawLink.findElement(By.className("entry"));
            WebElement rawTitle = rawEntry.findElement(By.cssSelector("a.title"));
            WebElement rawComments = rawEntry.findElement(By.cssSelector("a.comments"));

            String uri = rawTitle.getAttribute("href");
            String text = rawTitle.getText();
            String commentsUri = rawComments.getAttribute("href");

            if (StringUtils.hasText(uri) && StringUtils.hasText(text)) {
                link.setUri(uri);
                link.setText(text);
            }
        }
        return link;
    }

    private Date dateFromString(WebElement rawEntry) {
        Date date = null;
        try {
            WebElement rawTime = rawEntry.findElement(By.cssSelector("time"));
            String dateStr = rawTime.getAttribute("datetime");
            date = dateFormatter.parse(dateStr);
        } catch (Exception e) {
            log.error("Can't convert date", e);
            date = new Date();
        }
        return date;
    }

    private void processPaginationUris(SearchResult searchResult, List<WebElement> uris) {
        if (uris != null) {
            for (WebElement uri : uris) {
                String rel = uri.getAttribute("rel");
                if (StringUtils.hasText(rel)) {
                    if (rel.contains("next")) {
                        searchResult.setNextPage(processPaginationUri(uri));
                    } else if (rel.contains("prev")) {
                        searchResult.setPrevPage(processPaginationUri(uri));
                    }
                }
            }
        }
    }

    private String processPaginationUri(WebElement rawUri) {
        String uri = null;
        if (rawUri != null) {
            uri = rawUri.getAttribute("href");
            if (!StringUtils.hasText(uri)) {
                uri = null; //reset if empty
            }
        }
        return uri;
    }
}
