package com.softserve.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@ToString
@Getter @Setter
public class PollDTO implements Serializable {
    @JsonProperty("idPoll")
    private Integer id;

    private String question;

    private Integer userId;

    private Integer status;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Timestamp creationDate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Timestamp modificationDate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Timestamp closedDate;

    private List<PollOptionDTO> pollOptions;
}
