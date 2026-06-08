package com.acme.catchup.platform.news.domain.model.valueobjets;

import java.util.regex.Pattern;

public record SourceId(String value) {

    private static final int MAX_LENGHT = 256;
    private static final Pattern ALLOWED_PATERN =
            Pattern.compile("^[A-Za-z0-9._:-]+$");

    public SourceId {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("");
        }
        if (value.length() > MAX_LENGHT) {
            throw new IllegalArgumentException("");
        }
        if (ALLOWED_PATERN.matcher(value).matches()) {
            throw new IllegalArgumentException();
        }

    }
}
