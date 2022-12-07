package com.app.schservice.users.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="sys_user_roles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_role_id")
    private Long userroleid;

    @ManyToOne
    @JoinColumn(name="user_role_role")
    private Roles roles;

    @ManyToOne
    @JoinColumn(name="user_role_user")
    private User user;

    @Column(name="user_role_name")
    private String roleName;

}
