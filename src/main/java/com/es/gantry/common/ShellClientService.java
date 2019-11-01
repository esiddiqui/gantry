package com.es.gantry.common;

import com.es.gantry.auth.Auth;
import com.es.gantry.auth.ThreadLocalAuthService;
import com.es.gantry.ssh.SshConnectionService;
import com.es.shell.ExecuteRequest;
import com.es.shell.ExecuteResult;
import com.es.shell.JSchShell;
import com.es.shell.Shell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class ShellClientService {

    @Autowired
    protected SshConnectionService sshConnectionService;

    @Autowired
    protected ThreadLocalAuthService authService;

    protected Shell shell = new JSchShell();

    public ExecuteResult exec(String command) throws Exception {
        Auth auth = this.authService.getAuth();
        if (auth == null)
            throw new Exception("Error in authentication");

        String host = auth.getHost();
        String user = auth.getUser();
        String key = auth.getKey();

        ExecuteRequest request =
                this.sshConnectionService
                        .getConnectionForHost(host, user, key);

        request.setCommand(Arrays.asList(command));
        return shell.execute(request);
    }
}
