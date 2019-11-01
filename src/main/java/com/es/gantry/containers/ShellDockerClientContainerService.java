package com.es.gantry.containers;


import com.es.gantry.common.ShellClientService;
import com.es.shell.ExecuteResult;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ShellDockerClientContainerService
        extends ShellClientService implements ContainerService {

    private static final Logger logger = LoggerFactory.getLogger(ShellDockerClientContainerService.class);

    private static final String CONTAINER_CREATED_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z";

    private static final String CONTAINER_LS_WITH_FORMAT =
            "docker ps --format=\"{{.ID}},{{.Image}},{{.Command}},{{.CreatedAt}},{{.Status}},{{.Ports}},{{.Names}},{{.Networks}}\"";

    private static final String CONTAINER_INSPECT ="docker inspect %s";

    private static final String FILTER_ARG = " --filter ";

    @Override
    public List<Container> findAll() {
        return findAll(null);
    }

    @Override
    public Optional<Container> findById(String containerId) {
        List<Container> containers = this.findAll("id="+containerId);
        if (containers.size()==0)
            return Optional.empty();
        else
            return Optional.of(containers.get(0));
    }

    @Override
    public List<Container> findAll(String filter) {
        List<Container> containers = new ArrayList<>();
        try {
            String command = CONTAINER_LS_WITH_FORMAT;
            if (!StringUtils.isEmpty(filter)) {
                command += FILTER_ARG + filter;
            }
            ExecuteResult rs = this.exec(command);
            if (rs.getReturnStatus() == ExecuteResult.CommandStatus.COMMAND_RAN_SUCCESSFUL &&
                    rs.getExitStatus() == 0) {
                for (String line : rs.getOut()) {
                    Container container = new Container();
                    String[] fields = line.split(",");
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
            }
            return containers;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

        return containers;
    }


    @Override
    public List<HashMap<String,Object>> inspect(String containerId) {
        try {
            ExecuteResult rs = this.exec(String.format(CONTAINER_INSPECT,containerId));
            if (rs.getReturnStatus()== ExecuteResult.CommandStatus.COMMAND_RAN_SUCCESSFUL) {
                StringBuilder output = new StringBuilder();
                rs.getOut().stream().forEach(output::append);
                logger.info(output.toString());
                String json = output.toString();
                List<HashMap<String,Object>> containers = new ObjectMapper().readValue(
                        json,new TypeReference<List<HashMap<String,Object>>>(){});
                return containers;
            } else
                return null;
        } catch (Exception ex) {
            return null;
        }
    }

}
