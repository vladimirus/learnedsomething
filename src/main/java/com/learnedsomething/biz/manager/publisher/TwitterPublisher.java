package com.learnedsomething.biz.manager.publisher;

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
    public void publish(Link link, WebBrowser browser) throws Exception {
        if (link.getText().length() < 140) {
            login(browser.getDriver());
            postLink(browser.getDriver(), link);
        }
    }

    private void login(WebDriver driver) {
        driver.get("https://twitter.com/");
        driver.findElement(By.id("signin-email")).sendKeys(email);
        WebElement passwordInput = driver.findElement(By.id("signin-password"));
        passwordInput.sendKeys(password);
        passwordInput.submit();
    }

    private void postLink(WebDriver driver, Link link) throws Exception {
        WebElement textarea = driver.findElement(By.id("tweet-box-mini-home-profile"));
        textarea.click();
        textarea.sendKeys(link.getText());

        Thread.sleep(2000);
        driver.findElement(By.className("tweet-action")).click();
    }
}
