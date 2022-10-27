package com.softserve.dto;

import com.softserve.model.Poll;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Getter @Setter
public class PollOptionDTO implements Serializable {
    private String value;

    private Integer order;

    private Poll poll;
}
