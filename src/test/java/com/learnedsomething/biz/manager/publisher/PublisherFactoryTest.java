package com.learnedsomething.biz.manager.publisher;

import static com.learnedsomething.model.DomainFactory.aLink;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.learnedsomething.dao.browser.WebBrowser;
import com.learnedsomething.dao.browser.WebBrowserPool;
import com.learnedsomething.model.Link;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Iterator;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class PublisherFactoryTest extends TestCase {
    private PublisherFactory publisherFactory;
    @Mock
    private WebBrowserPool webBrowserPool;
    @Mock
    private WebBrowser webBrowser;
    @Mock
    private List<Publisher> publishers;

    @Mock
    private Publisher publisher;

    @Before
    public void before() {
        this.publisherFactory = new PublisherFactory();
        this.publisherFactory.publishers = publishers;
        this.publisherFactory.webBrowserPool = webBrowserPool;
        this.publisherFactory.sleepAfterEachPublishMillis = 0L;
    }

    @Test
    public void publish() {
        // given
        Link link = aLink();
        given(webBrowserPool.get()).willReturn(webBrowser);
        Iterator iterator = Mockito.mock(Iterator.class);
        given(publishers.iterator()).willReturn(iterator);
        given(iterator.hasNext()).willReturn(true, false);
        given(iterator.next()).willReturn(publisher);

        // when
        publisherFactory.publish(link);

        // then
        verify(iterator, times(2)).hasNext();
        verify(webBrowserPool).get();
        verify(webBrowserPool).close(webBrowser);
    }
}