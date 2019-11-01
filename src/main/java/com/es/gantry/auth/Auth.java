package com.es.gantry.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class Auth {

    private String token;

    private Timestamp expiry;

    private String host;

    private String user;

    private String key;

    private String message;

}
