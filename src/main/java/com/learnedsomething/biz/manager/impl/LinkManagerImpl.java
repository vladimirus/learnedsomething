package com.learnedsomething.biz.manager.impl;

import com.learnedsomething.biz.manager.LinkManager;
import com.learnedsomething.biz.manager.SearchManager;
import com.learnedsomething.biz.manager.task.ParallelTask;
import com.learnedsomething.dao.LinkExtendedDao;
import com.learnedsomething.model.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import java.util.List;

/**
 * Manager for link manupulation. addition/removal etc.
 */
@Service
public class LinkManagerImpl implements LinkManager {
    @Autowired
    ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    @Qualifier("mongoDaoImpl")
    LinkExtendedDao mongoDao;
    @Autowired
    SearchManager redditManager;

    @Override
    public List<Link> findAll() {
        return mongoDao.findAll();
    }

    @Override
    public Link save(Link link) {
        if (link != null) {
            link.setId(generateId(link.getUri()));
            mongoDao.save(link);
        }
        return link;
    }

    @Override
    public void startIndexThread() {
        ParallelTask linkIndexer = new ParallelTask(this, "index");
        taskExecutor.execute(linkIndexer);
    }

    @Override
//    @Scheduled(cron = "0 */1 * * * ?") TODO
    public void index() {
        List<Link> links = redditManager.findNewLinks();
        save(links);
    }

    @Override
    public void save(List<Link> links) {
        if (!CollectionUtils.isEmpty(links)) {
            for (Link link : links) {
                link.setId(generateId(link.getUri()));
            }
            mongoDao.saveNew(links);
        }
    }

    @Override
    public Link findById(String id) {
        return mongoDao.findById(id);
    }

    @Override
    public void startBroadcastThread() {
        ParallelTask linkBroadcaster = new ParallelTask(this, "broadcast");
        taskExecutor.execute(linkBroadcaster);
    }

    @Override
//    @Scheduled(cron = "0 */1 * * * ?") TODO
    public void broadcast() {
//        List<Link> links = getLinksToBroadcast();
//        for (Link link : links) {
//            mongoDao.delete(link);
//        }
    }

    @Override
    public void deleteAll() {
        mongoDao.deleteAll();
    }

    private List<Link> getLinksToBroadcast() {
        return mongoDao.findToBroadcast();
    }

    private String generateId(String uri) {
        return DigestUtils.md5DigestAsHex(uri.getBytes());
    }
}
