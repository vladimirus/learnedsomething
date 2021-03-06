package com.learnedsomething.dao.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import com.learnedsomething.dao.LinkExtendedDao;
import com.learnedsomething.model.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This class connects to mongodb to save links.
 */
@Repository
public class MongoDaoImpl implements LinkExtendedDao {
    @Autowired
    MongoOperations mongoOperation;

    @Override
    public void save(Link link) {
        mongoOperation.save(link);
    }

    @Override
    public void saveNew(List<Link> links) {
        for (Link link : links) {
            Link existing = findById(link.getId());
            if (existing == null) {
                save(link);
            }
        }
    }

    @Override
    public List<Link> findAll() {
        return mongoOperation.findAll(Link.class);
    }

    @Override
    public Link findById(String id) {
        return mongoOperation.findById(id, Link.class);
    }

    @Override
    public List<Link> findToBroadcast() {
        Query query = new Query()
                .addCriteria(where("broadcasted").is(false))
                .limit(50);
        return mongoOperation.find(query, Link.class);
    }

    @Override
    public void deleteAll() {
        mongoOperation.dropCollection(Link.class);
    }

    @Override
    public void delete(Link link) {
        mongoOperation.remove(link);
    }
}
