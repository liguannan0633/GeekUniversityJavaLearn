package com.geek.learn.db.readwrite;

import java.io.Serializable;
import lombok.Data;

/**
 * users
 * @author 
 */
@Data
public class Users implements Serializable {
    private Long id;

    private String name;

    private String comment;

    private static final long serialVersionUID = 1L;
}