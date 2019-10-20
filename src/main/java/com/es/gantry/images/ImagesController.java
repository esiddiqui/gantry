package com.es.gantry.images;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;


@RestController
public class ImagesController {

    @Autowired
    ImageService service;

    @RequestMapping("/api/v1/images")
    public ResponseEntity<List<Image>> getImages() {

        List<Image> images = this.service.findAll();

        if (images.size()>0)
            return ResponseEntity.ok(images);
        else
            return ResponseEntity.ok(images);

//        Image m = new Image();
//        m.setId("26d5e4f5070d");
//        m.setCreatedString("22 months ago");
//        m.setSize(367*1024*1024);
//        m.setRepository("mongo");
//        m.setTag("latest");
//        List<Image> images = Arrays.asList(m);
        //return ResponseEntity.ok(images);
    }

}
