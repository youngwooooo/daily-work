package com.work.daily.domain.pk;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

public class BoardCommentPk implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long commentSeq;

    @Id
    private long userSeq;

    @Id
    private String userId;

}