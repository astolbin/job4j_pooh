package ru.job4j.pooh;

import java.util.List;

public class Req {
    private final String method;
    private final String mode;
    private final String text;
    private final String theme;

    private Req(String method, String mode, String text, String theme) {
        this.method = method;
        this.mode = mode;
        this.text = text;
        this.theme = theme;
    }

    public static Req of(String content) {
        String[] lines = content.split(System.lineSeparator());
        return new Req(parseMethod(lines[0]),
                parseMode(lines[0]),
                lines[lines.length - 1],
                parseTheme(lines[0])
        );
    }

    public String method() {
        return method;
    }

    public String mode() {
        return mode;
    }

    public String text() {
        return text;
    }

    public String theme() {
        return theme;
    }

    private static String parseMethod(String firstLine) {
        return List.of("POST", "GET").stream()
                .filter(firstLine::contains)
                .findFirst()
                .orElse("");
    }

    private static String parseMode(String firstLine) {
        return firstLine.split("/")[1];
    }

    private static String parseTheme(String firstLine) {
        String[] path = firstLine.split("\\s+")[1].split("/");
        return path.length > 3 ? path[2].concat("/").concat(path[3]) : path[2];
    }
}