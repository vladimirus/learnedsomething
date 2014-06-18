package com.learnedsomething.dao;

import com.learnedsomething.model.Link;

public interface LinkDao {
    void save(Link link);

    void deleteAll();
}
