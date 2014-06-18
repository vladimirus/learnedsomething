package com.learnedsomething.biz.manager.impl;

import static com.learnedsomething.model.DomainFactory.aLink;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.learnedsomething.dao.LinkExtendedDao;
import com.learnedsomething.model.Link;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * Test for LinkManager.
 */
@RunWith(MockitoJUnitRunner.class)
public class LinkManagerImplTest {
    private LinkManagerImpl manager;
    @Mock
    private LinkExtendedDao mongoDao;
    @Mock
    private ThreadPoolTaskExecutor taskExecutor;

    @Before
    public void before() {
        this.manager = new LinkManagerImpl();
        this.manager.mongoDao = mongoDao;
        this.manager.taskExecutor = taskExecutor;
    }

    @Test
    public void findAll() {
        // given
        List<Link> links = new ArrayList<Link>();
        links.add(aLink());
        links.add(aLink());
        given(mongoDao.findAll()).willReturn(links);

        // when
        List<Link> actual = manager.findAll();

        // then
        assertNotNull(actual);
        assertEquals(2, actual.size());
    }

    @Test
    public void save() {
        // given
        Link link = new Link();
        link.setUri("test");

        // when
        Link actual = manager.save(link);

        // then
        verify(mongoDao).save(link);
        assertEquals("098f6bcd4621d373cade4e832627b4f6", actual.getId());
    }

    @Test
    public void saveNull() {
        // given
        Link link = null;

        // when
        manager.save(link);

        // then
        verify(mongoDao, never()).save(link);
    }

    @Test
    public void startIndex() {

        // when
        manager.startIndexThread();

        // then
        verify(taskExecutor).execute(isA(Runnable.class));
    }

    @Test
    public void saveMany() {
        // given
        List<Link> links = new ArrayList<Link>();
        links.add(aLink());
        links.add(aLink());

        // when
        manager.save(links);

        // then
        verify(mongoDao).save(links);
    }

    @Test
    public void saveNone() {
        // given
        List<Link> links = new ArrayList<Link>();

        // when
        manager.save(links);

        // then
        verify(mongoDao, never()).save(links);
    }

    @Test
    public void saveNullLinks() {
        // given
        List<Link> links = null;

        // when
        manager.save(links);

        // then
        verify(mongoDao, never()).save(links);
    }

    @Test
    public void index() {
        // given
        List<Link> links = new ArrayList<Link>();
        links.add(aLink());
        links.add(aLink());

        // when
        manager.index();

        // then
//        verify(redditManager).findNewLinks(); //TODO
    }

    @Test
    public void findById() {
        // given
        given(mongoDao.findById("1")).willReturn(aLink());

        // when
        Link link = manager.findById("1");

        // then
        verify(mongoDao).findById("1");
        assertNotNull(link);
    }

    @Test
    public void startBroadcast() {

        // when
        manager.startBroadcastThread();

        // then
        verify(taskExecutor).execute(isA(Runnable.class));
    }

    @Test
    public void broadcast() {
        // given
        List<Link> links = new ArrayList<Link>();
        links.add(aLink());
        links.add(aLink());
        given(mongoDao.findToBroadcast()).willReturn(links);

        // when
        manager.broadcast();

        // then
        verify(mongoDao).findToBroadcast();
        verify(mongoDao, times(2)).delete(isA(Link.class));
    }

    @Test
    public void deleteAll() {

        // when
        manager.deleteAll();

        // then
        verify(mongoDao).deleteAll();
    }
}
