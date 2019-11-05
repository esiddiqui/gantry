package com.es.gantry.volumes;


import com.es.gantry.auth.ThreadLocalAuthService;
import com.es.gantry.base.ObjectListController;
import com.es.gantry.containers.ContainerController;
import com.es.gantry.networks.Network;
import com.es.gantry.networks.NetworkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class VolumeController extends ObjectListController<Volume, VolumeService> {

    private static final Logger logger = LoggerFactory.getLogger(VolumeController.class);

    public VolumeController(VolumeService service, ThreadLocalAuthService threadLocalAuth) {
        super(service,threadLocalAuth);
    }

    @RequestMapping("/api/v1/volumes")
    public ResponseEntity<List<Volume>> getNetworks(
            @RequestHeader(value = "Authorization", defaultValue = "") String authHeader,
            @RequestParam(value= "filter", defaultValue="")String filter) {
        return super.getObjects(authHeader,filter);
    }

    @RequestMapping("/api/v1/volumes/{id}")
    public ResponseEntity<List<HashMap<String,Object>>> inspectContainer(
            @RequestHeader(value = "Authorization", defaultValue = "") String authHeader,
            @PathVariable(value= "id")String id) {
        return super.inspectObjects(authHeader,id);
    }

}
