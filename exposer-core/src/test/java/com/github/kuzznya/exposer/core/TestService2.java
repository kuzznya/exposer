package com.github.kuzznya.exposer.core;

import org.springframework.stereotype.Service;

@Service
public class TestService2 {
    public String someFun() {
        return "Some func";
    }

    public String joinTwoArgs(String arg1, String arg2) {
        return arg1 + arg2;
    }
}
