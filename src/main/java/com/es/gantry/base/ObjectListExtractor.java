package com.es.gantry.base;

import java.util.List;

@FunctionalInterface()
public interface ObjectListExtractor<T> {
    List<T> extract(List<String> out);
}
