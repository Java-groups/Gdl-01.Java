package com.softserve.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softserve.model.Poll;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Getter @Setter
public class PollOptionDTO implements Serializable {
    private Integer id;
    private String value;
    private Integer order;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Poll poll;
}
