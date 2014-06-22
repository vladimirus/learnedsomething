package com.learnedsomething.biz.manager.impl;

import com.learnedsomething.biz.manager.SearchManager;
import com.learnedsomething.dao.SearchDao;
import com.learnedsomething.model.Link;
import com.learnedsomething.model.reddit.SearchQuery;
import com.learnedsomething.model.reddit.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Manager which connects to reddit and calls a service to save links etc.
 */
@Service
public class RedditManagerImpl implements SearchManager {
    @Autowired
    SearchDao searchDao;

    public List<Link> findNewLinks() {
        SearchQuery query = new SearchQuery("http://www.reddit.com/r/todayilearned/top/?sort=top&t=week");
        return findNewLinks(query);
    }

    public List<Link> findNewLinks(SearchQuery q) {
        SearchQuery query = q;
        SearchResult result = retrieveSearchResult(query);
        return result.getLinks();
    }

    SearchResult retrieveSearchResult(SearchQuery query) {
        return searchDao.search(query);
    }
}
