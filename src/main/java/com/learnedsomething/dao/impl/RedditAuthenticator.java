package com.learnedsomething.dao.impl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Deals with reddit's login.
 */
@Service
public class RedditAuthenticator {
    @Value("${ls.reddit.name}")
    String name;
    @Value("${ls.reddit.pass}")
    String password;

    public boolean isLoggedIn(WebDriver driver) {
        boolean result;
        try {
            WebElement username = driver.findElement(By.cssSelector("div#header-bottom-right span.user a"));
            result = username.getText().contains(name);
        } catch (Exception ignore) {
            result = false;
        }
        return result;
    }

    public void login(WebDriver driver) {

    }
}
