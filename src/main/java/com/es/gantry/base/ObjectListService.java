package com.es.gantry.base;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface ObjectListService<T> {
    List<T> findAll();
    List<T> findAll(String filter);
    Optional<T> findById(String id);
    List<HashMap<String,Object>> inspect(String id);
}
