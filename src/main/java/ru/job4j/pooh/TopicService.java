package ru.job4j.pooh;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {

    private final ConcurrentHashMap<String,
            ConcurrentHashMap<Integer, ConcurrentLinkedQueue<String>>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp rsl = null;
        if (req.method().equals("POST")) {
            rsl = new Resp(add(req), 200);
        } else if (req.method().equals("GET")) {
            rsl = new Resp(getMessage(req), 200);
        }
        return rsl;
    }

    private String add(Req req) {
        String rsl;
        String themeId = req.theme().split("/")[0];
        ConcurrentHashMap<Integer, ConcurrentLinkedQueue<String>> map = queue.get(themeId);
        if (map == null) {
            map = new ConcurrentHashMap<>();
            queue.putIfAbsent(themeId, map);
            rsl = "Post was created";
        } else {
            int count = 0;
            for (int index : map.keySet()) {
                map.get(index).add(req.text());
                count++;
            }
            rsl = String.format("Message sent to %s subscribers", count);
        }
        return rsl;
    }

    private String getMessage(Req req) {
        String rsl;
        String themeId = req.theme().split("/")[0];
        ConcurrentHashMap<Integer, ConcurrentLinkedQueue<String>> map = queue.get(themeId);
        int postId = Integer.parseInt(req.theme().split("/")[1]);
        if (map == null) {
            rsl = "Post not found";
        } else {
            ConcurrentLinkedQueue<String> messages = map.get(postId);
            if (messages == null) {
                map.putIfAbsent(postId, new ConcurrentLinkedQueue<>());
                rsl = "Was subscribed";
            } else {
                rsl = Objects.requireNonNullElse(messages.poll(), "Queue is empty");
            }
        }
        return rsl;
    }
}