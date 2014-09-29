package com.learnedsomething.biz.manager.publisher;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.Keys.RETURN;
import static org.openqa.selenium.Keys.SPACE;

import com.learnedsomething.dao.browser.WebBrowser;
import com.learnedsomething.model.Link;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by vladimir.
 */
@Service
public class FacebookPublisher implements Publisher {
    private static final transient Logger LOG = Logger.getLogger(FacebookPublisher.class);
    @Value("${ls.facebook.email}")
    String email;
    @Value("${ls.facebook.pass}")
    String password;

    @Override
    public void publish(Link link, WebBrowser browser) {
        login(browser.getDriver());
        openPage(browser.getDriver());
        postLink(browser.getDriver(), link);
    }

    private void login(WebDriver driver) {
        driver.get("https://www.facebook.com/");
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("pass")).sendKeys(password);
        driver.findElement(By.id("pass")).sendKeys(RETURN);
        sleepUninterruptibly(4, SECONDS);
    }

    private void openPage(WebDriver driver) {
        driver.get("https://www.facebook.com/learnedsomething?focus_composer=true&ref_type=bookmark");
    }

    private void postLink(WebDriver driver, Link link) {
        WebElement textarea = driver.findElement(By.xpath("//textarea[contains(.,'What have you been up to?')]"));
        textarea.click();
        textarea.sendKeys(link.getText());
        textarea.sendKeys(RETURN);
        textarea.sendKeys(RETURN);
        textarea.sendKeys(link.getUri());
        textarea.sendKeys(SPACE);
        sleepUninterruptibly(4, SECONDS);
        textarea.submit();
    }
}
