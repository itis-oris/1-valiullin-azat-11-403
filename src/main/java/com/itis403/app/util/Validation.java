package com.itis403.app.util;

import java.util.regex.Pattern;

public class Validation {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^[a-zA-Z0-9_]{3,50}$");

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^.{6,100}$");

}