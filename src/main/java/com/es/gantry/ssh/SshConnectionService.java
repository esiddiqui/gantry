package com.es.gantry.ssh;

import com.es.shell.ExecuteRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;

@Service
public class SshConnectionService {

    @Value("${gantry.certs.path}")
    private String certsPath;

    @Value("${file.separator}")
    private String pathSeparator;

    public  ExecuteRequest getConnectionForHost(String host, String user, String key) throws Exception {

        if (StringUtils.isEmpty(user))
            throw new Exception("User cannot be null");

        if (StringUtils.isEmpty(key))
            throw new Exception("Key cannot be null");

        String privateKeyFileFullPath = this.certsPath + pathSeparator + key;
        if (!new File(privateKeyFileFullPath).exists())
            throw new Exception("Private key doesn't exist");

        return new ExecuteRequest.ExecuteRequestBuilder()
                .host(host)
                .identityFile(privateKeyFileFullPath)
                .userName(user)
                .build();
    }
}
