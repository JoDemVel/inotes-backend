package com.app.dharbor.inotes.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorMessages {
    public static final String USER_NOT_EXIST = "User with email '%s' does not exist";
    public static final String USER_NOT_AUTHENTICATED = "User is not authenticated";
    public static final String INVALID_PASSWORD = "Invalid Password";
    public static final String MIN_MAX_ROLES = "The user must have between 1 and 3 roles";
    public static final String INVALID_EMAIL = "Invalid Email";
    public static final String EMAIL_ALREADY_EXISTS = "Email '%s' Already Exists";
    public static final String USER_IS_DISABLED = "User with email '%s' is Disabled";
    public static final String USER_IS_BLOCKED = "User with email '%s' is Blocked";
    public static final String ROLE_NOT_EXIST = "The Role(s) specified Not Exist";
    public static final String NOTE_NOT_FOUND = "Note with ID: '%s' Not Found";

}
