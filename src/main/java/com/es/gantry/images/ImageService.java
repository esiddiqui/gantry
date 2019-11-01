package com.es.gantry.images;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface ImageService {

    List<Image> findAll();
    List<Image> findAll(String filter);
    Optional<Image> findById(String imageId);
    List<HashMap<String,Object>> inspect(String imageId);
}
