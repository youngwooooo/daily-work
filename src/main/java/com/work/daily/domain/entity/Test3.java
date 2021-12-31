package com.work.daily.domain.entity;

import com.work.daily.domain.UserRole;
import lombok.*;
import javax.persistence.*;


@Getter
@Entity
@NoArgsConstructor
@Builder
public class Test3 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USERNAME")
    private String name;

    @Column(name = "TEAM_ID")
    private Long teamId;

    @Builder
    public Test3(Long id, String name, Long teamId) {
        this.id = id;
        this.name = name;
        this.teamId = teamId;
    }
}
