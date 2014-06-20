package com.learnedsomething.biz.manager.impl;

import com.learnedsomething.biz.manager.LinkManager;
import com.learnedsomething.biz.manager.Publisher;
import com.learnedsomething.biz.manager.SearchManager;
import com.learnedsomething.biz.manager.task.ParallelTask;
import com.learnedsomething.dao.LinkExtendedDao;
import com.learnedsomething.model.Link;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Manager for link manupulation. addition/removal etc.
 */
@Service
public class LinkManagerImpl implements LinkManager {
    private static final transient Logger LOG = Logger.getLogger(LinkManagerImpl.class);
    @Autowired
    ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    @Qualifier("mongoDaoImpl")
    LinkExtendedDao mongoDao;
    @Autowired
    SearchManager redditManager;
    @Autowired
    Publisher publisher;

    @Override
    public List<Link> findAll() {
        return mongoDao.findAll();
    }

    @Override
    public Link save(Link link) {
        if (link != null) {
            cleanse(link);
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
    @Scheduled(cron = "0 */1 * * * ?")
    public void index() {
        save(cleanse(redditManager.findNewLinks()));
    }

    @Override
    public void save(List<Link> links) {
        if (!CollectionUtils.isEmpty(links)) {
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
    @Scheduled(cron = "0 */1 * * * ?")
    public void broadcast() {
        List<Link> links = getLinksToBroadcast();
        for (Link link : links) {
            link.setBroadcasted(true);
            mongoDao.save(link);
            publisher.publish(link);
        }
    }

    @Override
    public void deleteAll() {
        mongoDao.deleteAll();
    }

    List<Link> cleanse(List<Link> links) {
        List<Link> newLinks = new ArrayList<>();
        if (!CollectionUtils.isEmpty(links)) {
            for (Link link : links) {
                try {
                    cleanse(link);
                    newLinks.add(link);
                } catch (Exception ignore) {
                    LOG.error("Something wrong while cleasing link", ignore);
                }
            }
        }
        return newLinks;
    }

    private void cleanse(Link link) {
        if (link.getId() == null) {
            link.setId(generateId(link.getUri()));
        }

        String text = removePrefix("til", link.getText());
        text = removePrefix("that", text);

        link.setText(text);
    }

    private String removePrefix(String prefix, String in) {
        String text = in;
        if (text.toUpperCase().startsWith(prefix.toUpperCase())) {
            text = text.substring(prefix.length()).trim();
        }
        text = text.substring(0, 1).toUpperCase() + text.substring(1);
        return text;
    }


    private List<Link> getLinksToBroadcast() {
        return mongoDao.findToBroadcast();
    }

    private String generateId(String uri) {
        return DigestUtils.md5DigestAsHex(uri.getBytes());
    }
}
