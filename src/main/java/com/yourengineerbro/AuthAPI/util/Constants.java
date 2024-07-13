package com.yourengineerbro.AuthAPI.util;

public class Constants {
    public static final class Messages {
        public static final String MESSAGE =  "This is a protected resource.";
        public static final String SIGNED_OUT =  "User Successfully Signed out";
        public static final String USER_ALREADY_EXIST = "User with given email already exist";
        public static final String USER_NOT_FOUND = "User Not Found";
        public static final String INVALID_AUTH_HEADER = "Authorization header missing or invalid";
    }

    public static final class Path {
        public static final String AUTH_PATH = "/api/auth";
        public static final String PROTECTED_PATH = "/api/protected";
        public static final String REVOKE_TOKEN = "/revoke";
        public static final String SIGNUP_USER = "/signup";
        public static final String REFRESH_TOKEN = "/refresh";
        public static final String SIGNIN_USER = "/signin";
    }
    public static final class Exception{
        public static final String INVALID_TOKEN = "Invalid Token";
        public static final String INVALID_CREDENTIALS = "Invalid credentials";
    }

    public static final class Properties {
        public static final String JWT_SECRET = "${jwt.secret}";
        public static final String JWT_EXPIRATION = "${jwt.expiration}";
    }

}
