package com.example.backendquiz.config.oauth;
import java.util.Map;

public class OAuthUserInfo {

    private Map<String, Object> attributes;

    public OAuthUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getEmail() {
        return (String) attributes.get("email");
    }

    public String getNickname() {
        return (String) attributes.get("name");
    }

    public String getProviderId() {
        return (String) attributes.get("sub");
    }

}
