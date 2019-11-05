package com.es.gantry.volumes;

import com.es.gantry.base.AbstractObjectListService;
import com.es.gantry.base.ObjectListExtractor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SshDockerCliVolumeService extends AbstractObjectListService<Volume> implements VolumeService  {

    private String LS_COMMAND =
            "docker volume ls --format \"{{.Name}},{{.Driver}},{{.Scope}},{{.Mountpoint}}\"";
    private String INSPECT_COMMAND = "docker volume inspect %s";

    protected String getFindByIdFilterString(String id) {
        return "id=%s";
    }

    protected String getInspectCliCommand(String id) {
        return String.format(INSPECT_COMMAND,id);
    }

    protected String getFindAllCliCommand() {
        return LS_COMMAND;
    }

    protected ObjectListExtractor<Volume> getObjectListExtractor() {
        return out->{
            List<Volume> volumes = new ArrayList<>();
            for (String line : out) {
                Volume volume = new Volume();
                String[] fields = line.split(",");
                volume.setName(fields[0]);
                volume.setDriver(fields[1]);
                volume.setScope(fields[2]);
                volume.setMountpoint(fields[3]);
                volumes.add(volume);
            }
            return volumes;
        };
    }

}
