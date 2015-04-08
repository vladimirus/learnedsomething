package com.learnedsomething.biz.manager.publisher;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.Keys.RETURN;

import com.learnedsomething.dao.browser.WebBrowser;
import com.learnedsomething.model.Link;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by vladimir.
 */
@Service
public class TwitterPublisher implements Publisher {
    @Value("${ls.twitter.email}")
    String email;
    @Value("${ls.twitter.pass}")
    String password;

    @Override
    public void publish(Link link, WebBrowser browser) {
        if (link.getText().length() < 140) {
            login(browser.getDriver());
            postLink(browser.getDriver(), link);
        }
    }

    private void login(WebDriver driver) {
        driver.get("https://twitter.com/");
        driver.findElement(By.id("signin-email")).sendKeys(email);
        driver.findElement(By.id("signin-password")).sendKeys(password);
        driver.findElement(By.id("signin-password")).sendKeys(RETURN);
        sleepUninterruptibly(4, SECONDS);
    }

    private void postLink(WebDriver driver, Link link) {
        WebElement textarea = driver.findElement(By.id("tweet-box-home-timeline"));
        textarea.click();
        textarea.sendKeys(link.getText());
        driver.findElement(By.className("tweet-action")).click();
    }
}
