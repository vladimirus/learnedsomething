package com.learnedsomething.dao.impl;

import static org.springframework.util.StringUtils.hasText;

import com.learnedsomething.model.Link;
import com.learnedsomething.model.reddit.SearchResult;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Parsing reddit.
 */
public class RedditParser {
    private static final transient Logger LOG = Logger.getLogger(RedditParser.class);
    private final WebDriver driver;
    private final SearchResult searchResult;

    public RedditParser(WebDriver driver, SearchResult searchResult) {
        this.driver = driver;
        this.searchResult = searchResult;
    }

    public void parse() {
        WebElement siteTable = driver.findElement(By.id("siteTable"));
        processLinks(siteTable.findElements(By.className("link")));
        processPaginationUris(siteTable.findElements(By.cssSelector("span.nextprev a")));
    }

    private void processLinks(List<WebElement> links) {
        if (links != null && links.size() > 0) {
            for (WebElement rawLink : links) {
                Link link = null;
                try {
                    if (rawLink.isDisplayed()) {
                        link = processLink(rawLink);
                    }
                } catch (Exception ignore) {
                    LOG.debug("Can't parse link, ignoring...", ignore);
                }

                if (link != null && hasText(link.getUri())) {
                    searchResult.getLinks().add(link);
                }
            }
        }
    }

    private Link processLink(WebElement rawLink) {
        Link link = new Link();
        WebElement rawRank = rawLink.findElement(By.className("rank"));
        String rank = rawRank.getText();

        if (hasText(rank)) {
            rank = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].innerHTML", rawRank);
        }

        if (hasText(rank)) {
            WebElement rawEntry = rawLink.findElement(By.className("entry"));
            WebElement rawTitle = rawEntry.findElement(By.cssSelector("a.title"));

            String uri = rawTitle.getAttribute("href");
            String text = rawTitle.getText();

            if (hasText(uri) && hasText(text)) {
                link.setUri(uri);
                link.setText(text);
            }
        }
        return link;
    }

    private void processPaginationUris(List<WebElement> uris) {
        if (uris != null) {
            for (WebElement uri : uris) {
                String rel = uri.getAttribute("rel");
                if (hasText(rel)) {
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
            if (!hasText(uri)) {
                uri = null; //reset if empty
            }
        }
        return uri;
    }
}
