package com.example.komunikator.domain;

import java.security.Principal;

public class MessageUser implements Principal {
    private String name;

    public MessageUser(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
