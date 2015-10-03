package com.crimepunch.app.model;

import lombok.Data;

import java.util.Map;

/**
 * Created by user-1 on 3/10/15.
 */
@Data
public class User {

    private String id;
    private Map<String, String> attributes;
}
