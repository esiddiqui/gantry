package com.es.gantry.images;

import com.es.gantry.auth.ThreadLocalAuthService;
import com.es.gantry.base.ObjectListController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;


@RestController
public class ImagesController extends ObjectListController<Image,ImageService> {

    private static final Logger logger = LoggerFactory.getLogger(ImagesController.class);

    public ImagesController(ImageService service, ThreadLocalAuthService threadLocalAuth) {
        super(service,threadLocalAuth);
    }

    @RequestMapping("/api/v1/images")
    public ResponseEntity<List<Image>> getImages(
            @RequestHeader(value = "Authorization", defaultValue = "") String authHeader,
            @RequestParam(value= "filter", defaultValue="")String filter) {
        return super.getObjects(authHeader,filter);
    }

    @RequestMapping("/api/v1/images/{id}")
    public ResponseEntity<List<HashMap<String,Object>>> inspectContainer(
            @RequestHeader(value = "Authorization", defaultValue = "") String authHeader,
            @PathVariable(value= "id")String id) {
        return super.inspectObjects(authHeader,id);
    }


}
