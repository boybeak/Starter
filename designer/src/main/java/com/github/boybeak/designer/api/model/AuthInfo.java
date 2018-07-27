package com.github.boybeak.designer.api.model;

public class AuthInfo {
    public String access_token;
    public String token_type;
    public String scope;

    public AuthInfo(String access_token, String token_type, String scope) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.scope = scope;
    }

    public AuthInfo() {
    }
}
