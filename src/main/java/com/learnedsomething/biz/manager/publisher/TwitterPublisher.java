package com.learnedsomething.biz.manager.publisher;

import com.learnedsomething.dao.browser.WebBrowser;
import com.learnedsomething.model.Link;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Created by vladimir.
 */
public class TwitterPublisher implements Publisher {

    public void publish(Link link, WebBrowser browser) throws Exception {
        if (link.getText().length() < 140) {
            login(browser.getDriver());
            postLink(browser.getDriver(), link);
        }
    }

    private void login(WebDriver driver) {
        driver.get("https://twitter.com/");
        driver.findElement(By.id("signin-email")).sendKeys(System.getProperty("ls.twitter.email"));
        WebElement password = driver.findElement(By.id("signin-password"));
        password.sendKeys(System.getProperty("ls.twitter.pass"));
        password.submit();
    }

    private void postLink(WebDriver driver, Link link) {
        WebElement textarea = driver.findElement(By.id("tweet-box-mini-home-profile"));
        textarea.click();
        textarea.sendKeys(link.getText());
        driver.findElement(By.className("tweet-action")).click();
    }
}
