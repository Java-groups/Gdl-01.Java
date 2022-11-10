package com.softserve.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "poll_option")
@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PollOption {
    @Id
    @Column(name = "id_poll_option")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "poll_value")
    private String value;

    @Column(name = "poll_order")
    private Integer order;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_poll")
    private Poll poll;
}
