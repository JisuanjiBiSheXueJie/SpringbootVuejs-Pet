package com.evan.aiu.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author kanghb
 * @data 2022/5/4
 */
@Data
public class CommentsDTO {
    private int id;

    private String contents;

    private String adoptname;

    private String animalName;

    private int aid;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date upttime;
}
