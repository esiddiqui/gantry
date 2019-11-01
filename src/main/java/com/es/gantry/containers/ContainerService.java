package com.es.gantry.containers;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface ContainerService {
    List<Container> findAll();
    List<Container> findAll(String filter);
    Optional<Container> findById(String containerId);
    List<HashMap<String,Object>> inspect(String containerId);

}
