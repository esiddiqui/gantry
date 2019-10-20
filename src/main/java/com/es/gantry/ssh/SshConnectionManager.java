package com.es.gantry.ssh;

import com.es.shell.ExecuteRequest;

public class SshConnectionManager {

    public static ExecuteRequest getConnectionForHost(String host) {
        if (host.equalsIgnoreCase("ops-liv-dkr01.bcommons.net")) {
            return new ExecuteRequest.ExecuteRequestBuilder()
                    .host(host)
                    .identityFile("/Users/ehteshamsiddiqui/.ssh/biblio-docker02.pem")
                    .userName("root")
                    .build();
        } else if (host.equalsIgnoreCase("nerf001.core.int.bf1.corp.pvt")) {
            return new ExecuteRequest.ExecuteRequestBuilder()
                    .host(host)
                    .identityFile("/Users/ehteshamsiddiqui/.ssh/integration_ansible.pem")
                    .userName("ansible")
                    .build();
        }

        else
            return null;
    }
}
