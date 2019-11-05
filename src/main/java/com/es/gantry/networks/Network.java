package com.es.gantry.networks;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class Network {

    private String id;

    private String name;

    private String driver;

    private String scope;

    private Timestamp createdAt;

}
