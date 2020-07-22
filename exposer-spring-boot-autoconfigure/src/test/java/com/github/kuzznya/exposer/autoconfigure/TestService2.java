package com.github.kuzznya.exposer.autoconfigure;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService2 {
    public static class ListSizeResult {
        public int size;
        ListSizeResult(int size) {this.size = size;}
    }

    public ListSizeResult getListSize(List<String> list) {
        return new ListSizeResult(list.size());
    }

    public String joinTwoArgs(String arg1, String arg2) {
        return arg1 + arg2;
    }
}
