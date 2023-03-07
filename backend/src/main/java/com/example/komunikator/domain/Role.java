package com.example.komunikator.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @JsonIgnore //relacja many-many powodowała stack overflow error, ponieważ JSON serializował użytkownika, który posiadał rolę, która należała do tego użytkownika itd
    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;
}
