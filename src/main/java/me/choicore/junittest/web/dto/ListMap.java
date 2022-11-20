package me.choicore.junittest.web.dto;

import java.util.HashMap;
import java.util.List;

public class ListMap<String, T extends List> extends HashMap<String, T> {
    public ListMap(String key, T value) {
        super.put(key, value);
    }
}
