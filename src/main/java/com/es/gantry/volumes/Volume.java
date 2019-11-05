package com.es.gantry.volumes;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Volume {

    private String name;

    private String driver;

    private String scope;

    private String mountpoint;

}
