package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {

    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue =
            new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp rsl = null;
        if (req.method().equals("POST")) {
            add(req);
            rsl = new Resp("Message was posted", 200);
        } else if (req.method().equals("GET")) {
            rsl = new Resp(getMessage(req), 200);
        }
        return rsl;
    }

    private void add(Req req) {
        ConcurrentLinkedQueue<String> messages = queue.get(req.theme());
        if (messages == null) {
            messages = new ConcurrentLinkedQueue<>();
            messages.add(req.text());
            this.queue.putIfAbsent(req.theme(), new ConcurrentLinkedQueue<>());
        } else {
            messages.add(req.text());
        }
    }

    private String getMessage(Req req) {
        String message = queue.get(req.theme()).poll();
        return message == null ? "no messages" : message;
    }
}