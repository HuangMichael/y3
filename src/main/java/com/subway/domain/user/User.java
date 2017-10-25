package com.subway.domain.user;


import com.subway.domain.locations.Vlocations;
import com.subway.domain.person.Person;
import com.subway.domain.role.Role;
import lombok.*;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.List;

/**
 * Created by huangbin on 2016/03/14 0023.
 * 用户信息
 */
@Entity
@Table(name = "T_USER")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(length = 20, unique = true, nullable = false)
    private String userName;
    @JsonIgnore
    @Column(length = 50, columnDefinition = "default '123456'")
    private String password;
    @Column(scale = 1000)
    private long sortNo;
    @Column(nullable = false, length = 1, columnDefinition = "varchar(1) default '1'")
    private String status;

    @OneToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @OneToOne
    @JoinColumn(name = "vlocations_id")
    private Vlocations vlocations;

    /*    @Column(length = 20)
        private String location;*/
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "t_role_user", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private List<Role> roleList;


}

