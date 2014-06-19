package com.learnedsomething.biz.manager.impl;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.learnedsomething.dao.SearchDao;
import com.learnedsomething.model.reddit.SearchQuery;
import com.learnedsomething.model.reddit.SearchResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RedditManagerImplTest {
    private RedditManagerImpl manager;
    @Mock
    private SearchDao searchDao;

    @Before
    public void before() {
        this.manager = new RedditManagerImpl();
        this.manager.searchDao = searchDao;
    }

    @Test
    public void findNewLinks() {
        // given
        SearchResult result1 = new SearchResult();
        result1.setNextPage("nextPage");
        SearchResult result2 = new SearchResult();
        SearchQuery query = new SearchQuery("test");
        given(searchDao.search(query)).willReturn(result1, result2);

        // when
        manager.findNewLinks(query);

        // then
        verify(searchDao, times(1)).search(Mockito.isA(SearchQuery.class));
    }

    @Test
    public void retrieveSearchResult() {
        // given
        SearchResult result1 = new SearchResult();
        SearchQuery query = new SearchQuery("test");
        given(searchDao.search(query)).willReturn(result1);

        // when
        manager.retrieveSearchResult(query);

        // then
        verify(searchDao).search(query);
    }
}
