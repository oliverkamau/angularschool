package com.app.schservice.users.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="sys_usertypes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="type_code")
    private Long typeCode;

    @Column(name="type_name")
    private String typeName;

    @Column(name="type_desc")
    private String typeDesc;


}
