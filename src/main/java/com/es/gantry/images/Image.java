package com.es.gantry.images;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class Image {

    private String repository;

    private String tag;

    private String id;

    private Timestamp created;

    private String createdString;

    private double size;

}
