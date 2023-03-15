package com.noctus.spring_security.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue
    private int id;

    @Column(unique = true)
    private String name;

//    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
//    @JsonBackReference
//    private Set<User> users;


}
