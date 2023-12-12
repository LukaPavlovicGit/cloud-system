package com.example.usermanagement.data.entities;

import com.example.usermanagement.data.entities.enums.PermissionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private PermissionType permission;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    List<User> users;

    public Permission(PermissionType permission) {
        this.permission = permission;
    }
}
