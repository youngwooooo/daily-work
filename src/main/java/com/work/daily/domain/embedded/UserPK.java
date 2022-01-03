package com.work.daily.domain.embedded;


import javax.persistence.Id;
import java.io.Serializable;

public class UserPK implements Serializable {

    @Id
    private long userSeq;

    @Id
    private String userId;
}
