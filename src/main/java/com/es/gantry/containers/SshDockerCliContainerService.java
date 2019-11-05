package com.es.gantry.containers;

import com.es.gantry.base.AbstractObjectListService;
import com.es.gantry.base.ObjectListExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SshDockerCliContainerService extends AbstractObjectListService<Container> implements ContainerService {

    private static final Logger logger = LoggerFactory.getLogger(SshDockerCliContainerService.class);

    private static final String CONTAINER_CREATED_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z";

    private static String LS_COMMAND =
            "docker ps --format=\"{{.ID}}%%{{.Image}}%%{{.Command}}%%{{.CreatedAt}}%%{{.Status}}%%{{.Ports}}%%{{.Names}}%%{{.Networks}}\"";

    private static final String INSPECT_COMMAND ="docker inspect %s";

    protected String getFindByIdFilterString(String id) {
        return "id=%s";
    }

    protected String getInspectCliCommand(String id) {
        return String.format(INSPECT_COMMAND,id);
    }

    protected String getFindAllCliCommand() {
        return LS_COMMAND;
    }

    protected ObjectListExtractor<Container> getObjectListExtractor() {
        return out->{
                List<Container> containers = new ArrayList<>();
            for (String line : out) {
                Container container = new Container();
                String[] fields = line.split("%%");
                container.setId(fields[0]);
                container.setImage(fields[1]);
                container.setCommand(fields[2]);
                String ts = fields[3];
                try {
                    Date createdAtDate =
                            new SimpleDateFormat(CONTAINER_CREATED_DATE_FORMAT)
                                    .parse(ts.substring(0, ts.lastIndexOf(' ')));
                    container.setCreated(new Timestamp(createdAtDate.getTime()));
                } catch (Exception ex) {
                    System.err.println("Error parsing container created at");
                }
                container.setStatus(fields[4]);
                container.setPorts(fields[5]);
                container.setNames(fields[6]);
                container.setNetworks(fields[7]);
                containers.add(container);
            }
            return containers;
        };
    }


}
