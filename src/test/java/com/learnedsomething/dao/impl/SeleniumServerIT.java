package com.learnedsomething.dao.impl;


import static org.openqa.selenium.remote.DesiredCapabilities.firefox;

import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class SeleniumServerIT {

    @Test
    @Ignore
    public void connect() throws Exception {
        WebDriver driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), firefox());
        driver.get("http://learnedsomething.today");

        System.out.println(driver.getPageSource());

    }
}
