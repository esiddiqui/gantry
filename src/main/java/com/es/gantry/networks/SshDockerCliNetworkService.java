package com.es.gantry.networks;

import com.es.gantry.base.AbstractObjectListService;
import com.es.gantry.base.ObjectListExtractor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("SshDockerCliNetworkService")
public class SshDockerCliNetworkService extends AbstractObjectListService<Network> implements NetworkService {

    private String LS_COMMAND =
            "docker network ls --format \"{{.ID}},{{.Name}},{{.Driver}},{{.Scope}}\"";

    private String INSPECT_COMMAND = "docker network inspect %s";

    protected String getFindByIdFilterString(String id) {
        return "id=%s";
    }

    protected String getInspectCliCommand(String id) {
        return String.format(INSPECT_COMMAND,id);
    }

    protected String getFindAllCliCommand() {
        return LS_COMMAND;
    }

    protected ObjectListExtractor<Network> getObjectListExtractor() {
        return out->{
            List<Network> networks = new ArrayList<>();
            for (String line : out) {
                Network network = new Network();
                String[] fields = line.split(",");
                network.setId(fields[0]);
                network.setName(fields[1]);
                network.setDriver(fields[2]);
                network.setScope(fields[3]);
                networks.add(network);
            }
            return networks;
        };
    }
}
