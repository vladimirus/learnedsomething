package com.learnedsomething.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import com.learnedsomething.model.reddit.SearchQuery;
import com.learnedsomething.model.reddit.SearchResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.core.io.ClassPathResource;

import java.io.File;

@RunWith(MockitoJUnitRunner.class)
public class RedditParserTest {
    @Mock
    private SearchQuery query;

    private SearchResult searchResult;
    private WebDriver driver;

    @Before
    public void before() {
        searchResult = new SearchResult();
        driver = new HtmlUnitDriver(true);
    }

    @After
    public void after() {
        driver.quit();
    }

    @Test
    public void doSearchFromStaticFile01() {
        //given
        driver.get(file(("reddit-01.html")));
        RedditParser redditParser = new RedditParser(driver, searchResult);

        // when
        redditParser.parse();

        // then
        assertEquals(25, searchResult.getLinks().size());
        assertEquals("http://www.reddit.com/?count=25&after=t3_1toimn", searchResult.getNextPage());

        assertEquals("The true meaning of Christmas", searchResult.getLinks().get(5).getText());
        assertEquals("http://i.imgur.com/lOqtfFN.png", searchResult.getLinks().get(5).getUri());
        assertNull(searchResult.getLinks().get(5).getId());
    }

    @Test
    public void doSearchFromStaticFile02() {
        //given
        driver = new FirefoxDriver(); //javascript only works in firefox (not html driver) for some reason...
        driver.get(file(("reddit-02.html")));
        RedditParser redditParser = new RedditParser(driver, searchResult);

        // when
        redditParser.parse();

        // then
        assertEquals(25, searchResult.getLinks().size());
    }

    private String file(String filename) {
        String fileLocation = null;
        ClassPathResource resource = new ClassPathResource("reddit-html/" + filename);
        try {
            File file = resource.getFile();
            if (file.exists()) {
                fileLocation = "file://" + file.getAbsolutePath();
            }
        } catch (Exception ignore) {
            fail("Can't find the file");
        }
        return fileLocation;
    }
}
