package com.learnedsomething.biz.manager.impl;

import com.learnedsomething.biz.manager.Publisher;
import com.learnedsomething.dao.browser.WebBrowser;
import com.learnedsomething.dao.browser.WebBrowserPool;
import com.learnedsomething.model.Link;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by vladimir.
 */
@Service
public class FacebookPublisher implements Publisher {
    private static final transient Logger LOG = Logger.getLogger(FacebookPublisher.class);
    @Autowired
    WebBrowserPool webBrowserPool;

    @Override
    public void publish(Link link) {
        WebBrowser browser = webBrowserPool.get();
        if (browser != null) {
            try {
                publish(link, browser);
            } catch (Exception e) {
                LOG.error("Can't publish", e);
            } finally {
                webBrowserPool.close(browser);
            }
        }

    }

    private void publish(Link link, WebBrowser browser) throws Exception {
        login(browser.getDriver());
        openPage(browser.getDriver());
        postLink(browser.getDriver(), link);
    }

    private void login(WebDriver driver) {
        driver.get("https://www.facebook.com/");
        driver.findElement(By.id("email")).sendKeys(System.getProperty("ls.email"));
        driver.findElement(By.id("pass")).sendKeys(System.getProperty("ls.pass"));
        driver.findElement(By.id("loginbutton")).click();
    }

    private void openPage(WebDriver driver) {
        driver.get("https://www.facebook.com/pages/Today-I-learned/320953654726360?ref_type=bookmark");
    }

    private void postLink(WebDriver driver, Link link) throws Exception {
        WebElement textarea = driver.findElement(By.xpath("//textarea[contains(.,'What have you been up to?')]"));
        textarea.click();
        textarea.sendKeys(link.getText());
        textarea.sendKeys(Keys.RETURN);
        textarea.sendKeys(Keys.RETURN);
        textarea.sendKeys(link.getUri());
        textarea.sendKeys(Keys.SPACE);
        Thread.sleep(4000);

        textarea.submit();
    }
}
