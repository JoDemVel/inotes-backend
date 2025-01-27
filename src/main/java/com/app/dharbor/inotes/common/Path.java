package com.app.dharbor.inotes.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Path {
    public static final String BASE = "v1";

    public static final String AUTH_ONLY = "/auth";
    public static final String AUTH = BASE + AUTH_ONLY;
    public static final String AUTH_ALL = "/" + AUTH + "/**";
    public static final String LOGIN = AUTH + "/login";
    public static final String SIGNUP = AUTH + "/signup";

    public static final String NOTE_ONLY = "/notes";
    public static final String NOTE = BASE + NOTE_ONLY;
    public static final String NOTE_ID = NOTE + "/{id}";

    public static final String TAG_ONLY = "/tags";
    public static final String TAG = BASE + TAG_ONLY;
    public static final String TAG_ID = TAG + "/{id}";

    public static final String NOTE_ID_TAGS = NOTE_ID + TAG_ONLY;
    public static final String NOTE_ID_TAGS_ID = NOTE_ID_TAGS + "/{tagId}";
}
