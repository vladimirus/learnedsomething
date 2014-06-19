package com.learnedsomething.dao;

import com.learnedsomething.model.reddit.SearchQuery;
import com.learnedsomething.model.reddit.SearchResult;

/**
 * Interface to connect to reddit.com.
 */
public interface SearchDao {

    SearchResult search(SearchQuery query);

}
