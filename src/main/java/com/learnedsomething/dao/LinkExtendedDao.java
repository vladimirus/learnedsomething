package com.learnedsomething.dao;

import com.learnedsomething.model.Link;

import java.util.List;

/**
 * Interface to save links.
 */
public interface LinkExtendedDao extends LinkDao {
    void saveNew(List<Link> links);

    List<Link> findAll();

    Link findById(String id);

    List<Link> findToBroadcast();

    void delete(Link link);
}
