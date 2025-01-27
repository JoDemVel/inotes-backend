package com.app.dharbor.inotes.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Path {
    public static final String BASE = "v1";

    public static final String AUTH = BASE + "/auth";
    public static final String AUTH_ALL = "/" + AUTH + "/**";
    public static final String LOGIN = AUTH + "/login";
    public static final String SIGNUP = AUTH + "/signup";

    public static final String NOTE = BASE + "/notes";
    public static final String NOTE_ALL = "/" + NOTE + "/**";
    public static final String NOTE_ID = NOTE + "/{id}";
}
