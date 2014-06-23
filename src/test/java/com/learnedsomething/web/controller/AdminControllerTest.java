package com.learnedsomething.web.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import com.learnedsomething.biz.manager.LinkManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AdminControllerTest {
    private AdminController controller;
    @Mock
    private LinkManager linkManager;

    @Before
    public void before() {
        this.controller = new AdminController();
        this.controller.linkManager = linkManager;
    }

    @Test
    public void index() {

        // when
        String actual = controller.index();

        // then
        verify(linkManager).startIndexThread();
        assertEquals("started", actual);
    }

    @Test
    public void broadcast() {

        // when
        String actual = controller.broadcast();

        // then
        verify(linkManager).startBroadcastThread();
        assertEquals("started", actual);
    }

    @Test
    public void broadcastQueue() {

        // when
        controller.broadcastQueue();

        // then
        verify(linkManager).getLinksToBroadcast();
    }

    @Test
    public void delete() {

        // when
        String actual = controller.delete();

        // then
        verify(linkManager).deleteAll();
        assertEquals("started", actual);
    }
}
