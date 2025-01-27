package com.app.dharbor.inotes.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Path {
    public static final String AUTH = "v1/auth";
    public static final String LOGIN = AUTH + "/login";
    public static final String SIGNUP = AUTH + "/signup";
}
