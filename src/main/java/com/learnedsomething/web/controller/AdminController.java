package com.learnedsomething.web.controller;

import com.learnedsomething.biz.manager.LinkManager;
import com.learnedsomething.model.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Controller for admin stuff like indexing, broadcasting.
 */
@Controller
@RequestMapping("/_admin")
public class AdminController {
    @Autowired
    LinkManager linkManager;

    @RequestMapping(value = "index", method = RequestMethod.GET)
    @ResponseBody
    public String index() {
        linkManager.startIndexThread();
        return "started";
    }

    @RequestMapping(value = "broadcast", method = RequestMethod.GET)
    @ResponseBody
    public String broadcast() {
        linkManager.startBroadcastThread();
        return "started";
    }

    @RequestMapping(value = "broadcast-queue", method = RequestMethod.GET)
    @ResponseBody
    public List<Link> broadcastQueue() {
        return linkManager.getLinksToBroadcast();
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    @ResponseBody
    public String delete() {
        linkManager.deleteAll();
        return "started";
    }
}
