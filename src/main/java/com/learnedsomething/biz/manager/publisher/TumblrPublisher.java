package com.learnedsomething.biz.manager.publisher;

import static org.openqa.selenium.Keys.RETURN;

import com.learnedsomething.dao.browser.WebBrowser;
import com.learnedsomething.model.Link;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Publishes links to Tumblr.
 */
@Service
public class TumblrPublisher implements Publisher {
    @Value("${ls.tumblr.email}")
    String email;
    @Value("${ls.tumblr.pass}")
    String password;

    @Override
    public void publish(Link link, WebBrowser browser) {
        login(browser.getDriver());
        openPage(browser.getDriver());
        postLink(browser.getDriver(), link);
    }

    private void login(WebDriver driver) {
        driver.get("https://www.tumblr.com/login");
        driver.findElement(By.id("signup_email")).sendKeys(email);
        driver.findElement(By.id("signup_password")).sendKeys(password);
        driver.findElement(By.id("signup_forms_submit")).sendKeys(RETURN);
    }

    private void openPage(WebDriver driver) {
        driver.findElement(By.id("new_post_label_text")).click();
    }

    private void postLink(WebDriver driver, Link link) {
//        driver.switchTo().frame(driver.findElement(By.id("post_two_ifr")));
        driver.findElement(By.cssSelector("div.editor.editor-plaintext")).sendKeys(link.getText());
        driver.findElement(By.cssSelector("div.editor.editor-richtext")).sendKeys(link.getUri());
//        driver.switchTo().defaultContent();
        driver.findElement(By.className("create_post_button")).click();
    }
}
