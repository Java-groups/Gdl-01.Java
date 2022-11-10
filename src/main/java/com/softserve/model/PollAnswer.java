package com.softserve.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "poll_answer")
@Getter
@Setter
public class PollAnswer {

    @Id
    @Column(name = "id_poll_answer")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_poll")
    private Integer idPoll;

    @Column(name = "id_poll_option")
    private Integer idPollOption;

    @Column(name = "id_app_user")
    private Integer userId;

    @Column(name = "status")
    private Integer status;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name = "creation_date")
    private Timestamp creationDate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modification_date")
    private Timestamp modificationDate;
}
