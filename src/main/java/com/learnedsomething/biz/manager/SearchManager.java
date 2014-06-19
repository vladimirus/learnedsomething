package com.learnedsomething.biz.manager;

import com.learnedsomething.model.Link;
import com.learnedsomething.model.reddit.SearchQuery;

import java.util.List;

/**
 * Manager to deal with reddit.com output.
 */
public interface SearchManager {
    List<Link> findNewLinks();

    List<Link> findNewLinks(SearchQuery query);
}
