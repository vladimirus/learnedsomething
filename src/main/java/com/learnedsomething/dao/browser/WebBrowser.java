package com.learnedsomething.dao.browser;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * WebBrowser contains webdriver.
 */
public class WebBrowser {
    Date created = new Date();
    private WebDriver driver;
    private boolean available;

    /**
     * Instantiates proxy server and webdriver.
     *
     * @param driver - webdriver to use
     * @throws Exception - raised if fails to create
     */
    public WebBrowser(WebDriver driver) throws Exception {
        if (driver == null) {
            this.driver = firefoxDriver();
        } else {
            this.driver = driver;
        }
        this.available = true;
    }

    private WebDriver firefoxDriver() throws Exception {
        WebDriver driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(180, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(180, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(180, TimeUnit.SECONDS);
        return driver;
    }

    /**
     * Stops everything.
     *
     * @throws Exception raised if couldnt be closed
     */
    public void close() throws Exception {
        driver.quit();
        available = false;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Date getCreated() {
        return created;
    }

    public boolean isExpired() {
        boolean isExpired = false;

        Calendar timeFromCreated = Calendar.getInstance();
        timeFromCreated.setTime(created);
        timeFromCreated.add(Calendar.HOUR, 1);

        Date now = new Date();

        if (now.after(timeFromCreated.getTime())) {
            isExpired = true;
        }

        return isExpired;
    }
}
