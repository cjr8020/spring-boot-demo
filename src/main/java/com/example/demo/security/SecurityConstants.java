package com.example.demo.security;

public class SecurityConstants {
	public static final String SECRET = "SecretKeyToGenJWTs";
	public static final long EXPIRATION_TIME = 864_000_000; // 10 days
	public static final String BEARER_TOKEN_PREFIX = "Bearer ";
	public static final String AUTHZ_HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/users/sign-up";
}
