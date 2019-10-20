package com.es.gantry.containers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ContainerController {

    @Autowired
    ContainerService service;

    @RequestMapping("/api/v1/containers")
    public ResponseEntity<List<Container>> getControllers() {
        List<Container> containers = this.service.findAll();
        if (containers.size()>0)
            return ResponseEntity.ok(containers);
        else
            return ResponseEntity.ok(containers);
    }

}
