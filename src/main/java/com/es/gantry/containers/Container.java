package com.es.gantry.containers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class Container {

    private String id;

    private String image;

    private String command;

    private Timestamp created;

    private String createdString;

    private String status;

    private String ports;

    private String names;

    private String networks;

}
