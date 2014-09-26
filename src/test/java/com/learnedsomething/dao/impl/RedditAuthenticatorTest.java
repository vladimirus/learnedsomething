package com.learnedsomething.dao.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isA;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Test for RedditAuthenticator.
 */
@RunWith(MockitoJUnitRunner.class)
public class RedditAuthenticatorTest {
    @Mock
    private WebDriver driver;
    @Mock
    private WebElement webElement;

    private RedditAuthenticator redditAuthenticator;

    @Before
    public void before() {
        this.redditAuthenticator = new RedditAuthenticator();
        redditAuthenticator.name = "test";
        redditAuthenticator.password = "pass";
    }

    @Test
    public void shouldBeLoggedIn() {
        // given
        given(driver.findElement(isA(By.class))).willReturn(webElement);
        given(webElement.getText()).willReturn("logged in as test");

        // when
        boolean actual = redditAuthenticator.isLoggedIn(driver);

        // then
        assertThat(actual, is(true));
    }

    @Test
    public void shouldNotBeLoggedIn() {
        // given
        given(driver.findElement(isA(By.class))).willReturn(webElement);
        given(webElement.getText()).willReturn("login here");

        // when
        boolean actual = redditAuthenticator.isLoggedIn(driver);

        // then
        assertThat(actual, is(false));
    }
}
