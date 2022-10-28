package com.softserve.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "poll")
@Getter @Setter
public class Poll {
    @Id
    @Column(name = "id_poll")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPoll;

    @Column(name = "question")
    private String question;

    @Column(name = "id_app_user")
    private Integer userId;

    @Column(name = "status")
    private Integer status;

    @Column(name = "closed_date")
    private Timestamp closedDate;

    @Column(name = "creation_date")
    private Timestamp creationDate;

    @Column(name = "modification_date")
    private Timestamp modificationDate;

    @OneToMany(mappedBy = "poll", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<PollOption> pollOptions = new ArrayList<>();
}
